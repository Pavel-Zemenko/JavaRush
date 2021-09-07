package javarush.probability.statistic;

import javarush.probability.command.Command;
import javarush.probability.entity.Dice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Класс предназначен для составления статистики по результатам
 * вычисления выражения для каждой из комбинаций игровых кубиков.
 * Комбинации значений, выпавших на кубиках, последовательно генерируются;
 * выпавшие значения подставляются в исходное выражение, представленное
 * объектом Command, и вычисляется его значение. Результаты вычислений
 * накапливаются в отображении, на основе данных которого строится
 * сводка вероятностей.
 */
public class StatisticManager {
    public static Map<Integer, Integer> getStatistics(List<Dice> dices, Command command) {
        Map<Integer, Integer> resultMap = new TreeMap<>();
        if (!dices.isEmpty()) {
            dices.get(0).setValue(0);    // нужно только для старта перебора возможных комбинаций кубиков
            while (generator(dices)) {
                int result = command.calculate();
                resultMap.computeIfPresent(result, (k, v) -> v + 1);
                resultMap.putIfAbsent(result, 1);
            }
        } else {
            resultMap.put(command.calculate(), 1);
        }
        return resultMap;
    }

    private static boolean generator(List<Dice> dices) {
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(i).next()) {
                return true;
            }
            dices.get(i).reset();
        }
        return false;
    }

    public static void printStatistics(Map<Integer, Integer> map) {
        int totalSum = map.values().stream()
                .reduce((v1, v2) -> v1 + v2)
                .orElse(0);

        map.forEach((k, v) -> System.out.printf("%d %.2f%s",
                k, ((double) v / totalSum) * 100, System.lineSeparator()));
    }
}
