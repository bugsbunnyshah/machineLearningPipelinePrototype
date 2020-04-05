package io.bugsbunny.restClient;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.UUID;

public class MLFlowExperimentClient
{
    private Logger logger = LoggerFactory.getLogger(MLFlowExperimentClient.class);

    //Takes an experiment name as a free form string
    //returns an internal experiment_id
    //the friendly name is then associated with this experiment_id
    public void createExperiment(String experimentName)
    {
        logger.info("***CREATE_EXPERIMENT*******");
        logger.info("***************************");

        //Setup RestTemplate
        String restUrl = "http://localhost:5000/api/2.0/mlflow/experiments/create";
        HttpClient httpClient = HttpClient.newBuilder().build();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", experimentName);
        String jsonBody = jsonObject.toString();
        //Setup POST request
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(restUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + httpResponse.statusCode());
            logger.info("JSON: " + httpResponse.body());
            logger.info("**************");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    //List all the experiments stored in the Tracking Server
    public void listExperiments()
    {
        logger.info("***LIST_EXPERIMENTS*******");
        logger.info("***************************");

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/experiments/list";
        try {
            //Setup the GET request
            URI restURI = new URI(restUrl);
            HttpMethod get = HttpMethod.GET;
            RequestEntity<Void> requestEntity = RequestEntity.get(restURI).build();
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            HttpStatus status = response.getStatusCode();

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + status.value());
            logger.info("HttpMessage: " + status.getReasonPhrase());
            logger.info("JSON: " + response.getBody());
            logger.info("**************");
        }
        catch (URISyntaxException uriSyntaxException)
        {
            throw new RuntimeException(uriSyntaxException);
        }
    }

    //Get metadata for an experiment. This method works on deleted experiments.
    public void getExperiment()
    {
        logger.info("***GET_AN_EXPERIMENT_METADATA*******");
        logger.info("***************************");

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = MessageFormat.
                format("http://127.0.0.1:5000/api/2.0/mlflow/experiments/get?experiment_id={0}",
                        "0");

        try {
            //Setup the GET request
            URI restURI = new URI(restUrl);
            HttpMethod get = HttpMethod.GET;
            RequestEntity<Void> requestEntity = RequestEntity.get(restURI)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            HttpStatus status = response.getStatusCode();

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + status.value());
            logger.info("HttpMessage: " + status.getReasonPhrase());
            logger.info("JSON: " + response.getBody());
            logger.info("**************");
        }
        catch (URISyntaxException uriSyntaxException)
        {
            throw new RuntimeException(uriSyntaxException);
        }
    }

    /**
     * Get metadata for an experiment.
     *
     * This endpoint will return deleted experiments, but prefers the active experiment if an active and deleted experiment share the same name. If multiple deleted experiments share the same name, the API will return one of them.
     *
     * Throws RESOURCE_DOES_NOT_EXIST if no experiment with the specified name exists.
     */
    public void getExperimentByName()
    {
        logger.info("***GET_AN_EXPERIMENT_METADATA_BY_NAME*******");
        logger.info("***************************");

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = MessageFormat.
                format("http://127.0.0.1:5000/api/2.0/mlflow/experiments/get-by-name?experiment_name={0}",
                        "bugsbunny");

        try {
            //Setup the GET request
            URI restURI = new URI(restUrl);
            HttpMethod get = HttpMethod.GET;
            RequestEntity<Void> requestEntity = RequestEntity.get(restURI)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            HttpStatus status = response.getStatusCode();

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + status.value());
            logger.info("HttpMessage: " + status.getReasonPhrase());
            logger.info("JSON: " + response.getBody());
            logger.info("**************");
        }
        catch (URISyntaxException uriSyntaxException)
        {
            throw new RuntimeException(uriSyntaxException);
        }
    }

    public void getRunById(String runId)
    {
        logger.info("***GET_AN_EXPERIMENT_METADATA_BY_NAME*******");
        logger.info("***************************");

        //Setup RestTemplate
        String restUrl = MessageFormat.
                format("http://127.0.0.1:5000/api/2.0/mlflow/runs/get?run_id={0}",
                        runId);
        HttpClient httpClient = HttpClient.newBuilder().build();
        try {
            //Setup the GET request
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(restUrl))
                    .GET()
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            logger.info("***RESPONSE***");
            logger.info("HttpStatus: " + httpResponse.statusCode());
            logger.info("JSON: " + httpResponse.body());
            logger.info("**************");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
