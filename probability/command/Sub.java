package javarush.probability.command;

import javarush.probability.entity.Operand;


public class Sub extends Command {
    public Sub(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    public Integer calculate() {
        return left.getValue() - right.getValue();
    }
}
