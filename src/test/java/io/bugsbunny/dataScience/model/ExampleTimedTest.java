package io.bugsbunny.dataScience.model;

import com.clarkware.junitperf.*;
import junit.framework.Test;

public class ExampleTimedTest {

    public static Test suite() {

        long maxElapsedTime = 1000;

        Test testCase = new ExampleTestCase("testOneSecondResponse");
        Test timedTest = new TimedTest(testCase, maxElapsedTime);

        return timedTest;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}