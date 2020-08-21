package io.bugsbunny.aviation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class SampleDataTests {
    private static Logger logger = LoggerFactory.getLogger(SampleDataTests.class);

    @Test
    public void testFirst() throws Exception
    {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("airlinesData.json"),
                StandardCharsets.UTF_8);

        JsonArray array = JsonParser.parseString(json).getAsJsonArray();
        JsonObject jsonObject = array.get(0).getAsJsonObject();

        logger.info("*******");
        logger.info(jsonObject.get("Id").getAsString());
        logger.info(jsonObject.get("Rcvr").getAsString());
        logger.info(""+jsonObject.get("HasSig").getAsBoolean());
        logger.info("*******");
    }
}
