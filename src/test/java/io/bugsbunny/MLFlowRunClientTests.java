package io.bugsbunny;

import io.bugsbunny.restClient.MLFlowRunClient;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MLFlowRunClientTests {

    @Test
    public void testCreateExperiment()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.createExperiment();
    }

    @Test
    public void testGetExperiments()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.getExperiments();
    }

    @Test
    public void testCreateRun()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.createRun();
    }
}
