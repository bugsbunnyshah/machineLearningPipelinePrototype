package io.bugsbunny.restClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

public class MLFlowRunClient
{
    private Logger logger = LoggerFactory.getLogger(MLFlowRunClient.class);

    /**
     * Create a new run within an experiment.
     * A run is usually a single execution of a machine learning or data ETL pipeline.
     * MLflow uses runs to track Param, Metric, and RunTag associated with a single execution.
     */
    public void createRun()
    {
        logger.info("***CREATE_RUN*******");
        logger.info("***************************");

        //Setup RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String restUrl = "http://127.0.0.1:5000/api/2.0/mlflow/runs/create";
        ObjectMapper objectMapper = new ObjectMapper();

        //Setup POST request
        try {
            URI restURI = new URI(restUrl);
            HttpMethod post = HttpMethod.POST;

            //Input Values
            String experimentId = "0";
            long startTime = Instant.now().toEpochMilli();

            //Setup the Input Payload
            ObjectNode inputPayload = objectMapper.createObjectNode();
            inputPayload.put("experiment_id", experimentId);
            inputPayload.put("start_time", startTime);
            String body = inputPayload.toString();
            logger.info("***INPUT_JSON***");
            logger.info("json: " + body);
            logger.info("**************");

            RequestEntity<String> requestEntity = RequestEntity.post(restURI).accept(MediaType.APPLICATION_JSON).body(body);
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
}
