package javarush.probability.command;

import javarush.probability.entity.Operand;


public abstract class Command implements Operand {
    protected Operand left;
    protected Operand right;

    public Command(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }

    public abstract Integer calculate();

    @Override
    public int getValue() {
        return calculate();
    }
}
