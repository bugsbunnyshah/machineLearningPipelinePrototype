package io.bugsbunny.dataScience.service;


import com.google.gson.*;

import io.bugsbunny.restclient.MLFlowRunClient;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jep.Jep;


@QuarkusTest
public class AIAgnosticTrainingWorkflowTests
{
    private static Logger logger = LoggerFactory.getLogger(AIAgnosticTrainingWorkflowTests.class);

    @Inject
    private DeepLearning4JTrainingWorkflow trainingWorkflow;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Test
    public void testStartTraining() throws Exception
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("script","dl4j");
        jsonObject.addProperty("mlPlatform","dl4j");
        String runId = this.trainingWorkflow.startTraining(jsonObject);

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
}
