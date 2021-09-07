package javarush.probability;

import javarush.probability.parser.DTO;

import java.util.Locale;
import java.util.Map;

import static javarush.probability.parser.ExpressionParser.parseExpression;
import static javarush.probability.statistic.StatisticManager.getStatistics;
import static javarush.probability.statistic.StatisticManager.printStatistics;


public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        DTO dto = parseExpression();
        Map<Integer, Integer> statMap = getStatistics(dto.getDices(), dto.getCommand());
        printStatistics(statMap);
    }
}
