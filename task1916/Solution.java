package javarush.task1916;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
Отслеживаем изменения
*/

public class Solution {
    public static List<LineItem> lines = new ArrayList<>();

    public static void main(String[] args) {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            List<String> lines1 = readFileToList(consoleReader.readLine());
            List<String> lines2 = readFileToList(consoleReader.readLine());
            mergeToLines(lines1, lines2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readFileToList(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.ready()) {
                list.add(reader.readLine());
            }
        }
        return list;
    }

    private static void mergeToLines(List<String> list1, List<String> list2) {
        for (int i = 0; i < Math.max(list1.size(), list2.size()); i++) {
            String s1 = i < list1.size() ? list1.get(i) : null;
            String s2 = i < list2.size() ? list2.get(i) : null;

            if (s1 == null) {
                lines.add(new LineItem(Type.ADDED, s2));
                list1.add(i, "       ");
                break;
            }

            if (s2 == null) {
                lines.add(new LineItem(Type.REMOVED, s1));
                list2.add(i, "       ");
                break;
            }

            if (s1.equals(s2)) {
                lines.add(new LineItem(Type.SAME, s1));
            } else {
                if (i + 1 < list2.size() && s1.equals(list2.get(i + 1))) {
                    lines.add(new LineItem(Type.ADDED, s2));
                    list1.add(i, "       ");
                } else {
                    lines.add(new LineItem(Type.REMOVED, s1));
                    list2.add(i, "       ");
                }
            }
        }
    }

    public static enum Type {
        ADDED,        // добавлена новая строка
        REMOVED,      // удалена строка
        SAME          // без изменений
    }

    public static class LineItem {
        public Type type;
        public String line;

        public LineItem(Type type, String line) {
            this.type = type;
            this.line = line;
        }
    }
}
