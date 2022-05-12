package javarush.task2027;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolutionV3 {
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
        List<Word> result = new ArrayList<>();
        List<Line> horizontalLines = new ArrayList<>();
        List<Line> allLines = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (direction == Direction.RIGHT) {
                horizontalLines.addAll(readLines(crossword, direction));
            }
            allLines.addAll(readLines(crossword, direction));
        }
        for (String word : words) {
            if (word.length() > 1) {
                collectWords(result, allLines, word);
            } else {
                collectWords(result, horizontalLines, word);
            }
        }
        return result;
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
     * Возвращает список линий кроссворда crossword в заданном направлении direction
     */
    private static List<Line> readLines(int[][] crossword, Direction direction) {
        List<Line> lines = new ArrayList<>();
        for (Point point : direction.getStartPoints(crossword)) {
            Line line = new Line();
            for (int x = point.x, y = point.y; checkBorders(crossword, x, y); y = direction.nextY(y), x = direction.nextX(x)) {
                line.addChar((char) crossword[y][x], x, y);
            }
            lines.add(line);
        }
        return lines;
    }

    /*
     * Проверяет действительность индексов x и y в массиве crossword
     */
    private static boolean checkBorders(int[][] crossword, int x, int y) {
        return x >= 0 && x < crossword[0].length && y >= 0 && y < crossword.length;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
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
            Matcher matcher = straight.matcher(builder.toString());
            while (matcher.find()) {
                Point start = charPoints.get(matcher.start());
                Point end = charPoints.get(matcher.end() - 1);
                addWord(start, end, word, resultList);
            }
            return resultList;
        }

        /*
         * Преобразует слово word в объект Word и добавляет его в список list
         */
        private void addWord(Point start, Point end, String word, List<Word> list) {
            Word w = new Word(word);
            w.setStartPoint(start.x, start.y);
            w.setEndPoint(end.x, end.y);
            list.add(w);
        }
    }

    /*
     * Перечисление направлений ориентации строк в кроссводре
     */
    private enum Direction {
        RIGHT(1, 0, crossword -> leftSide(crossword)),
        LEFT(-1, 0, crossword -> rightSide(crossword)),
        UP(0, -1, crossword -> bottomSide(crossword)),
        DOWN(0, 1, crossword -> topSide(crossword)),
        RIGHT_UP(1, -1, crossword -> leftBottomSide(crossword)),
        RIGHT_DOWN(1, 1, crossword -> leftTopSide(crossword)),
        LEFT_UP(-1, -1, crossword -> rightBottomSide(crossword)),
        LEFT_DOWN(-1, 1, crossword -> rightTopSide(crossword));

        private final int dx;
        private final int dy;
        private final Function<int[][], Set<Point>> function;

        Direction(int dx, int dy, Function<int[][], Set<Point>> function) {
            this.dx = dx;
            this.dy = dy;
            this.function = function;
        }

        private static Set<Point> leftSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            for (int i = 0; i < crossword.length; i++) {
                points.add(new Point(0, i));
            }
            return points;
        }

        private static Set<Point> rightSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            for (int i = 0; i < crossword.length; i++) {
                points.add(new Point(crossword[0].length - 1, i));
            }
            return points;
        }

        private static Set<Point> topSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            for (int i = 0; i < crossword[0].length; i++) {
                points.add(new Point(i, 0));
            }
            return points;
        }

        private static Set<Point> bottomSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            for (int i = 0; i < crossword[0].length; i++) {
                points.add(new Point(i, crossword.length - 1));
            }
            return points;
        }

        private static Set<Point> leftBottomSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            points.addAll(leftSide(crossword));
            points.addAll(bottomSide(crossword));
            return points;
        }

        private static Set<Point> leftTopSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            points.addAll(leftSide(crossword));
            points.addAll(topSide(crossword));
            return points;
        }

        private static Set<Point> rightBottomSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            points.addAll(rightSide(crossword));
            points.addAll(bottomSide(crossword));
            return points;
        }

        private static Set<Point> rightTopSide(int[][] crossword) {
            Set<Point> points = new HashSet<>();
            points.addAll(rightSide(crossword));
            points.addAll(topSide(crossword));
            return points;
        }

        /*
         * Определяет координаты начальных позиций для поиска линий в заданном направлении для матрицы crossword.
         * Возвращает множество координат ячеек начальных позиций, представленных объектами Point
         */
        public Set<Point> getStartPoints(int[][] crossword) {
            return function.apply(crossword);
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
