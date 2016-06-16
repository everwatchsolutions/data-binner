/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner.extractor;

import java.util.Map;

/**
 *
 * @author andrewserff
 */
public class DataExtractorFactory {
    public static DataExtractor getDataExtractor(Object data) throws Exception {
        DataExtractor ext = null;
        if (String.class.isAssignableFrom(data.getClass())) {
            String s = (String)data;
            if (s.startsWith("{")) {
                //it's a json object. 
                ext = new JsonDataExtractor(data);
            } else if (s.startsWith("<")) {
                //it's xml
            }
        } if (Map.class.isAssignableFrom(data.getClass())) {
            Map m = (Map)data;
            
        }
        return ext;
    }
}
