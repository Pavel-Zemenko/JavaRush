package javarush.cashmachine.command;

import javarush.cashmachine.CashMachine;
import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.CurrencyManipulator;
import javarush.cashmachine.CurrencyManipulatorFactory;
import javarush.cashmachine.exception.InterruptOperationException;
import javarush.cashmachine.exception.NotEnoughMoneyException;

import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

class WithdrawCommand implements Command {
    private final String baseName = CashMachine.RESOURCE_PATH + ".withdraw_en";
    private ResourceBundle res = ResourceBundle.getBundle(baseName);

    @Override
    public void execute() throws InterruptOperationException {
        String currencyCode;
        while ((currencyCode = ConsoleHelper.askCurrencyCode()) == null) {
        }

        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);

        while (true) {
            ConsoleHelper.writeMessage(res.getString("specify.amount"));

            String input = ConsoleHelper.readString();
            if (!input.matches("\\d+") || input.equals("0")) {
                ConsoleHelper.writeMessage(res.getString("specify.not.empty.amount"));
                continue;
            }

            try {
                int expectedAmount = Integer.parseInt(input);
                if (manipulator.isAmountAvailable(expectedAmount)) {
                    ConsoleHelper.writeMessage(res.getString("before"));
                    Map<Integer, Integer> issue = new TreeMap<>(Comparator.reverseOrder());
                    issue.putAll(manipulator.withdrawAmount(expectedAmount));
                    issue.forEach((k, v) ->
                            ConsoleHelper.writeMessage(String.format("\t%d - %d", k, v)));
                    ConsoleHelper.writeMessage(String.format(res.getString("success.format"),
                            expectedAmount, manipulator.getCurrencyCode()));
                    return;
                } else
                    ConsoleHelper.writeMessage(res.getString("not.enough.money"));
            } catch (NotEnoughMoneyException e) {
                ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));
            }
        }
    }
}
