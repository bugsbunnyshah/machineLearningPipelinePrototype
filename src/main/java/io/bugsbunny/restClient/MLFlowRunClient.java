package io.bugsbunny.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

@ApplicationScoped
public class MLFlowRunClient
{
    private Logger logger = LoggerFactory.getLogger(MLFlowRunClient.class);

    /**
     * Create a new run within an experiment.
     * A run is usually a single execution of a machine learning or data ETL pipeline.
     * MLflow uses runs to track Param, Metric, and RunTag associated with a single execution.
     */
    public void createExperiment()
    {
        //Setup RestTemplate
        HttpClient httpClient = HttpClient.newBuilder().build();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/experiments/create";

        //Setup POST request
        try {
            JsonObject json = new JsonObject();
            String experimentId = "0";
            long startTime = Instant.now().toEpochMilli();
            json.addProperty("name", "AppGal");
            java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
            java.net.http.HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                    //.header("Content-Type", "application/json")
                    //.header("api-key",primaryKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();


            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String tokenJson = httpResponse.body();
            int status = httpResponse.statusCode();

            logger.info("***RESPONSE***");
            logger.info("BODY: "+tokenJson);
            logger.info("STATUS: "+status);
            logger.info("**************");
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void getExperiments()
    {
        //Setup RestTemplate
        HttpClient httpClient = HttpClient.newBuilder().build();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/experiments/list";

        //Setup POST request
        try {
            java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
            java.net.http.HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                    //.header("Content-Type", "application/json")
                    //.header("api-key",primaryKey)
                    .GET()
                    .build();


            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String tokenJson = httpResponse.body();
            int status = httpResponse.statusCode();

            logger.info("***RESPONSE***");
            logger.info("BODY: "+tokenJson);
            logger.info("STATUS: "+status);
            logger.info("**************");
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getRun(String runId)
    {
        //Setup RestTemplate
        HttpClient httpClient = HttpClient.newBuilder().build();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/runs/get?run_id="+runId;

        //Setup POST request
        try {
            java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
            java.net.http.HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                    //.header("Content-Type", "application/json")
                    //.header("api-key",primaryKey)
                    .GET()
                    .build();


            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public String createRun()
    {
        String runId = null;

        //Setup RestTemplate
        HttpClient httpClient = HttpClient.newBuilder().build();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/runs/create";

        //Setup POST request
        try {
            JsonObject json = new JsonObject();
            String experimentId = "0";
            long startTime = Instant.now().toEpochMilli();
            json.addProperty("experiment_id", experimentId);
            json.addProperty("start_time", startTime);
            java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
            java.net.http.HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                    //.header("Content-Type", "application/json")
                    //.header("api-key",primaryKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();


            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            logger.info(httpResponse.body());
            JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject();
            runId = jsonObject.get("run").getAsJsonObject().get("info").getAsJsonObject().get("run_id").getAsString();

            return runId;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void logModel(String runId, String modelJson)
    {
        //Setup RestTemplate
        HttpClient httpClient = HttpClient.newBuilder().build();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/runs/log-model";

        //Setup POST request
        try {
            JsonObject json = new JsonObject();
            json.addProperty("run_id", runId);
            json.addProperty("model_json", modelJson);
            java.net.http.HttpRequest.Builder httpRequestBuilder = java.net.http.HttpRequest.newBuilder();
            java.net.http.HttpRequest httpRequest = httpRequestBuilder.uri(new URI(restUrl))
                    //.header("Content-Type", "application/json")
                    //.header("api-key",primaryKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();


            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String tokenJson = httpResponse.body();
            int status = httpResponse.statusCode();

            logger.info("***RESPONSE***");
            logger.info("BODY: "+tokenJson);
            logger.info("STATUS: "+status);
            logger.info("**************");
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
