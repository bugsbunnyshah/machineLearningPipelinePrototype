package io.bugsbunny.ai.model;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;

import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.api.util.ClassPathResource;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.*;

import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@QuarkusTest
public class DataTests implements Serializable {
    protected static final Logger log = LoggerFactory.getLogger(DataTests.class);

    //Images are of format given by allowedExtension -
    protected static final String [] allowedExtensions = BaseImageLoader.ALLOWED_FORMATS;

    protected static final long seed = 12345;

    public static final Random randNumGen = new Random(seed);

    protected static int height = 50;
    protected static int width = 50;
    protected static int channels = 3;
    protected static int numExamples = 80;
    protected static int outputNum = 3;

    private static Map<Integer,String> eats = readEnumCSV("/DataExamples/animals/eats.csv");
    private static Map<Integer,String> sounds = readEnumCSV("/DataExamples/animals/sounds.csv");
    private static Map<Integer,String> classifiers = readEnumCSV("/DataExamples/animals/classifiers.csv");

    @Test
    public void testImagePipeline() throws Exception
    {
        //DIRECTORY STRUCTURE:
        //Images in the dataset have to be organized in directories by class/label.
        //In this example there are ten images in three classes
        //Here is the directory structure
        //                                    parentDir
        //                                  /    |     \
        //                                 /     |      \
        //                            labelA  labelB   labelC
        //
        //Set your data up like this so that labels from each label/class live in their own directory
        //And these label/class directories live together in the parent directory
        //
        //
        File parentDir = new File("/Users/babyboy/training/deepLearning/dl4j-0.7.2/deeplearning4j/appgal-learning/src/test/resources/DataExamples/ImagePipeline/");
        //Files in directories under the parent dir that have "allowed extensions" plit needs a random number generator for reproducibility when splitting the files into train and test
        FileSplit filesInDir = new FileSplit(parentDir, allowedExtensions, randNumGen);

        //You do not have to manually specify labels. This class (instantiated as below) will
        //parse the parent dir and use the name of the subdirectories as label/class names
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        //The balanced path filter gives you fine tune control of the min/max cases to load for each class
        //Below is a bare bones version. Refer to javadocs for details
        BalancedPathFilter pathFilter = new BalancedPathFilter(randNumGen, allowedExtensions, labelMaker);

        //Split the image files into train and test. Specify the train test split as 80%,20%
        InputSplit[] filesInDirSplit = filesInDir.sample(pathFilter, 80, 20);
        InputSplit trainData = filesInDirSplit[0];
        InputSplit testData = filesInDirSplit[1];

        //Specifying a new record reader with the height and width you want the images to be resized to.
        //Note that the images in this example are all of different size
        //They will all be resized to the height and width specified below
        ImageRecordReader recordReader = new ImageRecordReader(height,width,channels,labelMaker);

        //Often there is a need to transforming images to artificially increase the size of the dataset
        //DataVec has built in powerful features from OpenCV
        //You can chain transformations as shown below, write your own classes that will say detect a face and crop to size
        ImageTransform transform = new MultiImageTransform(randNumGen,
            new CropImageTransform(10), new FlipImageTransform(),
            new ScaleImageTransform(10), new WarpImageTransform(10));


        //You can use the ShowImageTransform to view your images
        //Code below gives you a look before and after, for a side by side comparison
        //ImageTransform transform = new MultiImageTransform(randNumGen,new ShowImageTransform("Display - before "));

        //Initialize the record reader with the train data and the transform chain
        recordReader.initialize(trainData,transform);
        //convert the record reader to an iterator for training - Refer to other examples for how to use an iterator
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, 10, 1, outputNum);
        while (dataIter.hasNext()) {
            DataSet ds = dataIter.next();
            System.out.println(ds);
            try {
                Thread.sleep(3000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        recordReader.reset();

        transform = new MultiImageTransform(randNumGen,new CropImageTransform(50), new ShowImageTransform("Display - after"));
        recordReader.initialize(trainData,transform);
        recordReader.initialize(trainData);
        dataIter = new RecordReaderDataSetIterator(recordReader, 10, 1, outputNum);
        while (dataIter.hasNext()) {
            DataSet ds = dataIter.next();
        }
        recordReader.reset();
    }

    public static void logAnimals(Map<Integer,Map<String,Object>> animals){
        for(Map<String,Object> a:animals.values())
            log.info(a.toString());
    }

    public static void setFittedClassifiers(INDArray output, Map<Integer,Map<String,Object>> animals){
        for (int i = 0; i < output.rows() ; i++) {

            // set the classification from the fitted results
            animals.get(i).put("classifier",
                    classifiers.get(maxIndex(getFloatArrayFromSlice(output.slice(i)))));

        }

    }


    /**
     * This method is to show how to convert the INDArray to a float array. This is to
     * provide some more examples on how to convert INDArray to types that are more java
     * centric.
     *
     * @param rowSlice
     * @return
     */
    public static float[] getFloatArrayFromSlice(INDArray rowSlice){
        float[] result = new float[rowSlice.columns()];
        for (int i = 0; i < rowSlice.columns(); i++) {
            result[i] = rowSlice.getFloat(i);
        }
        return result;
    }

    /**
     * find the maximum item index. This is used when the data is fitted and we
     * want to determine which class to assign the test row to
     *
     * @param vals
     * @return
     */
    public static int maxIndex(float[] vals){
        int maxIndex = 0;
        for (int i = 1; i < vals.length; i++){
            float newnumber = vals[i];
            if ((newnumber > vals[maxIndex])){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * take the dataset loaded for the matric and make the record model out of it so
     * we can correlate the fitted classifier to the record.
     *
     * @param testData
     * @return
     */
    public static Map<Integer,Map<String,Object>> makeAnimalsForTesting(DataSet testData){
        Map<Integer,Map<String,Object>> animals = new HashMap<>();

        INDArray features = testData.getFeatures();
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


    public static Map<Integer,String> readEnumCSV(String csvFileClasspath) {
        try{
            List<String> lines = IOUtils.readLines(new ClassPathResource(csvFileClasspath).getInputStream());
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

    /**
     * used for testing and training
     *
     * @param csvFileClasspath
     * @param batchSize
     * @param labelIndex
     * @param numClasses
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private static DataSet readCSVDataset(
            String csvFileClasspath, int batchSize, int labelIndex, int numClasses)
            throws IOException, InterruptedException{

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new ClassPathResource(csvFileClasspath).getFile()));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr,batchSize,labelIndex,numClasses);
        return iterator.next();
    }
}
