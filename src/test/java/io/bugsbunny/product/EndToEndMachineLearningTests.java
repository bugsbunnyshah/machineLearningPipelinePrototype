package io.bugsbunny.product;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

@QuarkusTest
public class EndToEndMachineLearningTests
{
    private static Logger logger = LoggerFactory.getLogger(EndToEndMachineLearningTests.class);

    @Test
    public void testIngestionTrainingAndProdPush() throws Exception
    {
        logger.info("*******");
        logger.info("TEST_INGESTION");
        logger.info("*******");
    }
}