package io.bugsbunny.dataIngestion.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

import io.restassured.response.Response;
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
        String sourceData = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "airlinesData.json"),
                StandardCharsets.UTF_8);
        JsonArray jsonArray = JsonParser.parseString(sourceData).getAsJsonArray();
        JsonObject jsonObject = this.mapperService.map("", "", jsonArray);
        logger.info("***FUCK_SATAN****");
        logger.info(jsonObject.toString());
        logger.info("***FUCK_SATAN****");

        /*assertEquals("123456789", jsonObject.get("Id").getAsString());
        assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());*/
    }

    /*@Test
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
        try
        {
            String spaceData = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    "dataMapper/index3.csv"),
                    StandardCharsets.UTF_8);

            String[] lines = spaceData.split("\n");
            String header = lines[0];
            String[] columns = header.split(",");
            JsonArray array = new JsonArray();
            int length = lines.length;
            for(int i=1; i<length; i++)
            {
                String line = lines[i];
                String[] data = line.split(",");
                JsonObject jsonObject = new JsonObject();
                for(int j=0; j<data.length; j++)
                {
                    jsonObject.addProperty(columns[j],data[j]);
                }
                array.add(jsonObject);
            }
            logger.info(array.toString());

            //JSONArray jsonArray = new JSONArray();
            //logger.info(CDL.toJSONArray(jsonArray, spaceData).toString());

            JSONArray array = CDL.toJSONArray(new JSONArray(), spaceData);
            System.out.println(array);
            String json = array.get(0).toString();
            logger.info(json);

            JsonObject jsonObject = this.mapperService.map(json, json, json);
            logger.info("*******");
            logger.info(jsonObject.toString());
            logger.info("*******");
        }
        catch(Exception e)
        {
            //logger.error(e.getMessage(),e);
            //throw new RuntimeException(e);
        }
    }*/
}