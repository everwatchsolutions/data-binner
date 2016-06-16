/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

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
        String data = "{\"test\": \"test123\"}";
        
        LiteralBinner binner = new LiteralBinner();
        binner.setCountName("test");
        binner.setDataFieldName("test");
        
        List<String> bins = binner.generateBinNames(data);
        for (String bin : bins) {
            log.info("Bin [ " + bin + " ]");
        }
    }
}
