package io.bugsbunny.dataScience.model;


import com.google.gson.JsonObject;
import io.bugsbunny.persistence.MongoDBJsonStore;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.castor.core.util.Base64Encoder;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This example is intended to be a simple CSV classifier that seperates the training data
 * from the test data for the classification of animals. It would be suitable as a beginner's
 * example because not only does it load CSV data into the network, it also shows how to extract the
 * data and display the results of the classification, as well as a simple method to map the lables
 * from the testing data into the results.
 *
 * @author Clay Graham
 */
@QuarkusTest
public class BasicCSVClassifierTests {

    private static Logger log = LoggerFactory.getLogger(BasicCSVClassifierTests.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    private Map<Integer,String> eats;
    private Map<Integer,String> sounds;
    private Map<Integer,String> classifiers;

    @BeforeEach
    public void setUp()
    {
        this.eats = this.readEnumCSV();
        this.sounds = this.readEnumCSV();
        this.classifiers = this.readEnumCSV();
    }

    @Test
    public void testClassifier() throws Exception
    {

        try {

            //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
            int labelIndex = 4;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
            int numClasses = 3;     //3 classes (types of iris flowers) in the iris data set. Classes have integer values 0, 1 or 2

            int batchSizeTraining = 30;    //Iris data set: 150 examples total. We are loading all of them into one DataSet (not recommended for large data sets)
            DataSet trainingData = this.readCSVDataset(batchSizeTraining, labelIndex, numClasses);

            // this is the data we want to classify
            int batchSizeTest = 44;
            DataSet testData = this.readCSVDataset(batchSizeTest, labelIndex, numClasses);


            // make the data model for records prior to normalization, because it
            // changes the data.
            Map<Integer,Map<String,Object>> animals = makeAnimalsForTesting(testData);


            //We need to normalize our data. We'll use NormalizeStandardize (which gives us mean 0, unit variance):
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
            normalizer.transform(trainingData);     //Apply normalization to the training data
            normalizer.transform(testData);         //Apply normalization to the test data. This is using statistics calculated from the *training* set

            final int numInputs = 4;
            int outputNum = 3;
            int iterations = 1000;
            long seed = 6;

            log.info("Build model....");
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                    .seed(seed)
                    .iterations(iterations)
                    .activation(Activation.TANH)
                    .weightInit(WeightInit.XAVIER)
                    .learningRate(0.1)
                    .regularization(true).l2(1e-4)
                    .list()
                    .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(3).build())
                    .layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
                    .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                            .activation(Activation.SOFTMAX).nIn(3).nOut(outputNum).build())
                    .backprop(true).pretrain(false)
                    .build();

            //run the model
            MultiLayerNetwork model = new MultiLayerNetwork(conf);
            model.init();
            model.setListeners(new ScoreIterationListener(100));

            model.fit(trainingData);

            //evaluate the model on the test set
            Evaluation eval = new Evaluation(3);
            INDArray output = model.output(testData.getFeatureMatrix());

            eval.eval(testData.getLabels(), output);
            log.info(eval.stats());

            setFittedClassifiers(output, animals);
            logAnimals(animals);

            ByteArrayOutputStream modelStream = null;
            ObjectOutputStream out = null;
            try {
                modelStream = new ByteArrayOutputStream();
                out = new ObjectOutputStream(modelStream);
                out.writeObject(model);
            }
            finally
            {
                out.close();
                modelStream.close();
            }

            //Restore serialized model
            Base64.Encoder encoder = Base64.getEncoder();
            String modelString = encoder.encodeToString(modelStream.toByteArray());
            ObjectInputStream in = null;
            MultiLayerNetwork restoredModel = null;
            try {
                Base64.Decoder decoder = Base64.getDecoder();
                in = new ObjectInputStream(new ByteArrayInputStream(decoder.decode(modelString)));
                //in = new ObjectInputStream(new ByteArrayInputStream(modelStream.toByteArray()));
                restoredModel = (MultiLayerNetwork) in.readObject();
            } finally
            {
                if(in != null) {
                    in.close();
                }
            }

            restoredModel.fit(trainingData);

            //evaluate the model on the test set
            eval = new Evaluation(3);
            output = restoredModel.output(testData.getFeatureMatrix());

            eval.eval(testData.getLabels(), output);
            log.info(eval.stats());

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void logAnimals(Map<Integer,Map<String,Object>> animals){
        for(Map<String,Object> a:animals.values())
            log.info(a.toString());
    }

    private void setFittedClassifiers(INDArray output, Map<Integer,Map<String,Object>> animals){
        for (int i = 0; i < output.rows() ; i++) {

            // set the classification from the fitted results
            animals.get(i).put("classifier",
                    classifiers.get(maxIndex(getFloatArrayFromSlice(output.slice(i)))));

        }

    }

    private float[] getFloatArrayFromSlice(INDArray rowSlice){
        float[] result = new float[rowSlice.columns()];
        for (int i = 0; i < rowSlice.columns(); i++) {
            result[i] = rowSlice.getFloat(i);
        }
        return result;
    }

    private int maxIndex(float[] vals){
        int maxIndex = 0;
        for (int i = 1; i < vals.length; i++){
            float newnumber = vals[i];
            if ((newnumber > vals[maxIndex])){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private Map<Integer,Map<String,Object>> makeAnimalsForTesting(DataSet testData){
        Map<Integer,Map<String,Object>> animals = new HashMap<>();

        INDArray features = testData.getFeatureMatrix();
        for (int i = 0; i < features.rows() ; i++) {
            INDArray slice = features.slice(i);
            Map<String,Object> animal = new HashMap();

            //set the attributes
            animal.put("yearsLived", slice.getInt(0));
            animal.put("eats", eats.get(slice.getInt(1)));
            animal.put("sounds", sounds.get(slice.getInt(2)));
            animal.put("weight", slice.getFloat(3));

            animals.put(i,animal);
        }
        return animals;

    }

    private Map<Integer,String> readEnumCSV() {
        try{
            List<JsonObject> jsons = this.mongoDBJsonStore.getIngestionImages();
            JsonObject json = jsons.get(0);
            String data = json.get("data").getAsString();
            InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            List<String> lines = IOUtils.readLines(is);
            Map<Integer,String> enums = new HashMap<>();
            for(String line:lines){
                String[] parts = line.split(",");
                enums.put(Integer.parseInt(parts[0]),parts[1]);
            }
            return enums;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private DataSet readCSVDataset(int batchSize, int labelIndex, int numClasses)
            throws IOException, InterruptedException{
        //List<JsonObject> jsons = mongoDBJsonStore.getIngestionImages();
        //JsonObject json = jsons.get(0);
        //String data = json.get("data").getAsString();

        String data = "19,0,1,10,0\\n9,1,2,60,0\\n";
        File file = new File("tmp/data");
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.write(data, fos, StandardCharsets.UTF_8);

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(file));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, labelIndex, numClasses);
        return iterator.next();
    }
}
