/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class DataBinner {

    private static Logger log = LoggerFactory.getLogger(DataBinner.class);

    public static void main(String[] args) {
        String data = "{\"test\": \"test123\", \"myDate\": \"2012-04-23T18:25:43.511Z\",\"freq\":132.4}";

        LiteralBinner binner = new LiteralBinner("test");
        DateBinner dateBinner = new DateBinner("date", "myDate", DateGranularity.MSEC);
        NumericBinner numBinner = new NumericBinner("freq", 10);

        List<Binner> binners = Arrays.asList(binner, dateBinner, numBinner);

        binners.stream().forEach((b) -> {
            List<String> bins = b.generateBinNames(data);
            for (String bin : bins) {
                log.info("Bin [ " + bin + " ]");
            }
        });

    }
}
