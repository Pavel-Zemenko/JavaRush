package javarush.probability.command;

import javarush.probability.entity.Operand;


public class Mul implements Command {
    private Operand left;
    private Operand right;

    public Mul(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Integer calculate() {
        return left.getValue() * right.getValue();
    }

    @Override
    public int getValue() {
        return calculate();
    }
}
