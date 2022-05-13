package javarush.task1916;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Набор тестов к задаче "Отслеживаем изменения"
*/

public class TEST {
    private static class TestData {
        String fileName1;
        String fileName2;
        String[] expectedStrings;

        public TestData(String fileName1, String fileName2, String[] expectedStrings) {
            this.fileName1 = fileName1;
            this.fileName2 = fileName2;
            this.expectedStrings = expectedStrings;
        }
    }

    private List<TestData> dataList = new ArrayList<>();

    {
        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v1.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v1.txt",
                new String[] {
                        "SAME строка1",
                        "REMOVED строка2",
                        "SAME строка3",
                        "REMOVED строка4",
                        "SAME строка5",
                        "ADDED строка0",
                        "SAME строка1",
                        "REMOVED строка2",
                        "SAME строка3",
                        "ADDED строка4",
                        "SAME строка5",
                        "REMOVED строка0"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v2.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v2.txt",
                new String[] {
                        "SAME строка1",
                        "SAME строка2",
                        "SAME строка3",
                        "REMOVED строка4",
                        "SAME строка5",
                        "ADDED строка0",
                        "SAME строка1",
                        "SAME строка2",
                        "SAME строка3",
                        "ADDED строка4",
                        "SAME строка5",
                        "ADDED строка0"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v3.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v3.txt",
                new String[] {
                        "SAME 1000001",
                        "REMOVED 1000002",
                        "SAME 1000003",
                        "REMOVED 1000004",
                        "SAME 1000005",
                        "ADDED 1000006",
                        "SAME 1000007",
                        "REMOVED 1000008",
                        "SAME 1000009",
                        "ADDED 1000010",
                        "SAME 1000011",
                        "REMOVED 1000012"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v4.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v4.txt",
                new String[] {
                        "SAME строка1",
                        "ADDED строка0"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v5.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v5.txt",
                new String[] {
                        "SAME 1000001",
                        "REMOVED 1000002",
                        "SAME 1000003",
                        "SAME 1000004",
                        "REMOVED 1000005",
                        "SAME 1000006",
                        "SAME 1000007",
                        "SAME 1000008",
                        "REMOVED 1000009",
                        "SAME 1000010",
                        "SAME 1000011",
                        "REMOVED 1000012"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v6.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v6.txt",
                new String[] {
                        "SAME строка1",
                        "REMOVED строка0",
                        "SAME строка2",
                        "ADDED строка3",
                        "SAME строка4",
                        "REMOVED строка5",
                        "SAME строка6",
                        "REMOVED строка7"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v7.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v7.txt",
                new String[] {
                        "REMOVED строка0",
                        "SAME строка1",
                        "REMOVED строка2"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v8.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v8.txt",
                new String[] {
                        "ADDED строка0",
                        "SAME строка2",
                        "SAME строка3",
                        "REMOVED строка0",
                        "SAME строка4"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v9.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v9.txt",
                new String[] {
                        "REMOVED строка0",
                        "SAME строка1"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v10.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v10.txt",
                new String[] {
                        "ADDED строка0"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v11.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v11.txt",
                new String[] {
                        "REMOVED строка0"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v12.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v12.txt",
                new String[] {
                        "REMOVED строка1",
                        "SAME строка0",
                        "REMOVED строка1",
                        "SAME строка0",
                        "REMOVED строка1",
                        "SAME строка0",
                        "REMOVED строка1"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v13.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v13.txt",
                new String[] {
                        "ADDED строка1",
                        "SAME строка0",
                        "ADDED строка1",
                        "SAME строка0",
                        "ADDED строка1",
                        "SAME строка0",
                        "ADDED строка1"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v14.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v14.txt",
                new String[] {
                        "REMOVED строка1",
                        "SAME строка0",
                        "ADDED строка2"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v15.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v15.txt",
                new String[] {
                        "ADDED строка2",
                        "SAME строка0",
                        "REMOVED строка1"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v16.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v16.txt",
                new String[] {
                        "SAME строка1",
                        "SAME строка2",
                        "SAME строка3",
                        "SAME строка0",
                        "SAME строка5",
                        "ADDED строка0",
                        "SAME строка1",
                        "SAME строка2",
                        "SAME строка3",
                        "REMOVED строка4",
                        "SAME строка5"
                }
        ));

        dataList.add(new TestData(
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file1_v17.txt",
                "D:\\Обмен\\javarush\\testFiles\\task19\\1916_file2_v17.txt",
                new String[] {
                        "REMOVED строка1",
                        "SAME строка2",
                        "ADDED строка8",
                        "SAME строка3",
                        "ADDED строка7",
                        "SAME строка4",
                        "REMOVED строка5",
                }
        ));
    }

    @Before
    public void clean() {
        Solution.lines.clear();
    }

    @Test
    public void test1() {
        TestData testData = dataList.get(0);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 1");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test2() {
        TestData testData = dataList.get(1);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 2");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test3() {
        TestData testData = dataList.get(2);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 3");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test4() {
        TestData testData = dataList.get(3);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 4");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test5() {
        TestData testData = dataList.get(4);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 5");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test6() {
        TestData testData = dataList.get(5);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 6");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test7() {
        TestData testData = dataList.get(6);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 7");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test8() {
        TestData testData = dataList.get(7);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 8");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test9() {
        TestData testData = dataList.get(8);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 9");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test10() {
        TestData testData = dataList.get(9);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 10");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test11() {
        TestData testData = dataList.get(10);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 11");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test12() {
        TestData testData = dataList.get(11);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 12");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test13() {
        TestData testData = dataList.get(12);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 13");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test14() {
        TestData testData = dataList.get(13);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 14");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test15() {
        TestData testData = dataList.get(14);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 15");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test16() {
        TestData testData = dataList.get(15);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 16");
        checkLines(testData.expectedStrings);
    }

    @Test
    public void test17() {
        TestData testData = dataList.get(16);
        runTest(testData.fileName1, testData.fileName2);
        printLines("TEST 17");
        checkLines(testData.expectedStrings);
    }

    private void runTest(String... data) {
        StringBuilder builder = new StringBuilder();
        for (String dataSting : data) {
            builder.append(dataSting).append("\n");
        }

        InputStream testIn = new ByteArrayInputStream(builder.toString().getBytes());
        InputStream sysIn = System.in;

        System.setIn(testIn);
        Solution.main(null);
        System.setIn(sysIn);
    }

    private void checkLines(String[] expectedStrings) {
        Object[] actualStrings = Solution.lines.stream()
                .map(item -> item.type + " " + item.line)
                .toArray();
        try {
            Assert.assertArrayEquals(expectedStrings, actualStrings);
        } catch (AssertionError e) {
            Arrays.stream(expectedStrings).forEach(System.err::println);
            throw e;
        }
    }

    private void printLines(String header) {
        System.out.println("\033[92m" + header + ":\n\033[m");
        for (Solution.LineItem lineItem : Solution.lines) {
            System.out.println(lineItem.type + " " + lineItem.line);
        }
        System.out.println();
    }

}
