package cashmachine.cashmachine.command;

import cashmachine.cashmachine.exception.InterruptOperationException;

public interface Command {
    void execute() throws InterruptOperationException;
}
