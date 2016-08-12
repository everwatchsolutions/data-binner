/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 *
 * @author andrewserff
 */
public class JsonDataExtractor extends DataExtractor {

    private ObjectMapper mapper;
    private Map<String, Object> parsedData;
    
    public JsonDataExtractor(Object data) throws Exception {
        super(data);
        mapper = new ObjectMapper();
        parsedData = mapper.readValue((String)data, Map.class);
    }
    
    @Override
    public Object getValueForFieldName(String name) {
        return getValueForFieldName(name, parsedData);
    }
    
    protected Object getValueForFieldName(String name, Map<String, Object> dataMap) {
        if (name.contains(".")) {
            String thisLevelName = name.substring(0, name.indexOf("."));
            String nextLevelName = name.substring(name.indexOf(".") + 1);
            Map<String, Object> nextLevel = (Map<String, Object>) parsedData.get(thisLevelName);
            return getValueForFieldName(nextLevelName, nextLevel);
        } else {
            if (dataMap != null) {
                return dataMap.get(name);
            } else {
                return null;
            }
        }
    }
    
}
