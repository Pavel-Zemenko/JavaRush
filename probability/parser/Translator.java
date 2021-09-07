package javarush.probability.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс обеспечивает работу парсера ExpressionParser.
 * Его предназначение - преобразование выражения в постфиксную запись.
 * Выражение в постфиксной записи возвращается в виде списка строк,
 * каждый элемент которого представляет оператор либо операнд.
 */
public class Translator {
    private Stack<Character> stack;
    private String input;
    private List<String> rpn;

    public Translator(String input) {
        this.input = input;
        this.stack = new Stack<>();
        this.rpn = new ArrayList<>();
    }

    public List<String> translateToRPN() {
        Pattern pattern = Pattern.compile("[()*+\\->]|\\d+|d\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            switch (match) {
                case ">": gotOperator(match.charAt(0), 1);
                    break;
                case "+":
                case "-": gotOperator(match.charAt(0), 2);
                    break;
                case "*": gotOperator(match.charAt(0), 3);
                    break;
                case "(": stack.push(match.charAt(0));
                    break;
                case ")": gotParen();
                    break;
                default : rpn.add(match);
                    break;
            }
        }
        while (!stack.isEmpty()) {
            rpn.add(String.valueOf(stack.pop()));
        }
        return rpn;
    }

    private void gotOperator(char opThis, int prec1) {
        while (!stack.isEmpty()) {
            char opTop = stack.pop();
            if (opTop == '(') {
                stack.push(opTop);
                break;
            } else {
                int prec2;

                if (opTop == '>') {
                    prec2 = 1;
                }
                else if (opTop == '+' || opTop == '-') {
                    prec2 = 2;
                }
                else {
                    prec2 = 3;
                }

                if (prec2 < prec1) {
                    stack.push(opTop);
                    break;
                } else {
                    rpn.add(String.valueOf(opTop));
                }
            }
        }
        stack.push(opThis);
    }

    private void gotParen() {
        while (!stack.isEmpty()) {
            char chx = stack.pop();
            if (chx == '(')
                break;
            else
                rpn.add(String.valueOf(chx));
        }
    }
}
