package io.bugsbunny.aviation;

/*import io.delta.tables.DeltaTable;
import org.apache.commons.io.IOUtils;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StringType;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.*;

import com.github.opendevl.JFlat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

@QuarkusTest*/
public class CRUDAppTests
{
    /*private static Logger logger = LoggerFactory.getLogger(CRUDAppTests.class);

    @Test
    public void testQueries() throws Exception
    {
        try {
            SparkSession spark = SparkSession
                    .builder()
                    .master("local")
                    .appName("JavaWord2VecExample")
                    .getOrCreate();

            // $example on$
            // Input data: Each row is a bag of words from a sentence or document.
            List<Row> data = Arrays.asList(
                    RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
                    RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
                    RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
            );
            StructType schema = new StructType(new StructField[]{
                    new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
            });
            Dataset<Row> documentDF = spark.createDataFrame(data, schema);

            // Learn a mapping from words to Vectors.
            Word2Vec word2Vec = new Word2Vec()
                    .setInputCol("text")
                    .setOutputCol("result")
                    .setVectorSize(3)
                    .setMinCount(0);

            Word2VecModel model = word2Vec.fit(documentDF);
            Dataset<Row> result = model.transform(documentDF);

            for (Row row : result.collectAsList()) {
                List<String> text = row.getList(0);
                Vector vector = (Vector) row.get(1);
                System.out.println("Text: " + text + " => \nVector: " + vector + "\n");
            }
            // $example off$

            spark.stop();
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /*@Test
    public void testTableStreaming() throws Exception
    {
        String location = "/tmp/delta-table-"+ UUID.randomUUID().toString();

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("CRUDApp")
                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .getOrCreate();

        Dataset<Row> streamingDf = spark.readStream().format("rate").load();
        //streamingDf.show();
        StreamingQuery stream = streamingDf.selectExpr("value as id").writeStream().
                format("delta").
                option("checkpointLocation", "/tmp/checkpoint").start(location);

        spark.readStream().format("delta").
                load(location).writeStream().
                format("console").start();

        spark.close();
    }

    public static class Employee implements Serializable {
        public String name;
        public int salary;
    }*/
}
