package javarush.cashmachine;

import javarush.cashmachine.exception.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws InterruptOperationException {
        String string = "";
        try {
            string = reader.readLine();
            if (string.toUpperCase().contains("EXIT")) {
                throw new InterruptOperationException();
            }
        } catch (IOException e) {
        }
        return string;
    }

    public static String askCurrencyCode() throws InterruptOperationException {
        writeMessage("Выберите валюту: RUR, USD, EUR, UAH");
        String currencyCode = readString().toUpperCase();
        if (currencyCode.matches("RUR|USD|EUR|UAH")) {
            return currencyCode;
        }
        return null;
    }

    public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException {
        writeMessage("Введите номинал банкнот и их количество:");
        String inputData = readString();
        if (inputData.matches("\\d+? \\d+?")) {
            String[] array = inputData.split(" ");
            if (!array[0].startsWith("0") && !array[1].startsWith("0")) {
                return array;
            }
        }
        return null;
    }

    public static Operation askOperation() throws InterruptOperationException {
        while (true) {
            writeMessage("Введите код операции: 1 - INFO, 2 - DEPOSIT, 3 - WITHDRAW, 4 - EXIT");
            try {
                int ordinal = Integer.parseInt(readString());
                return Operation.getAllowableOperationByOrdinal(ordinal);
            } catch (IllegalArgumentException e) {
                writeMessage("Операция выбрана неверно, повторите ввод.");
            }
        }
    }
}
