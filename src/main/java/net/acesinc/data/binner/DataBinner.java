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
        String data = "{\"test\": \"test123\", \"myDate\": \"2012-04-23T18:25:43.511Z\",\"airSpeed\": 456.5,\"freq\":132.4, \"myInt\":1002, \"bigFreq\":455332.4, \"point\":[45.0, 45.0], \"pointObj\":{\"x\": 88.2, \"y\": -99.3},\"pointObj2\":{\"lat\": 88.2, \"lon\": -99.3}, \"car\": {\"make\": \"Subaru\",\"model\": \"Impreza WRX\"}}";

        Binner date = new DateBinner("date", "myDate", DateGranularity.MSEC);
        Binner freq = new NumericBinner("freq", 10);
        Binner test = new LiteralBinner("test");
        Binner missing = new GeoTileBinner("missing", "nope", 12);
        Binner geo = new GeoTileBinner("geo", "point", 17);
        
        List<Binner> binners = Arrays.asList(
//                test,
//                date,
//                freq,
//                new NumericBinner("airSpeed", 10),
//                new NumericBinner("myInt", 10),
//                new NumericBinner("bigFreq", 10),
//                geo,
                missing,
//                new GeoTileBinner("geoObj", "pointObj", 10, "x", "y"),
                new GeoTileBinner("geoObj2", "pointObj2")
        );

        String emptyData = "{}";
        binners.stream().forEach((b) -> {   
            List<String> bins = b.generateBinNames(emptyData);
            for (String bin : bins) {
                log.info("Bin [ " + bin + " ]");
            }
        });

        MergedBinner b = new MergedBinner(Arrays.asList(date,geo,freq, new NumericBinner("myInt", 10)));
        List<String> bins = b.generateBinNames(data);
        for (String bin : bins) {
            log.info("Bin [ " + bin + " ]");
        }
        MergedBinner b2 = new MergedBinner(Arrays.asList(date,freq));
        List<String> bins2 = b2.generateBinNames(data);
        for (String bin : bins2) {
            log.info("Bin [ " + bin + " ]");
        }
        
        Binner dateBinner = new DateBinner("date", "myDate", DateGranularity.MIN);
        Binner modelBinner = new LiteralBinner("car.model");

        Binner mergedBinner = new MergedBinner(Arrays.asList(dateBinner, modelBinner));

        bins = mergedBinner.generateBinNames(data);
        for (String bin : bins) {
            log.info("Bin [ " + bin + " ]");
        } 

    }
}
