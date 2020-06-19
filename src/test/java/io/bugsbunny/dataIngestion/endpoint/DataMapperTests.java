package io.bugsbunny.dataIngestion.endpoint;

import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.XML;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class DataMapperTests {
    private static Logger logger = LoggerFactory.getLogger(DataMapperTests.class);

    @Test
    public void testMapWithOneToOneFields() throws Exception{
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("airlinesDataOneToOneFields.json"),
                StandardCharsets.UTF_8);
        logger.info(json);

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", json);
        input.addProperty("destinationSchema", json);
        input.addProperty("sourceData", json);


        Response response = given().body(input.toString()).when().post("/dataMapper/map")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        //assert the body
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertEquals("123456789", jsonObject.get("Id").getAsString());
        assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());
    }

    @Test
    public void testMapWithScatteredFields() throws Exception{
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("airlinesDataScatteredFields.json"),
                StandardCharsets.UTF_8);
        logger.info(json);

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", json);
        input.addProperty("destinationSchema", json);
        input.addProperty("sourceData", json);


        Response response = given().body(input.toString()).when().post("/dataMapper/map")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        //assert the body
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertEquals("123456789", jsonObject.get("Id").getAsString());
        assertEquals("1234567", jsonObject.get("Rcvr").getAsString());
        assertEquals(Boolean.TRUE, jsonObject.get("HasSig").getAsBoolean());
    }

   @Test
    public void testMapXmlSourceData() throws Exception
    {
        String xml = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("people.xml"),
                StandardCharsets.UTF_8);

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", xml);
        input.addProperty("destinationSchema", xml);
        input.addProperty("sourceData", xml);


        Response response = given().body(input.toString()).when().post("/dataMapper/mapXml")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");
    }
}