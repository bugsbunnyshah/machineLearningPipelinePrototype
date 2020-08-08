package io.bugsbunny.dataScience.service;

import com.google.gson.JsonObject;
import io.bugsbunny.persistence.MongoDBJsonStore;
import io.bugsbunny.pipeline.ModelDeployer;
import io.bugsbunny.restClient.ElasticSearchClient;
import io.bugsbunny.restclient.MLFlowRunClient;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class TensorFlowTrainingWorkflow
{
    private static Logger logger = LoggerFactory.getLogger(TensorFlowTrainingWorkflow.class);

    @Inject
    private MongoDBJsonStore mongoDBJsonStore;

    @Inject
    private ElasticSearchClient elasticSearchClient;

    @Inject
    private MLFlowRunClient mlFlowRunClient;

    @Inject
    private ModelDeployer modelDeployer;

    private Map<Integer,String> eats = new HashMap<>();
    private Map<Integer,String> sounds = new HashMap<>();
    private Map<Integer,String> classifiers = new HashMap<>();

    //@Override
    public String startTraining(JsonObject trainingMetaData)
    {
        ByteArrayOutputStream modelStream = null;
        ObjectOutputStream out = null;
        try
        {
            //Train the said Model
            //this.modelDeployer.deployPythonTraining();

            //TODO: Delete this file once it is entered into the Repositories
            String model = IOUtils.toString(new FileInputStream("devModel/1/saved_model.pb"),
                    StandardCharsets.UTF_8);

            //Register the Trained Model with the DataBricks Repository
            String runId = this.mlFlowRunClient.createRun();
            modelStream = new ByteArrayOutputStream();
            out = new ObjectOutputStream(modelStream);
            out.writeObject(model);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("artifact_path", "model");
            jsonObject.addProperty("utc_time_created", "2020-06-26 18:00:56.056775");
            jsonObject.addProperty("run_id", runId);
            jsonObject.add("flavors", new JsonObject());
            jsonObject.addProperty("mlPlatform", trainingMetaData.get("mlPlatform").getAsString());
            jsonObject.addProperty("script", trainingMetaData.get("script").getAsString());
            jsonObject.addProperty("data", trainingMetaData.get("data").getAsString());
            //jsonObject.addProperty("modelSer", Base64.getEncoder().encodeToString(modelStream.toByteArray()));

            String json = jsonObject.toString();
            this.mlFlowRunClient.logModel(runId, json);

            //Register the Trained Model with the BugsBunny Repository
            this.mongoDBJsonStore.storeDevModels(jsonObject);

            return runId;
        }
        catch(Exception e)
        {
            logger.info(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        finally
        {
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException ioe)
                {
                    //Tried to cleanup..no biggie no biggie if still problemo time (lol)
                }
            }
            if(modelStream != null)
            {
                try
                {
                    modelStream.close();
                }
                catch(IOException ioe)
                {
                    //Tried to cleanup..no biggie no biggie if still problemo time (lol)
                }
            }
        }
    }

    public String getData(String runId)
    {
        JsonObject devModel = this.mongoDBJsonStore.getDevModel(runId);
        if(devModel != null && devModel.has("data"))
        {
            String data = devModel.get("data").getAsString();
            return data;
        }
        return null;
    }
}
