package io.bugsbunny;

import io.bugsbunny.restClient.MLFlowExperimentClient;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

public class MLFlowExperimentClientTest {

    @Test
    public void testCreateExperiment()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.createExperiment();
    }

    @Test
    public void testListExperiments()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.listExperiments();
    }

    @Test
    public void testGetExperiment()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.getExperiment();
    }

    @Test
    public void testGetExperimentByName()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.getExperimentByName();
    }
}