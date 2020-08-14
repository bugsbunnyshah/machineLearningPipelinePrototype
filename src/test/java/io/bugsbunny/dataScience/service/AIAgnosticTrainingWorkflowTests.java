package io.bugsbunny.dataScience.service;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private TensorFlowTrainingWorkflow tensorFlowTrainingWorkflow;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    //@Test
    public void testStartTraining() throws Exception
    {
        String runId = this.tensorFlowTrainingWorkflow.startTraining(null);

        logger.info("*******");
        logger.info("RunId: "+runId);
        logger.info("*******");
        assertNotNull(runId);

        String runJson = this.mlFlowRunClient.getRun(runId);
        JsonObject jsonObject = JsonParser.parseString(runJson).getAsJsonObject();
        String storedRunId = jsonObject.get("run").getAsJsonObject().get("info").getAsJsonObject().get("run_id").getAsString();
        logger.info(storedRunId);
        assertEquals(runId, storedRunId);
    }

    //@Test
    public void testExecuteScript() throws Exception
    {
        int exitCode = this.tensorFlowTrainingWorkflow.executeScript("print('Hello,World')");
        logger.info("*******");
        logger.info("ExitCode: "+exitCode);
        logger.info("*******");
    }

    /*@Test
    public void testJep() throws Exception
    {
        String script = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("helloworld.py"),
                StandardCharsets.UTF_8);

        Jep jep = new Jep();
        jep.runScript(script);
    }*/
}
