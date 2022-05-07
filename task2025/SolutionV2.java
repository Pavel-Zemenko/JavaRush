package javarush.task2025;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;


/*
Алгоритмы-числа (однопоточная версия)
*/
public class SolutionV2 {
    private static long[][] matrix;
    private static volatile TreeSet<Long> resultSet = new TreeSet<>();

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

    public static long[] getNumbers(long N) {
        resultSet.clear();
        if (N > 0) {
            Generator generator = new Generator(N);
            while (true) {
                int[] array = generator.getDigits();
                checker(array);
                while (array.length > 0 && array[0] == 0) {
                    array = trimZero(array);
                    checker(array);
                }
                if (generator.isGenerationAllowed())
                    generator.decrementDigit(0);
                else break;
            }

        }
        return getResultArray(N);
    }

    // ВСПОМОГАТЕЛЬНЫЕ КЛАССЫ И МЕТОДЫ

    /*
     * Генератор для получения только "полезных" наборов цифр,
     * на базе которых в дальенйшем будет осуществляться поиск чисел Армстронга.
     * Имеется блокировка для совместного использования экземпляра несколькими потоками.
     */
    static class Generator {
        private int[] digits;
        private boolean isGenerationAllowed;

        public Generator(long number) {
            digits = new int[String.valueOf(number).length()];
            Arrays.fill(digits, 9);
            isGenerationAllowed = true;
        }

        public int decrementDigit(int index) {
            if (index > digits.length - 1) {
                int lastDigit = digits[digits.length - 1];
                if (lastDigit <= 1) {
                    isGenerationAllowed = false;
                    return -1;
                }
            }

            if (digits[index]-- == 0) {
                digits[index] = decrementDigit(index + 1);
            }

            return digits[index];
        }

        public int[] getDigits() {
            return Arrays.copyOf(digits, digits.length);
        }

        public boolean isGenerationAllowed() {
            return isGenerationAllowed;
        }
    }

    /*
     * Возвращает порядок числа (количество разрядов в числе).
     * Если передаваемое число отрицательное, возвращает ноль.
     */
    private static int getNumberOrder(long number) {
        if (number < 0)
            return 0;
        return String.valueOf(number).length();
    }

    /*
     * Преобразует число в массив составялющих его цифр.
     */
    private static int[] digitize(long number) {
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
    private static long getPowerSum(int[] digits) {
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
    private static void checker(int[] digits) {
        long powerSum = getPowerSum(digits);
        int[] powerSumDigits = digitize(powerSum);
        if (powerSum == getPowerSum(powerSumDigits)) {
            resultSet.add(powerSum);
        }
    }

    /*
     * Отрезает начальный ноль (если таковой имеется) от передаваемого массива.
     */
    private static int[] trimZero(int[] digits) {
        if (digits[0] == 0) {
            return Arrays.copyOfRange(digits, 1, digits.length);
        }
        return new int[0];
    }

    /*
     * Возвращает массив, полученный из множества resultSet.
     */
    private static long[] getResultArray(long number) {
        resultSet.removeIf(armstrong -> armstrong == 0 || armstrong >= number);
        long[] resultArray = new long[resultSet.size()];
        Iterator<Long> it = resultSet.iterator();
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = it.next();
        }
        return resultArray;
    }

    public static void main(String[] args) {
        long a, b;
        long[] testLongs = {
                Long.MIN_VALUE,
                -1,
                0,
                1,
                2,
                8,
                9,
                10,
                371,
                1000,
                548834,
                548835,
                15555500,
                1000000,
                Integer.MAX_VALUE,
                Long.MAX_VALUE,
        };

        for (int j = 0; j < 2; j++) {
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

}
