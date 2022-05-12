package javarush.task2027;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionV2 {
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
            collectWords(list, getHorizontalLines(crossword), word);
            if (word.length() > 1) {
                collectWords(list, getVerticalLines(crossword), word);
                collectWords(list, getDiagonalLines(crossword), word);
            }
        }
        return list;
    }

    /*
     * Выполняет поиск слова word в линиях кроссворда lines.
     * Все найденные совпадения помещаются в список wordList в виде объектов Word
     */
    private static void collectWords(List<Word> wordList, List<Line> lines, String word) {
        for (Line line : lines) {
            wordList.addAll(line.scan(word));
        }
    }

    /*
     * Возвращает список горизонтальных линий переданного кроссворда crossword
     */
    private static List<Line> getHorizontalLines(int[][] crossword) {
        List<Line> resultList = new ArrayList<>();
        for (int y = 0; y < crossword.length; y++) {
            Line line = new Line();
            for (int x = 0; x < crossword[0].length; x++) {
                line.addChar((char) crossword[y][x], x, y);
            }
            resultList.add(line);
        }
        return resultList;
    }

    /*
     * Возвращает список вертикальных линий переданного кроссворда crossword
     */
    private static List<Line> getVerticalLines(int[][] crossword) {
        List<Line> resultList = new ArrayList<>();
        for (int x = 0; x < crossword[0].length; x++) {
            Line line = new Line();
            for (int y = 0; y < crossword.length; y++) {
                line.addChar((char) crossword[y][x], x, y);
            }
            resultList.add(line);
        }
        return resultList;
    }

    /*
     * Возвращает список диагональных линий переданного кроссворда crossword
     */
    private static List<Line> getDiagonalLines(int[][] crossword) {
        List<Line> resultList = new ArrayList<>();
        // поиск по оси Y (сверху вниз, слева направо)
        for (int i = 0; i < crossword.length; i++) {
            Line line = new Line();
            for (int x = 0, y = i; x < crossword[0].length && y < crossword.length; x++, y++) {
                line.addChar((char) crossword[y][x], x, y);
            }
            resultList.add(line);
        }
        // поиск по оси X (сверху вниз, слева направо)
        for (int i = 1; i < crossword[0].length; i++) {
            Line line = new Line();
            for (int x = i, y = 0; x < crossword[0].length && y < crossword.length; x++, y++) {
                line.addChar((char) crossword[y][x], x, y);
            }
            resultList.add(line);
        }
        // поиск по оси Y (сверху вниз, справа налево)
        for (int i = 0; i < crossword.length; i++) {
            Line line = new Line();
            for (int x = crossword[0].length - 1, y = i; x >= 0 && y < crossword.length; x--, y++) {
                line.addChar((char) crossword[y][x], x, y);
            }
            resultList.add(line);
        }
        // поиск по оси X (сверху вниз, справа налево)
        for (int i = crossword[0].length - 2; i >= 0; i--) {
            Line line = new Line();
            for (int x = i, y = 0; x >= 0 && y < crossword.length; x--, y++) {
                line.addChar((char) crossword[y][x], x, y);
            }
            resultList.add(line);
        }
        return resultList;
    }

    /*
     * Точка с координатами ячейки кроссворда
     */
    private static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /*
     * Линия кроссворда (вертикальная, горизонтальная или диагональная).
     * Содержит символы, из которых состоит линия, а также их координаты в сетке кроссворда
     */
    private static class Line {
        private final StringBuilder builder = new StringBuilder();
        private final List<Point> charPoints = new ArrayList<>();

        /*
         * Добавляет новый символ в линию, а также его координаты
         */
        public void addChar(char ch, int x, int y) {
            builder.append(ch);
            charPoints.add(new Point(x, y));
        }

        /*
         * Выполняет в линии поиск всех слов из списка word.
         * Поиск в линии производится в прямом и обратном направлениях
         */
        public List<Word> scan(String word) {
            List<Word> resultList = new ArrayList<>();
            Pattern straight = Pattern.compile(word);
            Pattern reversed = Pattern.compile(new StringBuilder(word).reverse().toString());
            Matcher matcher = straight.matcher(builder.toString());
            while (matcher.find()) {
                Point start = charPoints.get(matcher.start());
                Point end = charPoints.get(matcher.end() - 1);
                addWord(start, end, word, resultList);
            }
            if (word.length() > 1) {
                matcher.reset();
                matcher.usePattern(reversed);
                while (matcher.find()) {
                    Point start = charPoints.get(matcher.start());
                    Point end = charPoints.get(matcher.end() - 1);
                    addWord(end, start, word, resultList);
                }
            }
            return resultList;
        }

        /*
         * Преобразует слово word в объект Word и добавляет его в список list.
         */
        private void addWord(Point start, Point end, String word, List<Word> list) {
            Word w = new Word(word);
            w.setStartPoint(start.getX(), start.getY());
            w.setEndPoint(end.getX(), end.getY());
            list.add(w);
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
