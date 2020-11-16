package javarush.task1916;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TEST {

    @Before
    public void clean() {
        Solution.lines.clear();
    }

    @Test
    public void test1() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v1.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v1.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 1");

        String[] expectedStrings = {
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
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test2() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v2.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v2.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 2");

        String[] expectedStrings = {
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
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test3() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v3.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v3.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 3");

        String[] expectedStrings = {
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
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test4() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v4.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v4.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 4");

        String[] expectedStrings = {
                "SAME строка1",
                "ADDED строка0"
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test5() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v5.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v5.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 5");

        String[] expectedStrings = {
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
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test6() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v6.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v6.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 6");

        String[] expectedStrings = {
                "SAME строка1",
                "REMOVED строка0",
                "SAME строка2",
                "ADDED строка3",
                "SAME строка4",
                "REMOVED строка5",
                "SAME строка6",
                "REMOVED строка7"
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test7() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v7.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v7.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 7");

        String[] expectedStrings = {
                "REMOVED строка0",
                "SAME строка1",
                "REMOVED строка2"
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test8() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v8.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v8.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 8");

        String[] expectedStrings = {
                "ADDED строка0",
                "SAME строка2",
                "SAME строка3",
                "REMOVED строка0",
                "SAME строка4"
        };
        checkLines(expectedStrings);
    }

    @Test
    public void test9() throws Exception {
        String fileName1 = "./src/javarush/task1916/testfiles/1916_file1_v9.txt";
        String fileName2 = "./src/javarush/task1916/testfiles/1916_file2_v9.txt";
        runTest(fileName1, fileName2);
        printLines("TEST 9");

        String[] expectedStrings = {
                "REMOVED строка0",
                "SAME строка1"
        };
        checkLines(expectedStrings);
    }

    private void runTest(String ... data) throws IOException {
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
        for (int i = 0; i < expectedStrings.length; i++) {
            Solution.LineItem lineItem = Solution.lines.get(i);
            Assert.assertEquals("lineIndex=" + i,
                    expectedStrings[i], (lineItem.type + " " + lineItem.line));
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
