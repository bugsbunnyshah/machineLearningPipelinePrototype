package io.bugsbunny.dataScience.model;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Base64;

public class PortableAIModel
{
    private MultiLayerNetwork model;

    private Logger logger = LoggerFactory.getLogger(PortableAIModel.class);

    public void load(String encodedModelString)
    {
        ObjectInputStream in = null;
        try
        {
            in = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(encodedModelString)));
            this.model = (MultiLayerNetwork) in.readObject();
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        finally
        {
            if(in != null) {
                try{
                    in.close();
                }
                catch(IOException ioe){

                }
            }
        }
    }

    public void unload()
    {

    }

    public double calculate()
    {
        return this.model.calcL1(true);
    }
}
