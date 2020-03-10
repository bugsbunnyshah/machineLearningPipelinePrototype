package io.bugsbunny.cloud.kubernetes;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

public class DynamicSpinUpClientTest {

    @Test
    public void testInvokeAlive()
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokeKubeAlive();
    }

    @Test
    public void testInvokeGetPods()
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokeGetPods();
    }
}
