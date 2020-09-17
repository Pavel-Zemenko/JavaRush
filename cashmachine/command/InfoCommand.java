package javarush.cashmachine.command;

import javarush.cashmachine.CashMachine;
import javarush.cashmachine.ConsoleHelper;
import javarush.cashmachine.CurrencyManipulator;
import javarush.cashmachine.CurrencyManipulatorFactory;

import java.util.Collection;
import java.util.ResourceBundle;

class InfoCommand implements Command {
    private final String baseName = CashMachine.class.getPackage().getName()
            + ".resources.info_en";
    private ResourceBundle res = ResourceBundle.getBundle(baseName);

    @Override
    public void execute() {
        boolean hasMoney = false;
        Collection<CurrencyManipulator> manipulators =
                CurrencyManipulatorFactory.getAllCurrencyManipulators();

        ConsoleHelper.writeMessage(res.getString("before"));

        for (CurrencyManipulator manipulator : manipulators) {
            if (manipulator.hasMoney()) {
                hasMoney = true;
                break;
            }
        }

        if (hasMoney) {
            CurrencyManipulatorFactory.getAllCurrencyManipulators()
                    .forEach(m -> ConsoleHelper.writeMessage(
                            m.getCurrencyCode() + " - " + m.getTotalAmount()));
        } else {
            ConsoleHelper.writeMessage(res.getString("no.money"));
        }
    }
}
