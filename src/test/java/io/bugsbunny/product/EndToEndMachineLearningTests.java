package io.bugsbunny.product;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.JsonParser;
import io.bugsbunny.dataScience.service.TrainingWorkflow;
import io.bugsbunny.persistence.MongoDBJsonStore;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;

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

import io.bugsbunny.restclient.MLFlowRunClient;

@QuarkusTest
public class EndToEndMachineLearningTests
{
    private static Logger logger = LoggerFactory.getLogger(EndToEndMachineLearningTests.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    @Inject
    private TrainingWorkflow trainingWorkflow;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Test
    public void testIngestionTrainingAndProdPush() throws Exception
    {
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

        jsonObject = new JsonObject();
        jsonObject.addProperty("script","learningTime()cause_I_am_a_lazy_python");
        jsonObject.addProperty("mlPlatform","tensorflow");
        response = given().body(jsonObject.toString()).when().post("/trainModel/train")
                .andReturn();
        jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        jsonObject = new JsonObject();
        response = given().body(jsonObject.toString()).when().post("/liveModel/calculate")
                .andReturn();
        jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        JsonObject result = JsonParser.parseString(jsonResponse).getAsJsonObject();
        assertNotNull(result.get("calculation"));
    }
}