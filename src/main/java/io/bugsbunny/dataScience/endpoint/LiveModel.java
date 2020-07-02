package io.bugsbunny.dataScience.endpoint;

import com.google.gson.JsonObject;
import io.bugsbunny.dataScience.service.TrainingWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("liveModel")
public class LiveModel {
    private Logger logger = LoggerFactory.getLogger(LiveModel.class);

    @Inject
    private TrainingWorkflow trainingWorkflow;

    @Path("calculate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculate() throws Exception
    {
        try {
            this.trainingWorkflow.processLiveModelRequest(new JsonObject());
            return Response.ok(0.0).build();
        }
        catch(Exception e)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }

}
