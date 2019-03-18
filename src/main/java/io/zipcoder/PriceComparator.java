package io.zipcoder;

import java.util.Comparator;

public class PriceComparator implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return -o1.compareTo(o2);
    }
}
