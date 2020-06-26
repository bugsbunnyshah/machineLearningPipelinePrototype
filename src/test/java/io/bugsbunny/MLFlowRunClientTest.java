package io.bugsbunny;

import io.bugsbunny.restClient.MLFlowRunClient;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MLFlowRunClientTest {

    @Test
    public void testCreateRuns()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.createRun();
    }
}
