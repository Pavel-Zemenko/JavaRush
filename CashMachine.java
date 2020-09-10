package cashmachine;

public class CashMachine {
    public static void main(String[] args) {
        String currencyCode = ConsoleHelper.askCurrencyCode();
        String[] data = ConsoleHelper.getValidTwoDigits(currencyCode);
        CurrencyManipulator manipulator =
                CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);

        manipulator.addAmount(Integer.parseInt(data[0]), Integer.parseInt(data[1]));

        System.out.println(manipulator.getTotalAmount());
    }
}