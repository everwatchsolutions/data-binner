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
        String data = "{\"test\": \"test123\", \"myDate\": \"2012-04-23T18:25:43.511Z\",\"airSpeed\": 456.5,\"freq\":132.4, \"myInt\":1002, \"bigFreq\":455332.4, \"point\":[45.0, 45.0], \"pointObj\":{\"x\": 88.2, \"y\": -99.3},\"pointObj2\":{\"lat\": 88.2, \"lon\": -99.3}}";

        List<Binner> binners = Arrays.asList(
                new LiteralBinner("test"),
                new DateBinner("date", "myDate", DateGranularity.MSEC),
                new NumericBinner("freq", 10),
                new NumericBinner("airSpeed", 10),
                new NumericBinner("myInt", 10),
                new NumericBinner("bigFreq", 10),
                new GeoTileBinner("geo", "point", 17),
                new GeoTileBinner("geoObj", "pointObj", 10, "x", "y"),
                new GeoTileBinner("geoObj2", "pointObj2")
        );

        binners.stream().forEach((b) -> {
            List<String> bins = b.generateBinNames(data);
            for (String bin : bins) {
                log.info("Bin [ " + bin + " ]");
            }
        });

    }
}
