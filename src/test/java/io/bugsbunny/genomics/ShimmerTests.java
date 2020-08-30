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
        String geneticData = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "genomics/testref.fa.fai"),
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
            //char[] characters = line.toCharArray();

            if (line.charAt(0) == '\t')
            {
                fieldNo++;
            }

            //Process the Chromosome DataSet
            if (fieldNo == 0)
            {
                logger.info("****PROCESSING*****");
                /* populate chromosome with the current chromosome */
                chromosome = line.substring(0, 1000);
                chromosomePointer = 1001;
                String restOfTheChromosome = null;
                while (line.charAt(chromosomePointer) != '\t')
                {
                    restOfTheChromosome = line.substring(chromosomePointer);
                    chromosomePointer++;
                    if(chromosomePointer == line.length())
                    {
                        break;
                    }
                }
                chromosome = restOfTheChromosome.toString();
                logger.info("***CHROMOSOME****");
                logger.info(chromosome.length()+"");
                System.out.println(chromosome);

                //Progress to the next set
                fieldNo++;
            }

            if(chromosomePointer == line.length())
            {
                continue;
            }

            //Process the POS DataSet
            if (fieldNo == 1)
            {
                /* populate position with current position */
                int tabIndex = line.indexOf(chromosomePointer);

                if(tabIndex == -1)
                {
                    String restOfTheChromosome = line.substring(chromosomePointer);
                    logger.info("***POS_STRING****");
                    logger.info(restOfTheChromosome.length()+"");
                    logger.info(restOfTheChromosome);
                }
                else
                {
                    String restOfTheChromosome = line.substring(chromosomePointer, tabIndex);
                    logger.info("***POS_STRING****");
                    logger.info(restOfTheChromosome.length()+"");
                    logger.info(restOfTheChromosome);
                }

                /*while (line.charAt(chromosomePointer) != '\t')
                {
                    restOfTheChromosome += line.substring(chromosomePointer);
                    chromosomePointer++;
                    if(chromosomePointer == line.length())
                    {
                        break;
                    }
                }*/
                //Progress to the next set
                fieldNo++;
            }
        }
    }
}