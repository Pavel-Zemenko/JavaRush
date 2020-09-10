package cashmachine.cashmachine;

import cashmachine.cashmachine.command.CommandExecutor;
import cashmachine.cashmachine.exception.InterruptOperationException;

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
