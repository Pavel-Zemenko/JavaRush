package javarush.task2025;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
Алгоритмы-числа (многопоточная версия)
*/
public class Solution {
    private static long[][] matrix;
    private static Generator generator;

    static {
        matrix = new long[10][20];
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
                if (j == 1) {
                    matrix[i][j] = i;
                    continue;
                }
                matrix[i][j] = (long) i * matrix[i][j - 1];
            }
        }
    }

    /*
     * Генератор для получения только "полезных" наборов цифр,
     * на базе которых в дальнейшем будет осуществляться поиск чисел Армстронга.
     */
    static class Generator {
        private int[] digits;
        private boolean isGenerationAllowed;

        public Generator(long number) {
            digits = new int[String.valueOf(number).length()];
            Arrays.fill(digits, 9);
            isGenerationAllowed = true;
        }

        public void decrementDigit() {
            int index = 0;

            while (index < digits.length && digits[index] == 0) {
                index++;
            }

            if (index + 1 == digits.length && digits[index] == 1) {
                isGenerationAllowed = false;
                return;
            }

            Arrays.fill(digits, 0, index + 1, digits[index] - 1);
        }

        public int[] getDigits() {
            return Arrays.copyOf(digits, digits.length);
        }

        public boolean isGenerationAllowed() {
            return isGenerationAllowed;
        }
    }

    /*
     * Содержит общий алгоритм для поиска чисел Армстронга,
     * а также необходимые вспомогательные инструменты.
     */
    static class CalcTask implements Runnable {
        private final SortedSet<Long> resultSet;

        public CalcTask(SortedSet<Long> resultSet) {
            this.resultSet = resultSet;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (generator) {
                    int[] array = generator.getDigits();
                    checker(array);
                    while (array.length > 0 && array[0] == 0) {
                        array = trimZero(array);
                        checker(array);
                    }
                    if (generator.isGenerationAllowed()) {
                        generator.decrementDigit();
                    }
                    else return;
                }
            }
        }

        /*
         * Возвращает порядок числа (количество разрядов в числе).
         * Если передаваемое число отрицательное, возвращает ноль.
         */
        private int getNumberOrder(long number) {
            long p = 10;
            for (int i = 1; i < 19; i++) {
                if (number < p) {
                    return i;
                }
                p *= 10;
            }
            return 19;
        }

        /*
         * Преобразует число в массив составялющих его цифр.
         */
        private int[] digitize(long number) {
            int[] digits = new int[getNumberOrder(number)];
            long tempNumber1 = number, tempNumber2;

            for (int i = 0; i < digits.length; i++) {
                digits[i] = (int) (tempNumber1 % 10);

                if ((tempNumber2 = tempNumber1 / 10) > 0)
                    tempNumber1 = tempNumber2;
            }
            return digits;
        }

        /*
         * Вычисляет и возвращает степенную сумму набора цифр.
         */
        private long getPowerSum(int[] digits) {
            if (digits.length == 0 || digits[0] < 0)
                return 0;

            long result = 0;
            for (int digit : digits) {
                if (digit == -1)
                    break;
                result += matrix[digit][digits.length];
            }
            return result;
        }

        /*
         * Проверяет набор цифр. Если в результате проверки найдено число Армстронга,
         * оно будет занесено в итоговое множество resultSet.
         */
        private void checker(int[] digits) {
            long powerSum = getPowerSum(digits);
            int[] powerSumDigits = digitize(powerSum);
            if (powerSum == getPowerSum(powerSumDigits)) {
                resultSet.add(powerSum);
            }
        }

        /*
         * Отрезает начальный ноль (если таковой имеется) от передаваемого массива.
         */
        private int[] trimZero(int[] digits) {
            if (digits[0] == 0) {
                return Arrays.copyOfRange(digits, 1, digits.length);
            }
            return new int[0];
        }
    }

    /*
     * Возвращает массив, полученный из множества resultSet.
     */
    private static long[] getResultArray(long number, SortedSet<Long> resultSet) {
        resultSet.removeIf(armstrong -> armstrong == 0 || armstrong >= number);
        long[] resultArray = new long[resultSet.size()];
        Iterator<Long> it = resultSet.iterator();
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = it.next();
        }
        return resultArray;
    }

    public static long[] getNumbers(long N) {
        SortedSet<Long> resultSet = Collections.synchronizedSortedSet(new TreeSet<>());

        if (N > 0) {
            int threadCount = Runtime.getRuntime().availableProcessors() - 1;
            generator = new Generator(N);
            ExecutorService exec = Executors.newFixedThreadPool(threadCount);

            for (int i = 0; i < threadCount; i++)
                exec.execute(new CalcTask(resultSet));
            exec.shutdown();

            try {
                exec.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return getResultArray(N, resultSet);
    }

    public static void main(String[] args) {
        long a, b;
        long[] testLongs = {
                Long.MIN_VALUE,
                -1,
                0,
                10,
                371,
                548834,
                548835,
                Integer.MAX_VALUE,
                Long.MAX_VALUE,
        };

        for (int i = 0; i < testLongs.length; i++) {
            a = System.currentTimeMillis();
            long[] numbers = getNumbers(testLongs[i]);
            System.out.println(Arrays.toString(numbers));
            b = System.currentTimeMillis();
            System.out.println("testNumber = " + testLongs[i]);
            System.out.println("numbers found: " + numbers.length);
            System.out.println("memory = " + (Runtime.getRuntime().totalMemory()
                    - Runtime.getRuntime().freeMemory()) / (8 * 1024));
            System.out.println("time = " + (b - a));
            System.out.println();
        }
    }

}
