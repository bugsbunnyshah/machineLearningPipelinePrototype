package io.bugsbunny.dataLake;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delta.tables.DeltaTable;
import org.apache.spark.sql.*;
//import org.apache.spark.sql.streaming.StreamingQuery;

import java.util.HashMap;
import java.util.UUID;
import java.util.*;

import com.mongodb.spark.MongoSpark;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

import org.bson.Document;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.WriteConfig;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

import org.bson.Document;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

import java.io.Serializable;

import com.google.gson.*;

public class DeltaLakeStartedTests implements Serializable
{
    private static Logger logger = LoggerFactory.getLogger(DeltaLakeStartedTests.class);

    @Test
    public void testQueries() throws Exception
    {
        String location = "/tmp/delta-table-"+ UUID.randomUUID().toString();

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("CRUDApp")
                .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.myCollection")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.myCollection")
                .getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email","c.s@data.world");
        String json = jsonObject.toString();

        //Initialize the Data
        System.out.println("INIT_DATA");
        //Dataset<Long> data = spark.range(0, 5);
        //data.write().format("delta").save(location);
        //Dataset<Row> df = spark.read().format("delta").load(location);
        Dataset<Row> df = spark.read().text(Thread.currentThread().
                getContextClassLoader().
                getResource("dataLake/email.json").getFile());
        df.show();

        /*System.out.println("OVERWRITE_DATA");
        data = spark.range(5, 10);
        //location = "/tmp/delta-table-"+ UUID.randomUUID().toString();
        data.write().format("delta").mode("overwrite").save(location);
        df = spark.read().format("delta").load(location);
        df.show();

        DeltaTable deltaTable = DeltaTable.forPath(location);

        // Update every even value by adding 100 to it
        System.out.println("UPDATE_DATA");
        deltaTable.update(
                functions.expr("id % 2 == 0"),
                new HashMap<String, Column>() {{
                    put("id", functions.expr("id + 100"));
                }}
        );
        deltaTable.toDF().show();

        //Read in the old overwritten data set
        System.out.println("TIMETRAVEL_DATA_ORIGINAL");
        df = spark.read().format("delta").option("versionAsOf", 0).load(location);
        df.show();

        //Read in the updated data set
        System.out.println("TIMETRAVEL_DATA_UPDATED");
        df = spark.read().format("delta").option("versionAsOf", 1).load(location);
        df.show();

        //Read in the latest/live data set
        System.out.println("TIMETRAVEL_DATA_LATEST_LIVE");
        df = spark.read().format("delta").option("versionAsOf", 2).load(location);
        df.show();*/

        // Create a custom WriteConfig
        Map<String, String> writeOverrides = new HashMap<String, String>();
        writeOverrides.put("collection", "spark");
        writeOverrides.put("writeConcern.w", "majority");
        WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);

        /*Start Example: Save data from RDD to MongoDB*****************/
        MongoSpark.save(df, writeConfig);
        /*End Example**************************************************/

        spark.close();

        //String logFile = "README.md"; // Should be some file on your system
        //SparkSession spark = SparkSession
        //        .builder()
        //        .appName("Java Spark SQL basic example")
        //        .config("spark.master", "local")
        //        .getOrCreate();
        //Dataset<String> logData = spark.read().textFile(logFile).cache();
    }

    @Test
    public void testMongoDBConnector() throws Exception
    {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("MongoSparkConnectorIntro")
                .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.myCollection")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.myCollection")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        // Create a RDD of 10 documents
        JavaRDD<Document> sparkDocuments = jsc.parallelize(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).map
                (new Function<Integer, Document>() {
                    public Document call(final Integer i) throws Exception {
                        return Document.parse("{test: " + i + "}");
                    }
                });

        // Create a custom WriteConfig
        Map<String, String> writeOverrides = new HashMap<String, String>();
        writeOverrides.put("collection", "spark");
        writeOverrides.put("writeConcern.w", "majority");
        WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);

        /*Start Example: Save data from RDD to MongoDB*****************/
        MongoSpark.save(sparkDocuments, writeConfig);
        /*End Example**************************************************/

        jsc.close();
    }
}