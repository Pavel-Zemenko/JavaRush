package javarush.cashmachine.command;

import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.exception.InterruptOperationException;

class ExitCommand implements Command {
    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage("Вы действительно хотите выйти? (Y/N)");
        if (ConsoleHelper.readString().toUpperCase().equals("Y")) {
            ConsoleHelper.writeMessage("Приходите к нам ещё!");
        }
    }
}
