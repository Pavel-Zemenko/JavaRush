package javarush.probability.command;

import javarush.probability.entity.Operand;


public class Com implements Command {
    private Operand left;
    private Operand right;

    public Com(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Integer calculate() {
        return (left.getValue() > right.getValue()) ? 1 : 0;
    }

    @Override
    public int getValue() {
        return calculate();
    }
}
