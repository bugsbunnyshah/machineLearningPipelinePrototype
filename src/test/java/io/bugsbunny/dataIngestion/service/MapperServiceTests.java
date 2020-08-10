package io.bugsbunny.dataIngestion.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONTokener;

@QuarkusTest
public class MapperServiceTests {
    private static Logger logger = LoggerFactory.getLogger(MapperServiceTests.class);

    @Inject
    private MapperService mapperService;

    @Test
    public void testMapAirlineData() throws Exception
    {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "airlinesData.json"),
                StandardCharsets.UTF_8);
        JsonObject jsonObject = this.mapperService.map(json, json, json);
        logger.info("*******");
        logger.info(jsonObject.toString());
        logger.info("*******");

        assertEquals("123456789", jsonObject.get("Id").getAsString());
        assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());
    }

    @Test
    public void testMapPeopleData() throws Exception
    {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "people.json"),
                StandardCharsets.UTF_8);
        JsonObject jsonObject = this.mapperService.map(json, json, json);
        logger.info("*******");
        logger.info(jsonObject.toString());
        logger.info("*******");

        assertEquals("James", jsonObject.get("firstname").getAsString());
        //assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        //assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());
    }

    @Test
    public void testMapSpaceData() throws Exception
    {
        //Case 4: CSV without header
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("empId");
        jsonArray.put("name");
        jsonArray.put("age");
        String csvData = "1, Mark, 22 \n" + "2, Robert, 35 \n" + "3, Julia, 18";
        logger.info(CDL.toJSONArray(jsonArray,csvData).toString());

        JSONArray array = CDL.toJSONArray(jsonArray,csvData);
        String json = array.get(0).toString();
        logger.info(json);

        JsonObject jsonObject = this.mapperService.map(json, json, json);
        logger.info("*******");
        logger.info(jsonObject.toString());
        logger.info("*******");
    }
}