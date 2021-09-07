package javarush.probability.parser;

import javarush.probability.command.Add;
import javarush.probability.command.Com;
import javarush.probability.command.Command;
import javarush.probability.command.Mul;
import javarush.probability.command.Sub;
import javarush.probability.entity.Dice;
import javarush.probability.entity.Literal;
import javarush.probability.entity.Operand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Класс предназначен для считывания выражения из потока ввода
 * и дальшейшего преобразования полученной строки в объект DTO,
 * содержащий данные для обработки в менеждере статистики StatisticManager
 * (список игральных кубиков и вычисляемое математическое выражение
 * в формате Command).
 */
public class ExpressionParser {
    public static DTO parseExpression() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return construct(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static DTO construct(String expr) {
        Translator translator = new Translator(expr);
        List<String> rpn = translator.translateToRPN();

        List<Dice> dices = new ArrayList<>();
        Stack<Operand> stack = new Stack<>();

        for (String s : rpn) {
            if (s.matches("d\\d+")) {
                int sides = Integer.parseInt(s.substring(1));
                Dice dice = new Dice(sides);
                stack.push(dice);
                dices.add(dice);
            }
            else if (s.matches("[>+\\-*]") && !stack.isEmpty()) {
                Operand op2 = stack.pop();
                Operand op1 = stack.pop();
                switch (s) {
                    case ">" : stack.push(new Com(op1, op2));
                        break;
                    case "+" : stack.push(new Add(op1, op2));
                        break;
                    case "-" : stack.push(new Sub(op1, op2));
                        break;
                    case "*" : stack.push(new Mul(op1, op2));
                        break;
                }
            }
            else {
                stack.push(new Literal(Integer.parseInt(s)));
            }
        }
        return new DTO(dices, (Command) stack.pop());
    }
}
