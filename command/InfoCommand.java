package cashmachine.command;

import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulator;
import cashmachine.CurrencyManipulatorFactory;

class InfoCommand implements Command {
    @Override
    public void execute() {
        String currencyCode = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
        ConsoleHelper.writeMessage(String.valueOf(manipulator.getTotalAmount()));
    }
}
