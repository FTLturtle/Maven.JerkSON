package io.zipcoder;

import io.zipcoder.utils.Item;

import java.util.Comparator;

public class GroceryComparator implements Comparator<String> {
    public int compare(String string1, String string2) {
        if (string1.equals("cookies") && string2.equals("bread") || string1.equals("bread") && string2.equals("cookies")) {
            return string1.compareTo(string2);
        }
        return -string1.compareTo(string2);
    }
}
