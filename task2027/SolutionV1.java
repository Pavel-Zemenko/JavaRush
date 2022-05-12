package javarush.task2027;

import java.util.ArrayList;
import java.util.List;

public class SolutionV1 {
    public static void main(String[] args) throws Exception {
        int[][] crossword = new int[][] {
                {'f', 'd', 'e', 'r', 'l', 'k'},
                {'u', 's', 'a', 'm', 'e', 'o'},
                {'l', 'n', 'g', 'r', 'o', 'v'},
                {'m', 'l', 'p', 'r', 'r', 'h'},
                {'p', 'o', 'e', 'e', 'j', 'j'}
        };
        detectAllWords(crossword, "home", "same").forEach(System.out::println);
        // Ожидаемый результат:
        // home - (5, 3) - (2, 0)
        // same - (1, 1) - (4, 1)
        System.out.println();
    }

    public static List<Word> detectAllWords(int[][] crossword, String... words) {
        List<Word> list = new ArrayList<>();
        for (String word : words) {
            for (int y = 0; y < crossword.length; y++) {
                for (int x = 0; x < crossword[0].length; x++) {
                    char ch = (char) crossword[y][x];
                    if (word.equals(String.valueOf(ch))) {
                        addWord(x, y, x, y, word, list);
                    }
                    else if (word.charAt(0) == ch) {
                        searchWord(crossword, x, y, word, list);
                    }
                }
            }
        }
        return list;
    }

    /*
     * Выполняет поиск слова word в кроссворде crossword.
     * Поиск начинается из ячейки с индексами startX и startY и продвигается в одном из направлений,
     * пока не будет онаружено искомое слово или достигнута граница кроссворда.
     * Во время поиска последовательно проверяются все восемь направлений
     */
    private static void searchWord(int[][] crossword, int startX, int startY, String word, List<Word> list) {
        for (Direction direction : Direction.values()) {
            StringBuilder tempStr = new StringBuilder();
            for (int x = startX, y = startY; checkBorders(crossword, x, y); y = direction.nextY(y), x = direction.nextX(x)) {
                tempStr.append((char) crossword[y][x]);
                if (!word.startsWith(tempStr.toString())) {
                    break;
                }
                else if (tempStr.toString().equals(word)) {
                    addWord(startX, startY, x, y, word, list);
                    break;
                }
            }
        }
    }

    /*
     * Проверяет действительность индексов x и y в массиве crossword
     */
    private static boolean checkBorders(int[][] crossword, int x, int y) {
        return x >= 0 && x < crossword[0].length && y >= 0 && y < crossword.length;
    }

    /*
     * Преобразует слово word в объект Word и добавляет его в список list
     */
    private static void addWord(int startX, int startY, int endX, int endY, String word, List<Word> list) {
        Word w = new Word(word);
        w.setStartPoint(startX, startY);
        w.setEndPoint(endX, endY);
        list.add(w);
    }

    /*
     * Перечисление направлений ориентации строк в кроссводре
     */
    private enum Direction {
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP(0, -1),
        DOWN(0, 1),
        RIGHT_UP(1, -1),
        RIGHT_DOWN(1, 1),
        LEFT_UP(-1, -1),
        LEFT_DOWN(-1, 1);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int nextX(int x) {
            return x + dx;
        }

        public int nextY(int y) {
            return y + dy;
        }
    }

    public static class Word {
        private String text;
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        public Word(String text) {
            this.text = text;
        }

        public void setStartPoint(int i, int j) {
            startX = i;
            startY = j;
        }

        public void setEndPoint(int i, int j) {
            endX = i;
            endY = j;
        }

        @Override
        public String toString() {
            return String.format("%s - (%d, %d) - (%d, %d)", text, startX, startY, endX, endY);
        }
    }

}
