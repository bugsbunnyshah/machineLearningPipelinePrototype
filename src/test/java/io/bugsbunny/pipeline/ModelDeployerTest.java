package io.bugsbunny.pipeline;

import io.quarkus.test.junit.QuarkusTest;
import jdk.nashorn.internal.ir.FunctionNode;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.junit.jupiter.api.Test;
import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;
import org.kamranzafar.jtar.TarOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
        String tarFile = "/Users/babyboy/mamasboy/appgallabsForProfit/machineLearningPipelinePrototype/src/main/resources/code-with-quarkus-jvm.tar";
        String destFolder = "target/code-with-quarkus-jvm.docker";

        TarInputStream tis = new TarInputStream(new BufferedInputStream(new FileInputStream(tarFile)));
        TarEntry entry;
        while((entry = tis.getNextEntry()) != null)
        {
            int count;
            byte data[] = new byte[2048];
        }

        tis.close();
    }
}
