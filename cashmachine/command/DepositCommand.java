package cashmachine.cashmachine.command;

import cashmachine.cashmachine.ConsoleHelper;
import cashmachine.cashmachine.CurrencyManipulator;
import cashmachine.cashmachine.CurrencyManipulatorFactory;
import cashmachine.cashmachine.exception.InterruptOperationException;

class DepositCommand implements Command {
    @Override
    public void execute() throws InterruptOperationException {
        String currencyCode = ConsoleHelper.askCurrencyCode();
        String[] data = ConsoleHelper.getValidTwoDigits(currencyCode);
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
        manipulator.addAmount(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
}