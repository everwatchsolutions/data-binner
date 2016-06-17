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

    private String binName;
    private String dataFieldName;

    public static final String ALL_COUNT_NAME = "All";

    public Binner(String binName) {
        this(binName, binName);
    }

    public Binner(String binName, String dataFieldName) {
        this.binName = binName;
        this.dataFieldName = dataFieldName;
    }

    public List<String> generateBinNames(Object data) {
        try {
            DataExtractor ext = DataExtractorFactory.getDataExtractor(data);
            if (ext != null) {
                Object val = ext.getValueForFieldName(dataFieldName);
                if (val != null) {
                    List<String> binNames = new ArrayList<>();

                    //always add an All count
                    binNames.add(getBinName() + "." + Binner.ALL_COUNT_NAME);

                    //now add all the generated count names
                    binNames.addAll(generateBinNamesForData(val));
                    return binNames;
                } else {
                    log.warn("No data exists for field [ " + dataFieldName + " ]");
                }
            }
        } catch (Exception e) {
            log.error("Error getting DataExtractor", e);
        }
        return new ArrayList<>();
    }

    protected abstract List<String> generateBinNamesForData(Object value);

    /**
     * @return the binName
     */
    public String getBinName() {
        return binName;
    }

    /**
     * @param binName the binName to set
     */
    public void setBinName(String binName) {
        this.binName = binName;
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
