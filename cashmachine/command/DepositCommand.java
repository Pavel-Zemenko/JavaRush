package javarush.cashmachine.command;

import javarush.cashmachine.CashMachine;
import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.CurrencyManipulator;
import javarush.cashmachine.CurrencyManipulatorFactory;
import javarush.cashmachine.exception.InterruptOperationException;

import java.util.ResourceBundle;

class DepositCommand implements Command {
    private final String baseName = CashMachine.RESOURCE_PATH + ".deposit_en";
    private ResourceBundle res = ResourceBundle.getBundle(baseName);

    @Override
    public void execute() throws InterruptOperationException {
        String currencyCode;
        while ((currencyCode = ConsoleHelper.askCurrencyCode()) == null) {
            ConsoleHelper.writeMessage(res.getString("invalid.data"));
        }

        String[] data;
        while ((data = ConsoleHelper.getValidTwoDigits(currencyCode)) == null) {
            ConsoleHelper.writeMessage(res.getString("invalid.data"));
        }

        ConsoleHelper.writeMessage(res.getString("before"));
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
        int denom = Integer.parseInt(data[0]);
        int count = Integer.parseInt(data[1]);
        manipulator.addAmount(denom, count);
        ConsoleHelper.writeMessage(
                String.format(res.getString("success.format"), denom * count, currencyCode));
    }
}
