package javarush.cashmachine.command;

import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.CurrencyManipulator;
import javarush.cashmachine.CurrencyManipulatorFactory;
import javarush.cashmachine.exception.InterruptOperationException;
import javarush.cashmachine.exception.NotEnoughMoneyException;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

class WithdrawCommand implements Command {
    @Override
    public void execute() throws InterruptOperationException {
        String currencyCode = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);

        while (true) {
            ConsoleHelper.writeMessage("Введите сумму:");

            String input = ConsoleHelper.readString();
            if (!input.matches("\\d+")) {
                ConsoleHelper.writeMessage("Сумма введена неверно.");
                continue;
            }

            try {
                int expectedAmount = Integer.parseInt(input);
                if (manipulator.isAmountAvailable(expectedAmount)) {
                    Map<Integer, Integer> issue = new TreeMap<>(Comparator.reverseOrder());
                    issue.putAll(manipulator.withdrawAmount(expectedAmount));
                    issue.forEach((k, v) ->
                            ConsoleHelper.writeMessage(String.format("\t%d - %d", k, v)));
                    ConsoleHelper.writeMessage("Получите, распишитесь!");
                    return;
                }
            } catch (NotEnoughMoneyException e) {
                ConsoleHelper.writeMessage("Денег нет, но вы держитесь!");
            }
        }
    }
}
