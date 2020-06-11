package io.bugsbunny.dataIngestion.endpoint;


import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("dataMapper")
public class DataMapper {
    private static Logger logger = LoggerFactory.getLogger(DataMapper.class);

    @Path("map")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response map()
    {
        try {
            Response response = Response.ok().build();
            return response;
        }
        catch(Exception e)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }
}