package javarush.task2027;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TEST {

    @Test
    public void test1() {
        int[][] crossword = new int[][] {
                {'f', 'd', 'e', 'r', 'l', 'k'},
                {'u', 's', 'a', 'm', 'e', 'o'},
                {'l', 'n', 'g', 'r', 'o', 'v'},
                {'m', 'l', 'p', 'r', 'r', 'h'},
                {'p', 'o', 'e', 'e', 'j', 'j'}
        };
        String[] words = { "home", "same" };
        String[] expected = {
                "home - (5, 3) - (2, 0)",
                "same - (1, 1) - (4, 1)"
        };
        assertion(1, crossword, words, expected);
    }

    @Test
    public void test2() {
        int[][] crossword = new int[][] {
                {'a', 'm', 'a', 'r', 'e', 'e'},
                {'m', 's', 'a', 'm', 'e', 'm'},
                {'e', 'a', 'a', 'r', 'o', 'o'},
                {'m', 'm', 'p', 'h', 'o', 'h'},
                {'h', 'r', 'e', 'm', 'o', 'h'}
        };
        String[] words = { "home", "same", "homer" };
        String[] expected = {
                "home - (5, 3) - (5, 0)",
                "home - (5, 4) - (2, 4)",
                "same - (1, 1) - (4, 1)",
                "homer - (5, 4) - (1, 4)"
        };
        assertion(2, crossword, words, expected);
    }

    @Test
    public void test3() {
        int[][] crossword = new int[][] {
                {'r', 'm', 'a', 'r', 'r', 'e'},
                {'m', 'e', 'a', 'e', 'e', 'm'},
                {'s', 'a', 'm', 'e', 's', 'o'},
                {'m', 'o', 'p', 'o', 'o', 'h'},
                {'h', 'r', 'e', 'm', 'h', 'h'}
        };
        String[] words = { "home" };
        String[] expected = {
                "home - (5, 3) - (5, 0)",
                "home - (0, 4) - (3, 1)",
                "home - (4, 4) - (1, 1)",
//                "same - (0, 2) - (3, 2)",
//                "homer - (0, 4) - (4, 0)",
//                "homer - (4, 4) - (0, 0)"
        };
        assertion(3, crossword, words, expected);
    }

    @Test
    public void test4() {
        int[][] crossword = new int[][] {
                {'a', 'b', 'c'},
                {'b', 'd', 'd'},
                {'c', 'd', 'd'}
        };
        String[] words = { "bb", "dd", "c" };
        String[] expected = {
                "bb - (1, 0) - (0, 1)",
                "bb - (0, 1) - (1, 0)",
                "dd - (1, 1) - (2, 1)",
                "dd - (1, 1) - (1, 2)",
                "dd - (1, 1) - (2, 2)",
                "dd - (2, 1) - (1, 1)",
                "dd - (2, 1) - (2, 2)",
                "dd - (2, 1) - (1, 2)",
                "dd - (1, 2) - (2, 2)",
                "dd - (1, 2) - (1, 1)",
                "dd - (1, 2) - (2, 1)",
                "dd - (2, 2) - (1, 2)",
                "dd - (2, 2) - (2, 1)",
                "dd - (2, 2) - (1, 1)",
                "c - (2, 0) - (2, 0)",
                "c - (0, 2) - (0, 2)"
        };
        assertion(4, crossword, words, expected);
    }

    @Test
    public void test5() {
        int[][] crossword = new int[][] {
                {' ', 'd', 'l', 'r', 'o', 'w'},
                {' ', 'r', ' ', ' ', 'r', ' '},
                {' ', 'o', 'd', ' ', 'd', 'd'},
                {'d', 'w', 'o', 'r', 'd', 'r'},
                {'r', 'o', ' ', ' ', 'o', 'o'},
                {'o', 'r', ' ', ' ', ' ', 'w'},
                {'w', 'd', 'r', 'o', 'o', 'w'}
        };
        String[] words = { "world", "word" };
        String[] expected = {
                "world - (5, 0) - (1, 0)",
                "word - (1, 3) - (4, 3)",
                "word - (1, 3) - (1, 0)",
                "word - (1, 3) - (1, 6)",
                "word - (5, 5) - (5, 2)",
                "word - (5, 5) - (2, 2)",
                "word - (0, 6) - (0, 3)"
        };
        assertion(5, crossword, words, expected);
    }

    @Test
    public void test6() {
        int[][] crossword = new int[][] {
                {'a', 'a'},
                {'a', 'a'}
        };
        String[] words = { "a" };
        String[] expected = {
                "a - (0, 0) - (0, 0)",
                "a - (1, 0) - (1, 0)",
                "a - (0, 1) - (0, 1)",
                "a - (1, 1) - (1, 1)"
        };
        assertion(6, crossword, words, expected);
    }

    @Test
    public void test7() {
        int[][] crossword = new int[][] {
                {'a', 'a', 'a'},
                {'a', 'o', 'a'},
                {'a', 'a', 'a'}
        };
        String[] words = { "aaa" };
        String[] expected = {
                "aaa - (0, 0) - (2, 0)",
                "aaa - (0, 0) - (0, 2)",
                "aaa - (2, 0) - (0, 0)",
                "aaa - (2, 0) - (2, 2)",
                "aaa - (0, 2) - (2, 2)",
                "aaa - (0, 2) - (0, 0)",
                "aaa - (2, 2) - (0, 2)",
                "aaa - (2, 2) - (2, 0)"
        };
        assertion(7, crossword, words, expected);
    }

    @Test
    public void test8() {
        int[][] crossword = new int[][] {
                {'x', 'y'},
                {'x', 'y'},
                {'x', 'y'},
                {'x', 'y'}
        };
        String[] words = { "xxxx", "yyyy" };
        String[] expected = {
                "xxxx - (0, 0) - (0, 3)",
                "xxxx - (0, 3) - (0, 0)",
                "yyyy - (1, 0) - (1, 3)",
                "yyyy - (1, 3) - (1, 0)"
        };
        assertion(8, crossword, words, expected);
    }

    private void assertion(int testNumber, int[][] crossword, String[] words, String[] expected) {
//        List<SolutionV1.Word> wordList = SolutionV1.detectAllWords(crossword, words);
//        List<SolutionV2.Word> wordList = SolutionV2.detectAllWords(crossword, words);
        List<SolutionV3.Word> wordList = SolutionV3.detectAllWords(crossword, words);

        String[] actual = new String[wordList.size()];
        System.out.println("TEST #" + testNumber);
        for (int i = 0; i < actual.length; i++) {
            actual[i] = wordList.get(i).toString();
            System.out.println(actual[i]);
        }
        Arrays.sort(expected);
        Arrays.sort(actual);
        Assert.assertArrayEquals(expected, actual);
    }

}
