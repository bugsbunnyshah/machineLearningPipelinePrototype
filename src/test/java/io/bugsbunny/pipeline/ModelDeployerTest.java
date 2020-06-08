package io.bugsbunny.pipeline;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.kamranzafar.jtar.TarEntry;
import org.kamranzafar.jtar.TarInputStream;
import org.kamranzafar.jtar.TarOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

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
