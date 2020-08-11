package io.bugsbunny.genomics;

import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ShimmerTests
{
    private static Logger logger = LoggerFactory.getLogger(ShimmerTests.class);

    @Test
    public void testShimmer() throws Exception
    {
        /*int i,j,k;
        int baseLength;
        long chunkSize;
        long position;
        long lastPosition;
        int totalBases;
        char charPtr;
        char[] cmd = new char[1000000];
        char mapQual, baseQual;
        int baseQualNo;
        char nextChar;
        char refBase;
        int[] noBases = new int[10];
        int indelLength;
        int posLength;
        int refID;
        int sampleShift;
        char[] posBases = new char[3000000];
        char[] posString = new char[3000000];
        String baseArray = "ATGCN";
        int maxBases;
        int maxBase;
        int nextMaxBases;
        int nextMaxBase;
        int baseCount;*/

        String geneticData = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "genomics/runoutput"),
                StandardCharsets.UTF_8);

        String[] lines = geneticData.split("\n");
        for(String line:lines)
        {
            if(line.length() < 1000)
            {
                continue;
            }

            int fieldNo = 0;
            String chromosome;
            String posString;

            int chromosomePointer = 0;
            char[] characters = line.toCharArray();

            if (characters[0] == '\t')
            {
                fieldNo++;
            }

            if (fieldNo == 0)
            {
                //logger.info("****PROCESSING*****");
                /* populate chromosome with the current chromosome */
                chromosome = new String(characters, 0, 1000);
                chromosomePointer = 1001;
                StringBuilder restOfTheChromosome = new StringBuilder();
                while (characters[chromosomePointer] != '\t')
                {
                    restOfTheChromosome.append(characters[chromosomePointer]);
                    chromosomePointer++;
                    if(chromosomePointer == characters.length)
                    {
                        break;
                    }
                }
                chromosome = chromosome + restOfTheChromosome.toString();
                //logger.info("***CHROMOSOME****");
                //logger.info(chromosome);

                fieldNo++;
            }

            if(chromosomePointer == characters.length)
            {
                continue;
            }

            if (fieldNo == 1)
            {
                /* populate position with current position */
                StringBuilder buffer = new StringBuilder();
                while (characters[chromosomePointer] != '\t')
                {
                    buffer.append(characters[chromosomePointer]);
                    chromosomePointer++;
                    if(chromosomePointer == characters.length)
                    {
                        break;
                    }
                }
                fieldNo = 2;
                posString = buffer.toString();
                logger.info("***POS_STRING****");
                logger.info(posString);
            }
        }
    }
}