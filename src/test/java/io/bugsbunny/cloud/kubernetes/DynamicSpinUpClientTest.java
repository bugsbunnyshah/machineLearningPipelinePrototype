package io.bugsbunny.cloud.kubernetes;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

public class DynamicSpinUpClientTest {

    @Test
    public void testInvokeAlive() throws Exception
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokeKubeAlive();
    }

    @Test
    public void testInvokeGetPods() throws Exception
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokeGetPods();
    }
}
