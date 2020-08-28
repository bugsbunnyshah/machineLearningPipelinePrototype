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

public class DeltaLakeStartedTests
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
                .getOrCreate();

        //Initialize the Data
        System.out.println("INIT_DATA");
        Dataset<Long> data = spark.range(0, 5);
        data.write().format("delta").save(location);
        Dataset<Row> df = spark.read().format("delta").load(location);
        df.show();

        System.out.println("OVERWRITE_DATA");
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
        df.show();

        spark.close();

        //String logFile = "README.md"; // Should be some file on your system
        //SparkSession spark = SparkSession
        //        .builder()
        //        .appName("Java Spark SQL basic example")
        //        .config("spark.master", "local")
        //        .getOrCreate();
        //Dataset<String> logData = spark.read().textFile(logFile).cache();
    }
}