package io.bugsbunny.dataIngestion.endpoint;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class DataMapperTests {
    private static Logger logger = LoggerFactory.getLogger(DataMapperTests.class);

    @Test
    public void testMap() throws Exception{
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("airlinesData.json"),
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
        //JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        //String statusCode = jsonObject.get("statusCode").getAsString();

        //assertEquals("1", statusCode);
    }
}