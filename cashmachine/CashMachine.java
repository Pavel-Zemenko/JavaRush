package javarush.cashmachine;

import javarush.cashmachine.command.CommandExecutor;
import javarush.cashmachine.exception.InterruptOperationException;

public class CashMachine {
    public static final String RESOURCE_PATH =
            CashMachine.class.getPackage().getName() + ".resources";

    public static void main(String[] args) {
        try {
            CommandExecutor.execute(Operation.LOGIN);
            Operation operation;
            do {
                operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            } while (operation != Operation.EXIT);
        } catch (InterruptOperationException e) {
            ConsoleHelper.printExitMessage();
        }
    }
}
