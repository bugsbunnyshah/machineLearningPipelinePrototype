package io.bugsbunny.dataScience.endpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

@QuarkusTest
public class TrainModelTests {
    private Logger logger = LoggerFactory.getLogger(TrainModelTests.class);

    //@Test
    public void testTrain() throws Exception
    {
        String script = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("tensorflow/trainModel.py"),
                StandardCharsets.UTF_8);

        String data = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("tensorflow/data.txt"),
                StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("script",script);
        jsonObject.addProperty("mlPlatform","tensorflow");
        jsonObject.addProperty("data",data);
        Response response = given().body(jsonObject.toString()).when().post("/trainModel/train")
                .andReturn();
        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        //assert the response
        jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assertEquals("tensorflow", jsonObject.get("mlPlatform").getAsString());

        JsonObject result = JsonParser.parseString(jsonResponse).getAsJsonObject();
        String runId = result.get("runId").getAsString();

        response = given().get("/trainModel/data/"+runId)
                .andReturn();
        String dataResponse = response.getBody().asString();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(dataResponse);
        logger.info("****");

        //assert the response
        assertEquals("Propitiation also to the King", dataResponse);
    }
}
