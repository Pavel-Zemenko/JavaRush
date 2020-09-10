package cashmachine.command;

import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulator;
import cashmachine.CurrencyManipulatorFactory;

import java.util.Collection;

class InfoCommand implements Command {
    @Override
    public void execute() {
        boolean hasMoney = false;
        Collection<CurrencyManipulator> manipulators =
                CurrencyManipulatorFactory.getAllCurrencyManipulators();

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
            ConsoleHelper.writeMessage("No money available.");
        }
    }
}
