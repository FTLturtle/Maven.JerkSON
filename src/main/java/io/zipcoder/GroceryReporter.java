package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroceryReporter {
    private Map<String, List<Item>> itemMap;
    private int numberOfErrors;

    public GroceryReporter(String jerksonFileName) {
        String originalFileText = FileReader.readFile(jerksonFileName);
        ItemParser itemParser = new ItemParser();
        itemMap = new TreeMap<>(new GroceryComparator());
        populateItemMap(originalFileText, itemParser);
        numberOfErrors = itemParser.getNumberOfErrors();
    }

    private void populateItemMap(String originalFileText, ItemParser itemParser) {
        List<Item> itemList = itemParser.parseItemList(originalFileText);
        List<Item> items;
        for (Item item : itemList) {
            items = itemMap.getOrDefault(item.getName(), new ArrayList<>());
            items.add(item);
            itemMap.put(item.getName(), items);
        }
    }

    @Override
    public String toString() {
        return extractStringOutputFromMap();
    }

    private String extractStringOutputFromMap() {
        StringBuilder output = new StringBuilder();
        for (String key : itemMap.keySet()) {
            appendItemString(output, key);
            appendAllPriceStringsForItem(output, key);
        }
        output.replace(output.length() - 36, output.length(), "");
        output.append(String.format("\nErrors               seen: %d times\n", numberOfErrors));
        return output.toString();
    }

    private void appendItemString(StringBuilder output, String key) {
        int numberOfItems;
        numberOfItems = itemMap.get(key).size();
        String keyUpperCase = key.substring(0,1).toUpperCase() + key.substring(1);
        output.append(String.format("name:%8s        seen: %d times\n", keyUpperCase, numberOfItems))
                .append("=============        =============\n");
    }

    private void appendAllPriceStringsForItem(StringBuilder output, String key) {
        Map<Double, Integer> itemPriceMap = new TreeMap<>(new PriceComparator());
        populateItemPriceMap(key, itemPriceMap);
        appendPriceString(output, itemPriceMap);
        output.append("\n");
    }

    private void populateItemPriceMap(String key, Map<Double, Integer> itemPriceMap) {
        int numberOfItemsAtPrice;
        for (Item item : itemMap.get(key)) {
            numberOfItemsAtPrice = itemPriceMap.getOrDefault(item.getPrice(), 0);
            itemPriceMap.put(item.getPrice(), numberOfItemsAtPrice + 1);
        }
    }

    private void appendPriceString(StringBuilder output, Map<Double, Integer> itemPriceMap) {
        int numberOfItemsAtPrice = 0;
        for (Double price : itemPriceMap.keySet()) {
            numberOfItemsAtPrice = itemPriceMap.get(price);
            output.append(String.format("Price:   %4.2f        seen: %d times\n", price, numberOfItemsAtPrice))
            .append("-------------        -------------\n");
        }
        if (numberOfItemsAtPrice == 1) {
            output.replace(output.length() - 37, output.length() - 36, "");
        }
    }
    
}
