package javarush.shortener.tasks.tests;

import org.junit.Assert;
import org.junit.Test;
import javarush.shortener.Helper;
import javarush.shortener.Shortener;
import javarush.shortener.strategy.HashBiMapStorageStrategy;
import javarush.shortener.strategy.HashMapStorageStrategy;

import java.util.HashSet;
import java.util.Set;

public class SpeedTest {

    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        long startTime = System.currentTimeMillis();
        for (String s : strings) {
            Long id = shortener.getId(s);
            ids.add(id);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        long startTime = System.currentTimeMillis();
        for (Long id : ids) {
            String s = shortener.getString(id);
            strings.add(s);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    @Test
    public void testHashMapStorageStrategy() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());
        Set<String> origStrings = new HashSet<>();
        Set<Long> ids = new HashSet<>();

        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }

        long timeToGetIds1 = getTimeToGetIds(shortener1, origStrings, ids);
        long timeToGetIds2 = getTimeToGetIds(shortener2, origStrings, new HashSet<>());
        Assert.assertTrue(timeToGetIds1 > timeToGetIds2);

        long timeToGetStrings1 = getTimeToGetStrings(shortener1, ids, new HashSet<>());
        long timeToGetStrings2 = getTimeToGetStrings(shortener2, ids, new HashSet<>());
        Assert.assertEquals(timeToGetStrings2, timeToGetStrings1, 30);
    }

}
