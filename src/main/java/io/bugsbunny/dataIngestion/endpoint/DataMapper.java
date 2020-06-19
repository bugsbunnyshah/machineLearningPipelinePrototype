package io.bugsbunny.dataIngestion.endpoint;

import io.bugsbunny.dataIngestion.service.MapperService;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.json.XML;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

@Path("dataMapper")
public class DataMapper {
    private static Logger logger = LoggerFactory.getLogger(DataMapper.class);

    @Inject
    private MapperService mapperService;

    @Path("map")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response map(@RequestBody String input)
    {
        try {
            JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();

            String sourceSchema = jsonObject.get("sourceSchema").getAsString();
            String destinationSchema = jsonObject.get("destinationSchema").getAsString();
            String sourceData = jsonObject.get("sourceData").getAsString();

            JsonObject result = this.mapperService.map(sourceSchema, destinationSchema, sourceData);
            Response response = Response.ok(result.toString()).build();
            return response;
        }
        catch(Exception e)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", e.getMessage());
            return Response.status(500).entity(jsonObject.toString()).build();
        }
    }

    @Path("mapXml")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response mapXmlSourceData(@RequestBody String input)
    {
        try {
            JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();

            String sourceData = jsonObject.get("sourceData").getAsString();
            JSONObject sourceJson = XML.toJSONObject(sourceData);
            String json = sourceJson.toString(4);
            logger.info(json);

            JsonObject result = this.mapperService.map(json, json, json);
            Response response = Response.ok(result.toString()).build();
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