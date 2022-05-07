package javarush.task2025;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TEST {
    private static long[] armstrongs = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 153, 370, 371, 407, 1634, 8208, 9474, 54748, 92727,
            93084, 548834, 1741725, 4210818, 9800817, 9926315, 24678050, 24678051, 88593477,
            146511208, 472335975, 534494836, 912985153, 4679307774L, 32164049650L, 32164049651L,
            40028394225L, 42678290603L, 44708635679L, 49388550606L, 82693916578L, 94204591914L,
            28116440335967L, 4338281769391370L, 4338281769391371L, 21897142587612075L,
            35641594208964132L, 35875699062250035L, 1517841543307505039L, 3289582984443187032L,
            4498128791164624869L, 4929273885928088826L
    };

    // Тесты для многопоточной версии Solution

    @Test
    public void getSerialTestForV1() {
        for (int i = 0; i < armstrongs.length; i++) {
            long[] actualNumbers = Solution.getNumbers(armstrongs[i] + 1);
            Assert.assertEquals(i + 1, actualNumbers.length);
            System.out.println("(" + (i + 1) + ") " + armstrongs[i] + " : " + Arrays.toString(actualNumbers));
        }
        System.out.println();
    }

    @Test
    public void getArmstrongsV1() {
        long[] actualNumbers = Solution.getNumbers(9475);
        System.out.println(actualNumbers.length);
        System.out.println(Arrays.toString(actualNumbers));
        System.out.println();
    }

    // Тесты для однопоточной версии SolutionV2

    @Test
    public void getSerialTestForV2() {
        for (int i = 0; i < armstrongs.length; i++) {
            long[] actualNumbers = SolutionV2.getNumbers(armstrongs[i] + 1);
            Assert.assertEquals(i + 1, actualNumbers.length);
            System.out.println("(" + (i + 1) + ") " + armstrongs[i] + " : " + actualNumbers[i]);
        }
        System.out.println();
    }

    @Test
    public void getArmstrongsV2() {
        long[] actualNumbers = SolutionV2.getNumbers(912985153);
        System.out.println(actualNumbers.length);
        System.out.println(Arrays.toString(actualNumbers));
        System.out.println();
    }

}
