package javarush.shortener;

import javarush.shortener.strategy.DualHashBidiMapStorageStrategy;
import javarush.shortener.strategy.Entry;
import javarush.shortener.strategy.FileBucket;
import javarush.shortener.strategy.FileStorageStrategy;
import javarush.shortener.strategy.HashBiMapStorageStrategy;
import javarush.shortener.strategy.OurHashBiMapStorageStrategy;
import javarush.shortener.strategy.OurHashMapStorageStrategy;
import javarush.shortener.strategy.StorageStrategy;

import static shortener.Solution.testStrategy;

public class TEST {
    public static void main(String[] args) throws Exception {
        ourHashMapStorageStrategyTest();      // Shortener (8)
//        fileBucketTest();                     // Shortener (9)
        fileStorageStrategyTest();            // Shortener (10)
        ourHashBiMapStorageStrategyTest();    // Shortener (11)
        hashBiMapStorageStrategyTest();       // Shortener (12)
        dualHashBidiMapStorageStrategyTest(); // Shortener (13)
    }


    /*
     * Shortener (8) test
     */
    private static void ourHashMapStorageStrategyTest() {
        StorageStrategy strategy = new OurHashMapStorageStrategy();
        testStrategy(strategy, 10000);
        System.out.println();
    }

    /*
     * Shortener (9) test
     */
    private static void fileBucketTest() {
        FileBucket bucket = new FileBucket();
        Entry e1 = new Entry(123, 10L, "FirstEntry", null);

        bucket.putEntry(e1);
        Entry loadedEntry = bucket.getEntry();
        System.out.println(loadedEntry);
        System.out.println(e1.equals(loadedEntry));
        System.out.println();

        Entry e3 = new Entry(789, 34L, "ThirdEntry", null);
        Entry e2 = new Entry(456, 22L, "SecondEntry", e3);
        Entry e0 = new Entry(000, 100L, "ZeroEntry", e2);

        bucket.putEntry(e0);
        loadedEntry = bucket.getEntry();
        System.out.println(loadedEntry);
        System.out.println(e0.equals(loadedEntry));
        System.out.println();
    }

    /*
     * Shortener (10) test
     */
    private static void fileStorageStrategyTest() {
        StorageStrategy strategy = new FileStorageStrategy();
        testStrategy(strategy, 500);
        System.out.println();
    }

    /*
     * Shortener (11) test
     */
    private static void ourHashBiMapStorageStrategyTest() {
        StorageStrategy strategy = new OurHashBiMapStorageStrategy();
        testStrategy(strategy, 10000);
        System.out.println();
    }

    /*
     * Shortener (12) test
     */
    private static void hashBiMapStorageStrategyTest() {
        StorageStrategy strategy = new HashBiMapStorageStrategy();
        testStrategy(strategy, 10000);
        System.out.println();
    }

    /*
     * Shortener (13) test
     */
    private static void dualHashBidiMapStorageStrategyTest() {
        StorageStrategy strategy = new DualHashBidiMapStorageStrategy();
        testStrategy(strategy, 10000);
        System.out.println();
    }

}
