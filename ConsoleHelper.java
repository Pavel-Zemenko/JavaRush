package cashmachine;

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
                return inputData.split(" ");
            }
            writeMessage("Данные введены некорректно, повторите ввод.");
        }
    }

    public static Operation askOperation() {
        while (true) {
            writeMessage("Введите код операции: 1 - INFO, 2 - DEPOSIT, 3 - WITHDRAW, 4 - EXIT");
            try {
                int ordinal = Integer.parseInt(readString());
                return Operation.getAllowableOperationByOrdinal(ordinal);
            } catch (Exception e) {
                writeMessage("Операция выбрана неверно, повторите ввод.");
            }
        }
    }
}