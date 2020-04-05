package io.bugsbunny;

import io.bugsbunny.restClient.MLFlowExperimentClient;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class MLFlowExperimentClientTest {

    @Test
    public void testCreateExperiment()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.createExperiment("appgallabs_"+UUID.randomUUID().toString());
    }

    @Test
    public void testListExperiments()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.listExperiments();
    }

    //@Test
    public void testGetExperiment()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.getExperiment();
    }

    //@Test
    public void testGetExperimentByName()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.getExperimentByName();
    }

    //@Test
    public void testGetRunById()
    {
        MLFlowExperimentClient mlFlowExperimentClient = new MLFlowExperimentClient();
        mlFlowExperimentClient.getRunById("7f6e4ab04eb54ffca3f589f5ef120fae");
    }
}
