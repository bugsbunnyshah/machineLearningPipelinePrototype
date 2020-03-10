package io.bugsbunny.pipeline;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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

    @Test
    public void testReadTarFile() throws Exception
    {
        TarArchiveInputStream tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(
                new FileInputStream("/Users/babyboy/mamasboy/appgallabsForProfit/machineLearningPipelinePrototype/src/main/resources/code-with-quarkus-jvm.tar.gz")));
        TarArchiveEntry currentEntry = tarInput.getNextTarEntry();
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        while (currentEntry != null) {
            br = new BufferedReader(new InputStreamReader(tarInput)); // Read directly from tarInput
            System.out.println("For File = " + currentEntry.getName());
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("line="+line);
            }
            currentEntry = tarInput.getNextTarEntry(); // You forgot to iterate to the next file
        }
    }
}
