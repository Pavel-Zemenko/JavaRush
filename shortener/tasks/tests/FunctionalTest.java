package javarush.shortener.tasks.tests;

import javarush.shortener.strategy.HashMapStorageStrategy;
import javarush.shortener.strategy.StorageStrategy;
import org.junit.Assert;
import org.junit.Test;
import javarush.shortener.Helper;
import javarush.shortener.Shortener;
import javarush.shortener.strategy.*;

public class FunctionalTest {

    public void testStorage(Shortener shortener) {
        String s1, s2, s3;
        s1 = s3 = Helper.generateRandomString();
        s2 = Helper.generateRandomString();

        long id1 = shortener.getId(s1);
        long id2 = shortener.getId(s2);
        long id3 = shortener.getId(s3);

        Assert.assertNotEquals(id1, id2);
        Assert.assertNotEquals(id3, id2);
        Assert.assertEquals(id1, id3);

        String str1 = shortener.getString(id1);
        String str2 = shortener.getString(id2);
        String str3 = shortener.getString(id3);

        Assert.assertEquals(s1, str1);
        Assert.assertEquals(s2, str2);
        Assert.assertEquals(s3, str3);
    }

    @Test
    public void testHashMapStorageStrategy() {
        StorageStrategy strategy = new HashMapStorageStrategy();
        Shortener shortener = new Shortener(strategy);
        testStorage(shortener);
    }

    @Test
    public void testOurHashMapStorageStrategy() {
        StorageStrategy strategy = new OurHashMapStorageStrategy();
        Shortener shortener = new Shortener(strategy);
        testStorage(shortener);
    }

    @Test
    public void testFileStorageStrategy() {
        StorageStrategy strategy = new FileStorageStrategy();
        Shortener shortener = new Shortener(strategy);
        testStorage(shortener);
    }

    @Test
    public void testHashBiMapStorageStrategy() {
        StorageStrategy strategy = new HashBiMapStorageStrategy();
        Shortener shortener = new Shortener(strategy);
        testStorage(shortener);
    }

    @Test
    public void testDualHashBidiMapStorageStrategy() {
        StorageStrategy strategy = new DualHashBidiMapStorageStrategy();
        Shortener shortener = new Shortener(strategy);
        testStorage(shortener);
    }

    @Test
    public void testOurHashBiMapStorageStrategy() {
        StorageStrategy strategy = new OurHashBiMapStorageStrategy();
        Shortener shortener = new Shortener(strategy);
        testStorage(shortener);
    }

}
