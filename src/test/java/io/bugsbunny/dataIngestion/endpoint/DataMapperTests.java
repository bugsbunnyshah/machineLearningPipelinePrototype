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
    public void testMapXmlSourceData()
    {
        String xml = "<persons>\n" +
                "    <person id=\"1\">\n" +
                "        <firstname>James</firstname>\n" +
                "        <lastname>Smith</lastname>\n" +
                "        <middlename></middlename>\n" +
                "        <dob_year>1980</dob_year>\n" +
                "        <dob_month>1</dob_month>\n" +
                "        <gender>M</gender>\n" +
                "        <salary currency=\"Euro\">10000</salary>\n" +
                "        <addresses>\n" +
                "            <address>\n" +
                "                <street>123 ABC street</street>\n" +
                "                <city>NewJersy</city>\n" +
                "                <state>NJ</state>\n" +
                "            </address>\n" +
                "            <address>\n" +
                "                <street>456 apple street</street>\n" +
                "                <city>newark</city>\n" +
                "                <state>DE</state>\n" +
                "            </address>\n" +
                "        </addresses>\n" +
                "    </person>\n" +
                "    <person id=\"2\">\n" +
                "        <firstname>Michael</firstname>\n" +
                "        <lastname></lastname>\n" +
                "        <middlename>Rose</middlename>\n" +
                "        <dob_year>1990</dob_year>\n" +
                "        <dob_month>6</dob_month>\n" +
                "        <gender>M</gender>\n" +
                "        <salary currency=\"Dollor\">10000</salary>\n" +
                "        <addresses>\n" +
                "            <address>\n" +
                "                <street>4512 main st</street>\n" +
                "                <city>new york</city>\n" +
                "                <state>NY</state>\n" +
                "            </address>\n" +
                "            <address>\n" +
                "                <street>4367 orange st</street>\n" +
                "                <city>sandiago</city>\n" +
                "                <state>CA</state>\n" +
                "            </address>\n" +
                "        </addresses>\n" +
                "    </person>\n" +
                "</persons>";

        JsonObject input = new JsonObject();
        input.addProperty("sourceSchema", xml);
        input.addProperty("destinationSchema", xml);
        input.addProperty("sourceData", xml);


        Response response = given().body(input.toString()).when().post("/dataMapper/map")
                .andReturn();

        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");
    }
}