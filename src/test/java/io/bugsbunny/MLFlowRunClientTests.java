package io.bugsbunny;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.bugsbunny.restClient.MLFlowRunClient;

import io.quarkus.test.junit.QuarkusTest;
import net.minidev.json.JSONValue;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@QuarkusTest
public class MLFlowRunClientTests {
    private static Logger logger = LoggerFactory.getLogger(MLFlowRunClientTests.class);

    @Test
    public void testCreateExperiment()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.createExperiment();
    }

    @Test
    public void testGetExperiments()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.getExperiments();
    }

    @Test
    public void testCreateRun()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        mlFlowRunClient.createRun();
    }

    @Test
    public void testGetRun()
    {
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        String runId = "1b117ece479c47aca912feb75bc55b0a";
        mlFlowRunClient.getRun(runId);
    }

    @Test
    public void testLogModel() throws Exception
    {
        String yamlString = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("MLmodel")
                , StandardCharsets.UTF_8);
        MLFlowRunClient mlFlowRunClient = new MLFlowRunClient();
        Yaml yaml= new Yaml();
        Object obj = yaml.load(yamlString);

        String modelLocation = "tmp/model.ser";
        FileInputStream fileIn = new FileInputStream(modelLocation);
        String model = IOUtils.toString(fileIn, StandardCharsets.UTF_8);

        String json = JSONValue.toJSONString(obj);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        jsonObject.addProperty("modelSer", model);
        json = jsonObject.toString();
        logger.info(json);

        String runId = "1b117ece479c47aca912feb75bc55b0a";
        mlFlowRunClient.logModel(runId, json);
        mlFlowRunClient.getRun(runId);
    }
}
