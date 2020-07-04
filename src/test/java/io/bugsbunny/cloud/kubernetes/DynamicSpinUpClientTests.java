package io.bugsbunny.cloud.kubernetes;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

public class DynamicSpinUpClientTests {

    //@Test
    public void testInvokeAlive() throws Exception
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokeKubeAlive();
    }

    //@Test
    public void testInvokeGetPods() throws Exception
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokeGetPods();
    }

    //@Test
    public void testInvokePostPod() throws Exception
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokePostPod();
    }

    @Test
    public void testInvokePostPodUsingClientLibrary() throws Exception
    {
        DynamicSpinUpClient dynamicSpinUpClient = new DynamicSpinUpClient();
        dynamicSpinUpClient.invokePostPodUsingClientLibrary();
    }
}
