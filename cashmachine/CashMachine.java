package javarush.cashmachine;

import javarush.cashmachine.command.CommandExecutor;
import javarush.cashmachine.exception.InterruptOperationException;

public class CashMachine {
    public static void main(String[] args) {
        try {
            Operation operation;
            do {
                operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            } while (operation != Operation.EXIT);
        } catch (InterruptOperationException e) {
            e.printStackTrace();
        }
    }
}
