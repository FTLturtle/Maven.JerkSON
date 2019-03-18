package io.zipcoder;

import io.zipcoder.utils.Item;

import java.util.Comparator;

public class GroceryComparator implements Comparator<String> {
    @Override
    public int compare(String string1, String string2) {
        return string1.compareTo(string2);
    }
}
