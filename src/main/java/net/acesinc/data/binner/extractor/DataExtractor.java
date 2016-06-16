/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner.extractor;

/**
 *
 * @author andrewserff
 */
public abstract class DataExtractor {
    private Object data;

    public DataExtractor(Object data) throws Exception {
        this.data = data;
    }
    
    public abstract Object getValueForFieldName(String name);

    /**
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }
}
