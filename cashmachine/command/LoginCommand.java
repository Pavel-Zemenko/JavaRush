package javarush.cashmachine.command;

import javarush.cashmachine.CashMachine;
import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.exception.InterruptOperationException;

import java.util.ResourceBundle;
import java.util.Set;

class LoginCommand implements Command {
    private final String baseName = CashMachine.class.getPackage().getName()
            + ".resources.verifiedCards";
    private ResourceBundle validCreditCards = ResourceBundle.getBundle(baseName);

    @Override
    public void execute() throws InterruptOperationException {
        Set<String> idSet = validCreditCards.keySet();

        while (true) {
            ConsoleHelper.writeMessage("Введите номер карты и пин-код:");
            String cardId = ConsoleHelper.readString();
            String pin = ConsoleHelper.readString();

            for (String id : idSet) {
                if (id.equals(cardId) && validCreditCards.getString(cardId).equals(pin)) {
                    ConsoleHelper.writeMessage("Добро пожаловать!");
                    return;
                }
            }
            ConsoleHelper.writeMessage("Данные введены некорректно. Повторите ввод.");
        }
    }
}
