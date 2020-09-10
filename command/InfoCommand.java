package cashmachine.command;

import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulatorFactory;

class InfoCommand implements Command {
    @Override
    public void execute() {
        CurrencyManipulatorFactory.getAllCurrencyManipulators()
                .forEach(m -> ConsoleHelper.writeMessage(
                        m.getCurrencyCode() + " - " + m.getTotalAmount()));
    }
}
