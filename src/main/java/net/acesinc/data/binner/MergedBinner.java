/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andrewserff
 */
public class MergedBinner extends Binner {

    private List<Binner> binners;

    public MergedBinner(List<Binner> binners) {
        super(null);
        this.binners = binners;
    }

    @Override
    public List<String> generateBinNames(Object data) {
        //generate all the binners bins
        List<List<String>> generatedBins = Collections.synchronizedList(new ArrayList<>());

        binners.stream().map((b) -> {
            if (b.getGeneratedBinNames() == null) {
                b.generateBinNames(data);
            }
            return b;
        }).forEach((b) -> {
            generatedBins.add(b.getGeneratedBinNames());
        });

        //then merge those bins in the order the binners were specified
        List<String> binNames = Collections.synchronizedList(new ArrayList<>());
        List<String> startingBins = generatedBins.get(0);

        for (List<String> bins : generatedBins.subList(1, generatedBins.size())) {
            List<String> newBins = Collections.synchronizedList(new ArrayList<>());
            if (bins != null) {
                //issues w/ for-each loop here due to concurrent modification
                for (int i = 0; i < bins.size(); i++) {
                    for (int j = 0; j < startingBins.size(); j++) {
                        newBins.add(startingBins.get(j) + "." + bins.get(i));
                    }
                }
            }
            binNames.addAll(newBins);
            startingBins = binNames;
        }

        return binNames;
    }

    @Override
    protected List<String> generateBinNamesForData(Object value) {
        //this is unused for this Binner
        return null;
    }

}
