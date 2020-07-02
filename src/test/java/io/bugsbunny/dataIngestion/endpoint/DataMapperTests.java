package io.bugsbunny.dataIngestion.endpoint;

import com.google.gson.JsonParser;
import io.bugsbunny.dataScience.service.TrainingWorkflow;
import io.bugsbunny.persistence.MongoDBJsonStore;
import io.bugsbunny.restClient.MLFlowRunClient;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.json.JSONObject;
import org.json.XML;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class DataMapperTests {
    private static Logger logger = LoggerFactory.getLogger(DataMapperTests.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    @Inject
    private TrainingWorkflow trainingWorkflow;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Test
    public void testMapWithOneToOneFields() throws Exception{
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("airlinesDataOneToOneFields.json"),
                StandardCharsets.UTF_8);
        logger.info(json);

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", json);
        input.addProperty("destinationSchema", json);
        input.addProperty("sourceData", json);


        Response response = given().body(input.toString()).when().post("/dataMapper/map")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        //assert the body
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertEquals("123456789", jsonObject.get("Id").getAsString());
        assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());
    }

    @Test
    public void testMapWithScatteredFields() throws Exception{
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("airlinesDataScatteredFields.json"),
                StandardCharsets.UTF_8);
        logger.info(json);

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", json);
        input.addProperty("destinationSchema", json);
        input.addProperty("sourceData", json);


        Response response = given().body(input.toString()).when().post("/dataMapper/map")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        //assert the body
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertEquals("123456789", jsonObject.get("Id").getAsString());
        assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());
    }

    @Test
    public void testMapXmlSourceData() throws Exception
    {
        String xml = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("people.xml"),
                StandardCharsets.UTF_8);

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", xml);
        input.addProperty("destinationSchema", xml);
        input.addProperty("sourceData", xml);


        Response response = given().body(input.toString()).when().post("/dataMapper/mapXml")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        JsonObject storedJson = this.mongoDBJsonStore.getIngestion("1");
        logger.info("*******");
        logger.info(storedJson.toString());
        logger.info("*******");
    }

    @Test
    public void testModelTraining() throws Exception
    {
        //Map<Integer,String> dataMap = this.readEnumCSV();
        //logger.info(dataMap.toString());

        //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
        //int labelIndex = 4;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
        //int numClasses = 3;     //3 classes (types of iris flowers) in the iris data set. Classes have integer values 0, 1 or 2
        //int batchSizeTraining = 30;
        //DataSet dataSet = this.readCSVDataset(batchSizeTraining, labelIndex, numClasses);
        //logger.info(dataSet.toString());

        File file = new File("tmp/data");
        FileInputStream fis = new FileInputStream(file);
        String data = IOUtils.toString(fis, StandardCharsets.UTF_8);
        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", data);
        input.addProperty("destinationSchema", data);
        input.addProperty("sourceData", data);

        Response response = given().body(input.toString()).when().post("/dataMapper/mapCsv")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        //logger.info("****");
        //logger.info(response.getStatusLine());
        //logger.info(jsonResponse);
        //logger.info("****");

        //Kickoff the Training
        String runId = this.trainingWorkflow.startTraining();

        logger.info("*******");
        logger.info("RunId: "+runId);
        logger.info("*******");
        assertNotNull(runId);

        String runJson = this.mlFlowRunClient.getRun(runId);
        //logger.info(runJson);
    }

    //-----------------------------------------------------------------------------
    private DataSet readCSVDataset(int batchSize, int labelIndex, int numClasses)
            throws IOException, InterruptedException{
        File file = new File("tmp/data");

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(file));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, labelIndex, numClasses);
        return iterator.next();
    }

    private Map<Integer,String> readEnumCSV() throws Exception{
        File file = new File("tmp/data");
        FileInputStream fis = new FileInputStream(file);
        String data = IOUtils.toString(fis, StandardCharsets.UTF_8);

        InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        List<String> lines = IOUtils.readLines(is);
        Map<Integer,String> enums = new HashMap<>();
        for(String line:lines){
            String[] parts = line.split(",");
            enums.put(Integer.parseInt(parts[0]),parts[1]);
        }
        return enums;
    }
}