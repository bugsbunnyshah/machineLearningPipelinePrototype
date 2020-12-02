package io.bugsbunny.dataScience.service;


import com.google.gson.*;

import io.bugsbunny.restclient.MLFlowRunClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class AIAgnosticTrainingWorkflowTests
{
    private static Logger logger = LoggerFactory.getLogger(AIAgnosticTrainingWorkflowTests.class);

    @Inject
    private DeepLearning4JTrainingWorkflow dl4jTrainingWorkflow;

    @Inject
    private TensorFlowTrainingWorkflow tensorFlowTrainingWorkflow;

    @Inject
    private AzureML azureML;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Test
    public void testStartTrainingDl4J() throws Exception
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("script","dl4j");
        jsonObject.addProperty("mlPlatform","dl4j");
        String runId = this.dl4jTrainingWorkflow.startTraining(jsonObject);

        logger.info("*******");
        logger.info("RunId: "+runId);
        logger.info("*******");
        assertNotNull(runId);

        String runJson = this.mlFlowRunClient.getRun(runId);
        jsonObject = JsonParser.parseString(runJson).getAsJsonObject();
        String storedRunId = jsonObject.get("run").getAsJsonObject().get("info").getAsJsonObject().get("run_id").getAsString();
        logger.info(storedRunId);
        assertEquals(runId, storedRunId);
    }

    @Test
    public void testStartTrainingTensorFlow() throws Exception
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("script","tensorflow");
        jsonObject.addProperty("mlPlatform","tensorflow");
        String runId = this.tensorFlowTrainingWorkflow.startTraining(jsonObject);

        logger.info("*******");
        logger.info("RunId: "+runId);
        logger.info("*******");
        assertNotNull(runId);

        String runJson = this.mlFlowRunClient.getRun(runId);
        jsonObject = JsonParser.parseString(runJson).getAsJsonObject();
        String storedRunId = jsonObject.get("run").getAsJsonObject().get("info").getAsJsonObject().get("run_id").getAsString();
        logger.info(storedRunId);
        assertEquals(runId, storedRunId);
    }

    @Test
    public void testGetDataAzureML() throws Exception
    {
        String query = "{microsoft}";
        String response = this.azureML.getData(query);
        logger.info("****************");
        logger.info(response);
        logger.info("****************");

        JsonObject responseJson = JsonParser.parseString(response).getAsJsonObject();
        JsonObject entities = responseJson.get("entities").getAsJsonObject();
        JsonArray entityArray = entities.get("value").getAsJsonArray();
        logger.info(responseJson.toString());
    }
}
