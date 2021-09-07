package javarush.probability.command;

import javarush.probability.entity.Operand;


public interface Command extends Operand {
    Integer calculate();
}
