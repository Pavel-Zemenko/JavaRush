package javarush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String string = "";
        try {
            string = reader.readLine();
        } catch (IOException e) {
        }
        return string;
    }

    public static String askCurrencyCode() {
        while (true) {
            writeMessage("Выберите валюту: RUR, USD, UAH");
            String currencyCode = readString().toUpperCase();
            if (currencyCode.matches("RUR|USD|UAH")) {
                return currencyCode;
            }
            writeMessage("Валюта выбрана неверно, повторите ввод.");
        }
    }

    public static String[] getValidTwoDigits(String currencyCode) {
        while (true) {
            writeMessage("Введите номинал банкнот и их количество:");
            String inputData = readString();
            if (inputData.matches("\\d+? \\d+?")) {
                String[] array = inputData.split(" ");
                if (!array[0].startsWith("0") && !array[1].startsWith("0")) {
                    return array;
                }
            }
            writeMessage("Данные введены некорректно, повторите ввод.");
        }
    }
}