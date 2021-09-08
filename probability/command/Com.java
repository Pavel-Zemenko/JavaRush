package javarush.probability.command;

import javarush.probability.entity.Operand;


public class Com extends Command {
    public Com(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    public Integer calculate() {
        return (left.getValue() > right.getValue()) ? 1 : 0;
    }
}
