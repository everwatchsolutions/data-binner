/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class GeoTileBinner extends Binner {

    private static Logger log = LoggerFactory.getLogger(GeoTileBinner.class);

    public static int MAX_LEVEL_DEFAULT = 10;
    private int maxLevel = MAX_LEVEL_DEFAULT;
    private String latFieldName = "lat";
    private String lonFieldName = "lon";

    public GeoTileBinner(String countName) {
        super(countName);
    }

    public GeoTileBinner(String countName, int maxLevel) {
        this(countName, countName, maxLevel);
    }

    public GeoTileBinner(String countName, String dataFieldName) {
        super(countName, dataFieldName);
    }

    public GeoTileBinner(String countName, String dataFieldName, int maxLevel) {
        super(countName, dataFieldName);
        this.maxLevel = maxLevel;
    }

    /**
     *
     * @param countName
     * @param dataFieldName
     * @param maxLevel
     * @param latFieldName Used if the provided value is a Map. If not provided, defaults to "lat"
     * @param lonFieldName Used if the provided value is a Map. If not provided, defaults to "lon"
     */
    public GeoTileBinner(String countName, String dataFieldName, int maxLevel, String latFieldName, String lonFieldName) {
        super(countName, dataFieldName);
        this.maxLevel = maxLevel;
        this.latFieldName = latFieldName;
        this.lonFieldName = lonFieldName;
    }

    @Override
    protected List<String> generateBinNamesForData(Object value) {
        //we should support inbound data in the formats: List<Double> (lat, long)
        List<String> binNames = new ArrayList<>();

        Double lat = null;
        Double lon = null;
        
        Object latObj = null;
        Object lonObj = null;
        if (List.class.isAssignableFrom(value.getClass())) {
            List list = (List) value;
            if (list.size() == 2) {
                latObj = list.get(0);
                lonObj = list.get(1);

            } else {
                //why would there be less or more than two? got me! get out of here!
            }
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            //hopefully this means there is a lat and lon key
            Map map = (Map) value;
            latObj = map.get(latFieldName);
            lonObj = map.get(lonFieldName); 
        }

        //now we assume those two values are doubles...but are they?
        if (Double.class.isAssignableFrom(latObj.getClass()) && Double.class.isAssignableFrom(lonObj.getClass())) {
            lat = (Double) latObj;
            lon = (Double) lonObj;
        } else if (String.class.isAssignableFrom(latObj.getClass()) && String.class.isAssignableFrom(lonObj.getClass())) {
            //maybe they wrapped the double in a string?
            lat = Double.valueOf(latObj.toString());
            lon = Double.valueOf(lonObj.toString());
        } else {
            //ya...no clue...
        }

        if (lat != null && lon != null) {
            //Now that we have the lat/lon, we need to determin what tile coordinates that lat/lon falls in at each zoom level
            //TODO...
            for (int zoomLevel = 0; zoomLevel < maxLevel; zoomLevel++) {
                //calculate x and y at each zoom level
                binNames.add(getBinName() + "." + getTileNumber(lat, lon, zoomLevel));
            }
        }

        if (binNames.isEmpty()) {
            log.warn("Provided value for count [ " + getBinName() + " ] is not a valid format. Please provide an array/list of Double values in lat, lon order");
        }
        return binNames;
    }

    //from http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Java
    protected String getTileNumber(final double lat, final double lon, final int zoom) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
        if (xtile < 0) {
            xtile = 0;
        }
        if (xtile >= (1 << zoom)) {
            xtile = ((1 << zoom) - 1);
        }
        if (ytile < 0) {
            ytile = 0;
        }
        if (ytile >= (1 << zoom)) {
            ytile = ((1 << zoom) - 1);
        }
        return ("" + zoom + "-" + xtile + "-" + ytile);
    }
}
