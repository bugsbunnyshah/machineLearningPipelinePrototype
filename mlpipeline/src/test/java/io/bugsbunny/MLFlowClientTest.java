package io.bugsbunny;

import io.bugsbunny.restClient.MLFlowClient;
import org.junit.Test;

public class MLFlowClientTest {

    @Test
    public void testCreateExperiment()
    {
        MLFlowClient mlFlowClient = new MLFlowClient();
        mlFlowClient.createExperiment();
    }
}
