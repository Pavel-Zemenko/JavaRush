package javarush.cashmachine.tests;

import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.Operation;
import javarush.cashmachine.command.CommandExecutor;
import javarush.cashmachine.exception.InterruptOperationException;
import org.junit.After;
import org.junit.Test;

import java.io.InputStream;

public class DebugTest {
    private static InputStream sysIn = System.in;
    private static VirtualConsole console = VirtualConsole.getInstance();

    public DebugTest() {
        System.setIn(console);
    }

    /*
     * Очистка виртуальной консоли.
     */
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
    public void printInfo() {
        login();
        try {
            System.out.println("=====");
            CommandExecutor.execute(Operation.INFO);
        } catch (InterruptOperationException e) {
            e.printStackTrace();
        }
    }

    /*
     * Авторизация в банкомате.
     */
    private void login() {
        cleanUp();
        console.setInputData("123456789012\n1234\n");
        try {
            CommandExecutor.execute(Operation.LOGIN);
        } catch (InterruptOperationException e) {
            throw new RuntimeException("Authorization failed.");
        }
    }

    @Test
    public void withdrawDebug() throws InterruptOperationException {
        login();
        String[] inputData = {"5000 10\n", "7 10\n", "5 10\n", "1 20\n"};
        Operation operation;

        for (String data : inputData) {
            console.setInputData("2\nRUR\n" + data);
            operation = ConsoleHelper.askOperation();
            CommandExecutor.execute(operation);
        }

        console.setInputData("3\nRUR\n24\n");
        operation = ConsoleHelper.askOperation();
        CommandExecutor.execute(operation);
    }

}
