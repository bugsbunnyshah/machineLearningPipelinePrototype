package io.bugsbunny.aviation;

import io.delta.tables.DeltaTable;
import org.apache.commons.io.IOUtils;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import com.github.opendevl.JFlat;

public class CRUDAppTests
{
    private static Logger logger = LoggerFactory.getLogger(CRUDAppTests.class);

    @Test
    public void testQueries() throws Exception
    {
        try {
            String location = "hdfs://localhost:9000/hdfs-table-"+ UUID.randomUUID().toString();

            System.setProperty("spark.delta.logStore.class","org.apache.spark.sql.delta.storage.HDFSLogStore");

            // configure spark
            SparkSession spark = SparkSession
                    .builder()
                    .appName("Read JSON File to DataSet")
                    .master("local[2]")
                    .getOrCreate();


            String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("airlinesData.json"),
                    StandardCharsets.UTF_8);
            JFlat flatMe = new JFlat(json);

            //get the 2D representation of JSON document
            flatMe.json2Sheet().headerSeparator("_").getJsonAsSheet();

            //write the 2D representation in csv format
            flatMe.write2csv("blah.csv");

            final Dataset<Row> csv = spark.read().csv("blah.csv");
            //csv.show();

            csv.write().format("delta").save(location);

            DeltaTable deltaTable = DeltaTable.forPath(location);
            //deltaTable.toDF().show();

            Dataset<Row> df = spark.read().format("delta").option("versionAsOf", 0).load(location);
            df.show();

            Dataset<Row> result = df.select("_c0");
            result.show();

            // Java Bean (data class) used to apply schema to JSON data
            /*Encoder<JSONtoDataSet.Employee> employeeEncoder = Encoders.bean(JSONtoDataSet.Employee.class);

            String path = Thread.currentThread().getContextClassLoader().getResource("data/employee.json").getPath();

            // read JSON file to Dataset
            Dataset<JSONtoDataSet.Employee> ds = spark.read().json(path).as(employeeEncoder);
            ds.show();
            /*String location = "/tmp/delta-table-"+ UUID.randomUUID().toString();*/

            /*SparkSession spark = SparkSession.builder()
                    .master("local")
                    .appName("CRUDApp")
                    .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
                    .getOrCreate();

            Encoder<Airline> encoder = Encoders.bean(Airline.class);

            StructType schema = new StructType();
            schema.add("id", "int");
            String path = Thread.currentThread().getContextClassLoader().getResource("airlinesData.json").getPath();
            System.out.println(path);
            final Dataset<Airline> jsonDataSet = spark.read().schema(schema).json(
                    path
            ).as(encoder);
            jsonDataSet.show();*/


                    //.json(Thread.currentThread().
                    //getContextClassLoader().
                    //getResource("airlinesData.json").getFile());
            //jsonDataSet.show();
            //spark.sparkContext().read

            /*System.out.println("INIT_DATA");
            Dataset<Long> data = spark.range(0, 5);
            data.write().format("delta").save(location);
            Dataset<Row> df = spark.read().format("delta").load(location);
            df.show();

            //StreamingQuery stream = spark.readStream().format("delta").
            //        load(location).writeStream().
            //        format("console").start();
            //System.out.println(stream.toString());

            System.out.println("OVERWRITE_DATA");
            data = spark.range(5, 10);
            location = "/tmp/delta-table-"+ UUID.randomUUID().toString();
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


            System.out.println("TIMETRAVEL_DATA");
            df = spark.read().format("delta").option("versionAsOf", 0).load(location);
            df.show();

            System.out.println("TIMETRAVEL_DATA");
            df = spark.read().format("delta").option("versionAsOf", 1).load(location);
            df.show();*/

            spark.close();
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
    }*/

    public static class Employee implements Serializable {
        public String name;
        public int salary;
    }
}
