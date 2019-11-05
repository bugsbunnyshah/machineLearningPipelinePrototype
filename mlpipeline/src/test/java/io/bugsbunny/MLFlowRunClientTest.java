package io.bugsbunny;

import io.bugsbunny.restClient.MLFlowRunClient;
import org.junit.Test;

public class MLFlowRunClientTest {

    @Test
    public void testCreateRuns()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.createRun();
    }
}
