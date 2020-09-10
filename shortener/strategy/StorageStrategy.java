package javarush.shortener.strategy;

public interface StorageStrategy {

    /**
     * Должен вернуть true, если хранилище содержит переданный ключ.
     */
    boolean containsKey(Long key);

    /**
     * Должен вернуть true, если хранилище содержит переданное значение.
     */
    boolean containsValue(String value);

    /**
     * Добавить в хранилище новую пару КЛЮЧ=ЗНАЧЕНИЕ.
     */
    void put(Long key, String value);

    /**
     * Вернуть ключ для переданного значения.
     */
    Long getKey(String value);

    /**
     * Вернуть значение для переданного ключа.
     */
    String getValue(Long key);
}
