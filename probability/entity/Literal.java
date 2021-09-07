package javarush.probability.entity;


public class Literal implements Operand {
    private int value;

    public Literal(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
