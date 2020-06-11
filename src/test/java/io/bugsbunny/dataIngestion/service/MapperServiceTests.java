package io.bugsbunny.dataIngestion.service;

import com.google.gson.JsonObject;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class MapperServiceTests {
    private static Logger logger = LoggerFactory.getLogger(MapperServiceTests.class);

    @Inject
    private MapperService mapperService;

    @Test
    public void testMap() throws Exception
    {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("airlinesData.json"),
                StandardCharsets.UTF_8);
        JsonObject result = this.mapperService.map(json, json, json);
        logger.info("*******");
        logger.info(result.toString());
        logger.info("*******");
    }
}