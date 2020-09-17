package javarush.cashmachine;

import javarush.cashmachine.exception.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final String BASENAME = CashMachine.class.getPackage().getName()
            + ".resources.common_en";
    private static ResourceBundle res = ResourceBundle.getBundle(BASENAME);

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws InterruptOperationException {
        String string = "";
        try {
            string = reader.readLine();
            if (string.toUpperCase().contains("EXIT")) {
                writeMessage(res.getString("the.end"));
                throw new InterruptOperationException();
            }
        } catch (IOException e) {
        }
        return string;
    }

    public static String askCurrencyCode() throws InterruptOperationException {
        writeMessage(res.getString("choose.currency.code"));
        String currencyCode = readString().toUpperCase();
        if (currencyCode.matches("RUR|USD|EUR|UAH")) {
            return currencyCode;
        }
        return null;
    }

    public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException {
        writeMessage(String.format(res.getString("choose.denomination.and.count.format"), currencyCode));
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
            writeMessage(res.getString("choose.operation"));
            writeMessage(String.format("1 - %s, 2 - %s, 3 - %s, 4 - %s",
                    res.getString("operation.INFO"),
                    res.getString("operation.DEPOSIT"),
                    res.getString("operation.WITHDRAW"),
                    res.getString("operation.EXIT")));
            try {
                int ordinal = Integer.parseInt(readString());
                return Operation.getAllowableOperationByOrdinal(ordinal);
            } catch (IllegalArgumentException e) {
                writeMessage(res.getString("invalid.data"));
            }
        }
    }
}
