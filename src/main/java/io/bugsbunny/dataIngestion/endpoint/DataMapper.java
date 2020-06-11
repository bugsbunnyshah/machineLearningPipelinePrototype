package io.bugsbunny.dataIngestion.endpoint;


import com.google.gson.JsonObject;
import io.bugsbunny.dataIngestion.service.MapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("dataMapper")
public class DataMapper {
    private static Logger logger = LoggerFactory.getLogger(DataMapper.class);

    @Inject
    private MapperService mapperService;

    @Path("map")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response map(@RequestBody String sourceData)
    {
        try {
            JsonObject jsonObject = this.mapperService.map(sourceData, sourceData, sourceData);
            Response response = Response.ok(jsonObject.toString()).build();
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