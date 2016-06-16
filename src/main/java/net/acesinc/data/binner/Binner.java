/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

import net.acesinc.data.binner.extractor.DataExtractorFactory;
import net.acesinc.data.binner.extractor.DataExtractor;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public abstract class Binner {
    private static Logger log = LoggerFactory.getLogger(Binner.class);
    
    private String countName;
    private String dataFieldName;
    
    public static final String ALL_COUNT_NAME = "All";
    
    public List<String> generateBinNames(Object data) {
        try {
            DataExtractor ext = DataExtractorFactory.getDataExtractor(data);
            if (ext != null) {
                Object val = ext.getValueForFieldName(dataFieldName);
                return generateBinNamesForData(val);
            }
        } catch (Exception e) {
            log.error("Error getting DataExtractor", e);
        }
        return new ArrayList<>();
    } 
    
    protected abstract List<String> generateBinNamesForData(Object value);

    /**
     * @return the countName
     */
    public String getCountName() {
        return countName;
    }

    /**
     * @param countName the countName to set
     */
    public void setCountName(String countName) {
        this.countName = countName;
    }

    /**
     * @return the dataFieldName
     */
    public String getDataFieldName() {
        return dataFieldName;
    }

    /**
     * @param dataFieldName the dataFieldName to set
     */
    public void setDataFieldName(String dataFieldName) {
        this.dataFieldName = dataFieldName;
    }
    
}
