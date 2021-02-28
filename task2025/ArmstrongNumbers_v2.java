package javarush.task2025;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * ПОИСК ЧИСЕЛ АРМСТРОНГА
 *
 * Многопоточная версия c использованием синхронизации
 * при генерировании уникальных комбинаций.
 *
 * Наилучшего быстродействия удалось достичь при двойной загрузке
 * каждого вычислительного ядра.
 *
 * Для N = Long.MAX_VALUE на 4-ядерном процессоре Intel Core i5-3570
 * при выполнении в 8 потоков алгоритм полностью отрабатывает за 990 мс
 * в "разогретом" состоянии (~1250 мс - при "холодном" старте).
 *
 */
public class ArmstrongNumbers_v2 {
    private static final long[][] matrix;

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


    private static class Generator {
        private final Set<Long> armstrongs = new CopyOnWriteArraySet<>();
        private final int[] intArray;

        private AtomicBoolean stop = new AtomicBoolean(false);

        private Generator(long N) {
            intArray = new int[String.valueOf(N).length()];
            Arrays.fill(intArray, 9);
        }

        private boolean decrementArray() {
            int index = 0;
            while (index < intArray.length && intArray[index] == 0) {
                index++;
            }
            if (index + 1 == intArray.length && intArray[index] == 1) {
                return false;
            }
            Arrays.fill(intArray, 0, index + 1, intArray[index] - 1);
            return true;
        }

        synchronized public int[] nextSequence() {
            if (!decrementArray()) {
                stop.set(true);
            }
            return intArray.clone();
        }

        boolean hasNext() {
            return !stop.get();
        }

    }


    private static class Calculator implements Runnable {
        private Generator generator;

        public Calculator(Generator generator) {
            this.generator = generator;
        }

        private long pow(int base, int exp) {
            return matrix[base][exp];
        }

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

        private int[] getDigits(long number) {
            int[] digits = new int[getNumberOrder(number)];
            long tempNumber1 = number, tempNumber2;

            for (int i = 0; i < digits.length; i++) {
                digits[i] = (int) (tempNumber1 % 10);

                if ((tempNumber2 = tempNumber1 / 10) > 0)
                    tempNumber1 = tempNumber2;
            }
            return digits;
        }

        private long calculateSequence(int[] sequence) {
            long result = 0;

            for (int i = 0; i < sequence.length; i++) {
                result += pow(sequence[i], sequence.length);
                if (result < 0) {
                    return -1;
                }
            }
            return result;
        }

        private void checkSequence(int[] sequence) {
            long result1 = calculateSequence(sequence);
            if (result1 > 0) {
                long result2 = calculateSequence(getDigits(result1));
                if (result2 == result1) {
                    generator.armstrongs.add(result2);
                }
            }
        }

        @Override
        public void run() {
            while (generator.hasNext()) {
                int[] sequence;
                if ((sequence = generator.nextSequence()) != null) {
                    while (sequence.length > 0 && sequence[0] == 0) {
                        checkSequence(sequence);
                        int[] tmpSequence = new int[sequence.length - 1];
                        System.arraycopy(sequence, 1, tmpSequence, 0, tmpSequence.length);
                        sequence = tmpSequence;
                    }
                    checkSequence(sequence);
                }
            }
        }

    }


    public static long[] getNumbers(long N) {
        if (N > 0) {
            int coreNumber = Runtime.getRuntime().availableProcessors() * 2;
            Generator generator = new Generator(N);
            ExecutorService exec = Executors.newFixedThreadPool(coreNumber);

            for (int i = 0; i < coreNumber; i++) {
                exec.execute(new Calculator(generator));
            }
            exec.shutdown();
            while (!exec.isTerminated());

            Set<Long> resultSet = new TreeSet<>(generator.armstrongs);
            resultSet.removeIf(e -> e >= N);
            Iterator<Long> iterator = resultSet.iterator();
            long[] resultArray = new long[resultSet.size()];

            for (int i = 0; i < resultArray.length; i++) {
                resultArray[i] = iterator.next();
            }
            return resultArray;
        } else {
            return new long[0];
        }
    }

    public static void main(String[] args) {
        long a, b;
        long[] testLongs = {
                Long.MIN_VALUE,
                -1,
                0,
                15555500,
                1000,
                1_000_000,
                Integer.MAX_VALUE,
                548834,
                1,
                2,
                8,
                371,
                548835,
                Long.MAX_VALUE,
        };

        for (int i = 0; i < testLongs.length; i++) {
            a = System.currentTimeMillis();
            long[] result = getNumbers(testLongs[i]);
            System.out.println(Arrays.toString(result));
            b = System.currentTimeMillis();
            System.out.println("testNumber = " + testLongs[i]);
            System.out.println("numbers found: " + result.length);
            System.out.println("memory = " + (Runtime.getRuntime().totalMemory()
                    - Runtime.getRuntime().freeMemory()) / (8 * 1024));
            System.out.println("time = " + (b - a));
            System.out.println();
        }
    }

}
