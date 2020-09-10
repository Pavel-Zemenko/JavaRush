package cashmachine;

public enum Operation {
    INFO, DEPOSIT, WITHDRAW, EXIT;

    public static Operation getAllowableOperationByOrdinal(Integer i) throws IllegalArgumentException {
        try {
            return values()[i - 1];
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
