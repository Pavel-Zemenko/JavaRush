package javarush.cashmachine.tests;

import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.CurrencyManipulator;
import javarush.cashmachine.CurrencyManipulatorFactory;
import javarush.cashmachine.Operation;
import javarush.cashmachine.command.CommandExecutor;
import javarush.cashmachine.exception.InterruptOperationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CommandTest {
    private static InputStream sysIn = System.in;
    private static VirtualConsole console = VirtualConsole.getInstance();

    public CommandTest() {
        System.setIn(console);
    }

    /*
     * Очистка виртуальной консоли.
     */
    @After
    public void cleanUp() {
        while (true) {
            try {
                ConsoleHelper.readString();
            } catch (Exception e) {
                break;
            }
        }
    }

    /*
     * Проверка имеющихся в банкомате денежных средств.
     */
    @After
    public void printInfo() {
        try {
            System.out.println("=====");
            CommandExecutor.execute(Operation.INFO);
        } catch (InterruptOperationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void depositCommandTest() {
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode("UAH");
        int[] denominations = { 100, 50, 20, 10, 5, 2, 1 };
        int amount = 0;

        for (int i = 0; i < 100; i++) {
            int noteIndex = (int) (Math.random() * denominations.length);
            int note = denominations[noteIndex] * ((int) (Math.random() * 3) - 1);
            int qty = (int) (Math.random() * 20) - 10;

            String dataString = String.format("2\nUAH\n%d %d\n", note, qty);
            console.setInputData(dataString);

            if (note > 0 && qty > 0) {
                for (int j = 0; j < qty; j++) {
                    amount += note;
                }
            } else {
                console.setInputData("1 1\n");
                amount++;
            }

            try {
                Operation operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            } catch (InterruptOperationException e) {
                continue;
            }
            Assert.assertEquals(amount, manipulator.getTotalAmount());
            System.out.println(amount + "=" + manipulator.getTotalAmount());
        }

        while (manipulator.hasMoney()) {
            int sum = (int) (Math.random() * 151) * ((int) (Math.random() * 3) - 1);

            System.out.println("-----");
            System.out.println("sum = " + sum);
            System.out.println("amount = " + amount);

            String dataString = String.format("3\nUAH\n%d\nexit\n", sum);
            console.setInputData(dataString);

            try {
                Operation operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            } catch (InterruptOperationException e) {
                console.clear();
                continue;
            }

            Assert.assertEquals(amount -= sum, manipulator.getTotalAmount());
        }
    }

    @Test
    public void withdrawCommandTest() {
        loadCash();

        String[] codes = { "RUR", "UAH", "EUR", "USD" };
        Map<String, CurrencyManipulator> manipulatorMap = new HashMap<>();
        Map<String, Integer> amountMap = new HashMap<>();
        for (String code : codes) {
            manipulatorMap.put(code, CurrencyManipulatorFactory.getManipulatorByCurrencyCode(code));
            amountMap.put(code, manipulatorMap.get(code).getTotalAmount());
        }

        for (int i = 0; i < 1000; i++) {
            String codeKey = codes[(int) (Math.random() * codes.length)];
            int sum = (int) (Math.random() * 250 + 1) * 10;

            System.out.println("-----");
            System.out.println("sum = " + sum + " " + codeKey);
            System.out.println("amount = " + amountMap.get(codeKey) + " " + codeKey);

            console.setInputData(String.format("3\n%s\n%d\nexit\n", codeKey, sum));

            try {
                Operation operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            } catch (InterruptOperationException e) {
                continue;
            } finally {
                console.clear();
                cleanUp();
            }

            amountMap.compute(codeKey, (k, v) -> (v != null) ? v - sum : 0);

            Assert.assertEquals(
                    (int) amountMap.get(codeKey),
                    manipulatorMap.get(codeKey).getTotalAmount());
        }
    }

    /*
     * Загрузка банкомата банкнотами.
     */
    private void loadCash() {
        StringBuilder builder = new StringBuilder();

        int[] rurDenoms = { 5000, 2000, 1000, 500, 200, 100, 50 };
        int[] uahDenoms = { 1000, 500, 200, 100, 50, 20 };
        int[] eurDenoms = { 500, 200, 100, 50, 20, 10 };
        int[] usdDenoms = { 100, 50, 20, 10, 5 };

        Map<String, int[]> cash = new HashMap<>();
        cash.put("RUR", rurDenoms);
        cash.put("UAH", uahDenoms);
        cash.put("EUR", eurDenoms);
        cash.put("USD", usdDenoms);

        for (Map.Entry<String, int[]> entry : cash.entrySet()) {
            for (int d : entry.getValue()) {
                builder.append("2\n");
                builder.append(entry.getKey()).append("\n");
                builder.append(String.format("%d %d\n", d, 500));
            }
        }

        builder.append("1\n");
        builder.append("exit\n");

        console.setInputData(builder.toString());

        while (true) {
            try {
                Operation operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
                System.out.println();
            } catch (InterruptOperationException e) {
                break;
            }
        }
    }

}
