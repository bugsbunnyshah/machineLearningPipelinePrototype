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

import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

@QuarkusTest
public class TrainModelTests {
    private Logger logger = LoggerFactory.getLogger(TrainModelTests.class);

    @Test
    public void testTrain() throws Exception
    {
        String script = IOUtils.toString(Thread.currentThread().getContextClassLoader().
                        getResourceAsStream("tensorflow/trainModel.py"),
                StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("script",script);
        jsonObject.addProperty("mlPlatform","tensorflow");
        Response response = given().body(jsonObject.toString()).when().post("/trainModel/train")
                .andReturn();
        String jsonResponse = response.getBody().prettyPrint();
        logger.info("****");
        logger.info(response.getStatusLine());
        logger.info(jsonResponse);
        logger.info("****");

        //JsonObject result = JsonParser.parseString(jsonResponse).getAsJsonObject();
        //assertNotNull(result.get("results"));
    }
}
