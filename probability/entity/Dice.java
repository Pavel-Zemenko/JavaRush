package javarush.probability.entity;


public class Dice implements Operand {
    private int sideCount;
    private int currValue;

    public Dice(int sideCount) {
        this.sideCount = sideCount;
        reset();
    }

    public void reset() {
        this.currValue = 1;
    }

    public boolean hasNext() {
        return currValue < sideCount;
    }

    public boolean next() {
        if (hasNext()) {
            ++currValue;
            return true;
        }
        return false;
    }

    public void setValue(int value) {
        this.currValue = value;
    }

    @Override
    public int getValue() {
        return currValue;
    }
}
