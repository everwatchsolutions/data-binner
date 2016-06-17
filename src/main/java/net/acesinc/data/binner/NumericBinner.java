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
public class NumericBinner extends Binner {

    private int maxLevel = 6;

    public NumericBinner(String countName) {
        super(countName);
    }

    public NumericBinner(String countName, int maxLevel) {
        this(countName, countName, maxLevel);
    }

    public NumericBinner(String countName, String dataFieldName) {
        super(countName, dataFieldName);
    }

    public NumericBinner(String countName, String dataFieldName, int maxLevel) {
        super(countName, dataFieldName);
        this.maxLevel = maxLevel;
    }

    @Override
    protected List<String> generateBinNamesForData(Object value) {
        List<String> binNames = new ArrayList<>();

        if (Number.class.isAssignableFrom(value.getClass())) {
            Number num = (Number) value;
            for (int i = 0; i < maxLevel; i++) {
                Double place = Math.pow(10.0, new Integer(i).doubleValue());
                long roundedVal = roundToNearestN(num.doubleValue(), place.intValue());
                long binLower = roundedVal;
                long binUpper = roundedVal + place.longValue();
                
                binNames.add(getBinName() + "." + binLower + "-" + binUpper);
            }
        }
        return binNames;
    }

    private long roundToNearestN(double value, int n) {
        return new Double(Math.floor(value / n)).longValue() * n;
    }

}
