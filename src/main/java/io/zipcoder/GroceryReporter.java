package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroceryReporter {
    private final String originalFileText;
    private Map<String, List<Item>> itemMap;
    private int numberOfErrors;

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
        ItemParser itemParser = new ItemParser();
        List<Item> itemList = itemParser.parseItemList(originalFileText);
        itemMap = new TreeMap<>(new GroceryComparator());

        List<Item> items;
        for (Item item : itemList) {
            items = itemMap.getOrDefault(item.getName(), new ArrayList<>());
            items.add(item);
            itemMap.put(item.getName(), items);
        }

        numberOfErrors = itemParser.getNumberOfErrors();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int numberOfItems;
        int numberOfItemsAtPrice = 0;
        Map<Double, Integer> itemPriceMap;

        for (String key : itemMap.keySet()) {
            numberOfItems = itemMap.get(key).size();
            String keyUpperCase = key.substring(0,1).toUpperCase() + key.substring(1);
            stringBuilder.append(String.format("name:%8s \t\t seen: %d times\n", keyUpperCase, numberOfItems))
                    .append("============= \t \t =============\n");

            itemPriceMap = new TreeMap<>(new PriceComparator());
            for (Item item : itemMap.get(key)) {
                numberOfItemsAtPrice = itemPriceMap.getOrDefault(item.getPrice(), 0);
                itemPriceMap.put(item.getPrice(), numberOfItemsAtPrice + 1);
            }

            for (Double price : itemPriceMap.keySet()) {
                numberOfItemsAtPrice = itemPriceMap.get(price);
                stringBuilder.append(String.format("Price: \t %4.2f\t\t seen: %d times\n", price, numberOfItemsAtPrice))
                .append("-------------\t\t -------------\n");
            }
            if (numberOfItemsAtPrice == 1) {
                stringBuilder.replace(stringBuilder.length() - 32, stringBuilder.length(), "\n");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.replace(stringBuilder.length() - 31, stringBuilder.length(), "");

        stringBuilder.append(String.format("\nErrors         \t \t seen: %d times\n", numberOfErrors));

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String string = "name:    Milk \t\t seen: 6 times\n" +
                "============= \t \t =============\n" +
                "Price: \t 3.23\t\t seen: 5 times\n" +
                "-------------\t\t -------------\n" +
                "Price:   1.23\t\t seen: 1 time\n" +
                "\n" +
                "name:   Bread\t\t seen: 6 times\n" +
                "=============\t\t =============\n" +
                "Price:   1.23\t\t seen: 6 times\n" +
                "-------------\t\t -------------\n" +
                "\n" +
                "name: Cookies     \t seen: 8 times\n" +
                "=============     \t =============\n" +
                "Price:   2.25        seen: 8 times\n" +
                "-------------        -------------\n" +
                "\n" +
                "name:  Apples     \t seen: 4 times\n" +
                "=============     \t =============\n" +
                "Price:   0.25     \t seen: 2 times\n" +
                "-------------     \t -------------\n" +
                "Price:   0.23  \t \t seen: 2 times\n" +
                "\n" +
                "Errors         \t \t seen: 4 times\n";
    }
}
