package io.bugsbunny.dataIngestion;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class SparkGetStartedTests {
    private static Logger logger = LoggerFactory.getLogger(SparkGetStartedTests.class);

    @Test
    public void testFirstStart() throws Exception
    {
        String logFile = "README.md"; // Should be some file on your system
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.master", "local")
                .getOrCreate();
        Dataset<String> logData = spark.read().textFile(logFile).cache();

        long numAs = logData.filter((FilterFunction<String>) s -> s.contains("a")).count();
        long numBs = logData.filter((FilterFunction<String>) s -> s.contains("b")).count();

        logger.info("**********");
        logger.info("Lines with a: " + numAs + ", lines with b: " + numBs);
        logger.info("**********");

        spark.stop();
    }

    @Test
    public void testFirstRDD() throws Exception
    {
        String master = "local";
        SparkConf conf = new SparkConf().setAppName("testFirstRDD").setMaster(master);
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data);
    }
}
