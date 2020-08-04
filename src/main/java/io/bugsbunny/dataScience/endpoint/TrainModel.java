package io.bugsbunny.dataScience.endpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.bugsbunny.dataScience.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

@Path("trainModel")
public class TrainModel {
    private Logger logger = LoggerFactory.getLogger(LiveModel.class);

    @Inject
    private TensorFlowTrainingWorkflow tensorFlowTrainingWorkflow;

    @Path("train")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response train(@RequestBody String trainingInput) throws Exception
    {
        try {
            JsonObject inputJson = JsonParser.parseString(trainingInput).getAsJsonObject();
            tensorFlowTrainingWorkflow.startTraining(inputJson);

            JsonObject result = new JsonObject();
            result.addProperty("mlPlatform", inputJson.get("mlPlatform").getAsString());
            result.addProperty("success", 200);
            result.add("results", result);
            return Response.ok(result).build();
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }

}