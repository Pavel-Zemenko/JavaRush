package javarush.cashmachine.command;

import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.exception.InterruptOperationException;

class LoginCommand implements Command {
    private final long card = 123456789012L;
    private final long pin = 1234;

    @Override
    public void execute() throws InterruptOperationException {
        while (true) {
            ConsoleHelper.writeMessage("Введите номер карты и пин-код:");
            try {
                long card = Long.parseLong(ConsoleHelper.readString());
                long pin = Long.parseLong(ConsoleHelper.readString());
                if (card < 0 || pin < 0) {
                    throw new NumberFormatException();
                }
                if (card == this.card && pin == this.pin) {
                    ConsoleHelper.writeMessage("Добро пожаловать!");
                    return;
                }
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage("Данные введены некорректно. Повторите ввод.");
            }
        }
    }
}
