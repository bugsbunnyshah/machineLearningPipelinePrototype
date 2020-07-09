package io.bugsbunny.dataScience.service;

import com.google.gson.JsonObject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//import io.bugsbunny.restclient.MLFlowRunClient;

@QuarkusTest
public class TrainingWorkflowTests {
    /*private static Logger logger = LoggerFactory.getLogger(TrainingWorkflowTests.class);

    @Inject
    private TrainingWorkflow trainingWorkflow;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Test
    public void testStartTraining() throws Exception
    {
        String runId = this.trainingWorkflow.startTraining();

        logger.info("*******");
        logger.info("RunId: "+runId);
        logger.info("*******");
        assertNotNull(runId);

        String runJson = this.mlFlowRunClient.getRun(runId);
        logger.info(runJson);
    }

    @Test
    public void testProcessLiveModelRequest() throws Exception
    {
        Double result = this.trainingWorkflow.processLiveModelRequest(new JsonObject());

        logger.info("*******");
        logger.info(result.toString());
        logger.info("*******");
    }*/
}
