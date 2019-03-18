package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {
    private static Pattern pattern = Pattern.compile("(?<=[:@^*%]).+");
    private int numberOfErrors;

    public List<Item> parseItemList(String valueToParse) {
        String[] arrayOfStrings = valueToParse.split("##");
        List<Item> itemList = new ArrayList<>();
        numberOfErrors = 0;

        for (String string : arrayOfStrings) {
            try {
                itemList.add(parseSingleItem(string));
            } catch (ItemParseException e) {
                e.printStackTrace();
                numberOfErrors++;
            }
        }
        Matcher matcher = pattern.matcher(valueToParse);

        return itemList;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        String[] singleItemArray = getSingleItemArray(singleItem);
        return getItem(singleItemArray);
    }

    private String[] getSingleItemArray(String singleItem) {
        singleItem = singleItem.replaceAll("#", "");
        singleItem = singleItem.replaceAll("(?<=[a-zA-Z])0(?=[a-zA-Z])", "o");
        singleItem = singleItem.toLowerCase();
        return singleItem.split(";|([:!@^*%](?=(name|price|type|expiration)))");
    }

    private Item getItem(String[] singleItemArray) throws ItemParseException {
        String name;
        Double price;
        String type;
        String expiration;
        Matcher matcher;

        try {
            matcher = pattern.matcher(singleItemArray[0]);
            matcher.find();
            name = matcher.group(0);
            matcher = pattern.matcher(singleItemArray[1]);
            matcher.find();
            price = Double.parseDouble(matcher.group(0));
            matcher = pattern.matcher(singleItemArray[2]);
            matcher.find();
            type = matcher.group(0);
            matcher = pattern.matcher(singleItemArray[3]);
            matcher.find();
            expiration = matcher.group(0);
        } catch (Exception e){
            e.printStackTrace();
            throw new ItemParseException();
        }

        return new Item(name, price, type, expiration);
    }

    public int getNumberOfErrors() {
        return numberOfErrors;
    }
}
