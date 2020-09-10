package javarush.cashmachine.command;

import javarush.cashmachine.exception.InterruptOperationException;

public interface Command {
    void execute() throws InterruptOperationException;
}
