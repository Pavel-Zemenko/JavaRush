package javarush.cashmachine.command;

import javarush.cashmachine.CashMachine;
import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.exception.InterruptOperationException;

import java.util.ResourceBundle;

class ExitCommand implements Command {
    private final String baseName = CashMachine.class.getPackage().getName()
            + ".resources.exit_en";
    private ResourceBundle res = ResourceBundle.getBundle(baseName);

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("exit.question.y.n"));
        if (ConsoleHelper.readString().toUpperCase().equals("Y")) {
            ConsoleHelper.writeMessage(res.getString("thank.message"));
        }
    }
}
