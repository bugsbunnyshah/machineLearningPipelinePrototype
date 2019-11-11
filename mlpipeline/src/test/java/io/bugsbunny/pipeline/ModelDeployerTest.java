package io.bugsbunny.pipeline;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelDeployerTest {
    private static Logger logger = LoggerFactory.getLogger(ModelDeployerTest.class);

    @Test
    public void testDeploy()
    {
        ModelDeployer deployer = new ModelDeployer();
        int status = deployer.deploy();
        logger.info("****STATUS******");
        logger.info("STATUS: "+status);
        logger.info("****************");
    }
}
