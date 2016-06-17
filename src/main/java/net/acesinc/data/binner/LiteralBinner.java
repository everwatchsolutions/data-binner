/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewserff
 */
public class LiteralBinner extends Binner {

    public LiteralBinner(String countName) {
        super(countName);
    }
    public LiteralBinner(String countName, String dataFieldName) {
        super(countName, dataFieldName);
    }
    
    @Override
    protected List<String> generateBinNamesForData(Object value) {
        List<String> binNames = new ArrayList<>();
        
        binNames.add(getCountName() + "." + value);
        return binNames;
    }
    
}
