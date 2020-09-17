package javarush.cashmachine.command;

import javarush.cashmachine.CashMachine;
import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.exception.InterruptOperationException;

import java.util.ResourceBundle;
import java.util.Set;

class LoginCommand implements Command {
    private final String baseNameRes = CashMachine.class.getPackage().getName()
            + ".resources.login_en";
    private ResourceBundle res = ResourceBundle.getBundle(baseNameRes);

    private final String baseNameCards = CashMachine.class.getPackage().getName()
            + ".resources.verifiedCards";
    private ResourceBundle validCreditCards = ResourceBundle.getBundle(baseNameCards);

    @Override
    public void execute() throws InterruptOperationException {
        Set<String> idSet = validCreditCards.keySet();

        ConsoleHelper.writeMessage(res.getString("before"));

        while (true) {
            ConsoleHelper.writeMessage(res.getString("specify.data"));
            String cardId = ConsoleHelper.readString();
            String pin = ConsoleHelper.readString();

            for (String id : idSet) {
                if (id.equals(cardId)) {
                    while (!validCreditCards.getString(cardId).equals(pin)) {
                        ConsoleHelper.writeMessage(res.getString("try.again.or.exit"));
                        pin = ConsoleHelper.readString();
                    }
                    ConsoleHelper.writeMessage(String.format(res.getString("success.format"), cardId));
                    return;
                }
            }
            ConsoleHelper.writeMessage(String.format(res.getString("not.verified.format"), cardId));
            ConsoleHelper.writeMessage(res.getString("try.again.with.details"));
        }
    }
}
