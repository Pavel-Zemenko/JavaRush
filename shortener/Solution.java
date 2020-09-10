package javarush.shortener;

import javarush.shortener.strategy.HashMapStorageStrategy;
import javarush.shortener.strategy.StorageStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        StorageStrategy strategy;

        strategy = new HashMapStorageStrategy();
        testStrategy(strategy, 10000);
        System.out.println();
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        Set<Long> ids = new HashSet<>();
        strings.forEach(string -> {
            Long id = shortener.getId(string);
            ids.add(id);
        });
        return ids;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> strings = new HashSet<>();
        keys.forEach(key -> {
            String string = shortener.getString(key);
            strings.add(string);
        });
        return strings;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());
        Shortener shortener = new Shortener(strategy);
        Set<String> testStrings = new HashSet<>();

        for (long i = 0; i < elementsNumber; i++) {
            String string = Helper.generateRandomString();
            testStrings.add(string);
        }

        long startTime = new Date().getTime();
        Set<Long> ids =getIds(shortener, testStrings);
        long finishTime = new Date().getTime();
        Helper.printMessage(String.valueOf(finishTime - startTime));

        startTime = new Date().getTime();
        Set<String> strings =getStrings(shortener, ids);
        finishTime = new Date().getTime();
        Helper.printMessage(String.valueOf(finishTime - startTime));

        if (strings.equals(testStrings)) {
            Helper.printMessage("Тест пройден.");
        } else {
            Helper.printMessage("Тест не пройден.");
        }
    }
}
