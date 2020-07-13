package io.bugsbunny.dataScience.endpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.bugsbunny.dataScience.service.TrainingWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("search")
public class DataSetSearch {
    private Logger logger = LoggerFactory.getLogger(DataSetSearch.class);

    @Inject
    private TrainingWorkflow trainingWorkflow;

    @Path("phrase")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@RequestBody String jsonBody) throws Exception
    {
        try {
            JsonObject phrase = JsonParser.parseString(jsonBody).getAsJsonObject();
            this.trainingWorkflow.generateLuceneIndex(phrase);

            logger.info("****");
            logger.info(phrase.toString());
            logger.info("****");

            JsonArray result = new JsonArray();
            return Response.ok(result.toString()).build();
        }
        catch(Exception e)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }
}
