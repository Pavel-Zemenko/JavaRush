package cashmachine.cashmachine.command;

import cashmachine.cashmachine.Operation;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static Map<Operation, Command> allKnownCommandsMap = new HashMap<>();

    private CommandExecutor() {}

    static {
        allKnownCommandsMap.put(Operation.INFO, new InfoCommand());
        allKnownCommandsMap.put(Operation.DEPOSIT, new DepositCommand());
        allKnownCommandsMap.put(Operation.WITHDRAW, new WithdrawCommand());
        allKnownCommandsMap.put(Operation.EXIT, new ExitCommand());
    }

    public static final void execute(Operation operation) {
        allKnownCommandsMap.get(operation).execute();
    }
}
