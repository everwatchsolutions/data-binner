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
public class MergedBinner extends Binner {

    private List<Binner> binners;

    public MergedBinner(List<Binner> binners) {
        super(null);
        this.binners = binners;
    }

    @Override
    public List<String> generateBinNames(Object data) {
        //generate all the binners bins
        List<List<String>> generatedBins = new ArrayList<>();

        binners.stream().map((b) -> {
            if (b.getGeneratedBinNames() == null) {
                b.generateBinNames(data);
            }
            return b;
        }).forEach((b) -> {
            generatedBins.add(b.getGeneratedBinNames());
        });

        //then merge those bins in the order the binners were specified
        List<String> binNames = new ArrayList<>();
        List<String> startingBins = generatedBins.get(0);

        for (List<String> bins : generatedBins.subList(1, generatedBins.size())) {
            List<String> newBins = new ArrayList<>();
            for (String bin : bins) {
                for (String prefix : startingBins) {
                    newBins.add(prefix + "." + bin);
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
