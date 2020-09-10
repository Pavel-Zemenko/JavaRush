package cashmachine.command;

import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulator;
import cashmachine.CurrencyManipulatorFactory;

class DepositCommand implements Command {
    @Override
    public void execute() {
        String currencyCode = ConsoleHelper.askCurrencyCode();
        String[] data = ConsoleHelper.getValidTwoDigits(currencyCode);
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
        manipulator.addAmount(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
}
