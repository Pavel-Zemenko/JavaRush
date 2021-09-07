package javarush.probability.command;

import javarush.probability.entity.Operand;


public class Add implements Command {
    private Operand left;
    private Operand right;

    public Add(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Integer calculate() {
        return left.getValue() + right.getValue();
    }

    @Override
    public int getValue() {
        return calculate();
    }
}
