package javarush.probability.command;

import javarush.probability.entity.Operand;


public class Add extends Command {
    public Add(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    public Integer calculate() {
        return left.getValue() + right.getValue();
    }
}
