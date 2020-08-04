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

@QuarkusTest
public class TrainModelTests {
    private Logger logger = LoggerFactory.getLogger(TrainModelTests.class);

    @Test
    public void testTrain() throws Exception
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("script","learningTime()cause_I_am_a_lazy_python");
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
