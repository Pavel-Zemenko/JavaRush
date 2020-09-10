package javarush.cashmachine.command;

import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.CurrencyManipulator;
import javarush.cashmachine.CurrencyManipulatorFactory;
import javarush.cashmachine.exception.InterruptOperationException;

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
