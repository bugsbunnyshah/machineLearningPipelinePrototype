package io.bugsbunny.pipeline;

import io.quarkus.test.junit.QuarkusTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.Test;

import java.io.*;

//@QuarkusTest
public class ModelDeployerTests {
    private static Logger logger = LoggerFactory.getLogger(ModelDeployerTests.class);

    //@Test
    public void testDeploy()
    {
        ModelDeployer deployer = new ModelDeployer();
        int status = deployer.deploy();
        logger.info("****STATUS******");
        logger.info("STATUS: "+status);
        logger.info("****************");
    }

    //@Test
    public void testDeployPythonTraining()
    {
        ModelDeployer deployer = new ModelDeployer();
        int status = deployer.deployPythonTraining();
        logger.info("****STATUS******");
        logger.info("STATUS: "+status);
        logger.info("****************");
    }

    //@Test
    public void testReadTarFile() throws Exception
    {
        String tarFile = "/Users/babyboy/mamasboy/appgallabsForProfit/machineLearningPipelinePrototype/src/main/resources/code-with-quarkus-jvm.tar";
        String destFolder = "target/code-with-quarkus-jvm.docker";
        File file = new File(tarFile);
        int fileLength = (int)file.length();

        /*try (TarArchiveInputStream fin = new TarArchiveInputStream(new FileInputStream(file))){
            System.out.println("BLAHBLAH");
            TarArchiveEntry entry = fin.getCurrentEntry();
            TarArchiveEntry nextEntry = null;
            do {
                if(entry != null) {
                    System.out.println("Current File: " + entry.getName());
                    entry = null;
                }
                nextEntry = fin.getNextTarEntry();
                if(nextEntry != null) {
                    System.out.println("Next File: " + nextEntry.getName());
                }
            }while(nextEntry != null);
        }*/
    }
}
