package javarush.shortener.strategy;

import java.util.Objects;

public class OurHashMapStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Entry[] table = new Entry[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private float loadFactor = DEFAULT_LOAD_FACTOR;


    /* ----- StorageStrategy implementation ----- */

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i];
            while (entry != null) {
                if (Objects.equals(value, entry.getValue())) {
                    return true;
                }
                entry = entry.next;
            }
        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Entry entry = getEntry(key);

        if (entry != null) {
            entry.value = value;
        } else {
            if (size + 1 >= threshold) {
                resize(table.length + 1);
            }
            if (table[index] == null) {
                createEntry(hash, key, value, index);
            } else {
                addEntry(hash, key, value, index);
            }
        }
    }

    @Override
    public Long getKey(String value) {
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i];
            while (entry != null) {
                if (Objects.equals(value, entry.getValue())) {
                    return entry.getKey();
                }
                entry = entry.next;
            }
        }
        return null;
    }

    @Override
    public String getValue(Long key) {
        Entry entry = getEntry(key);
        return (entry != null) ? entry.getValue() : null;
    }


    /* ----- Auxiliary methods ----- */

    /**
     * Модифицирует хэш-код переданного ключа для обеспечения равномерного
     * распределения записей по "корзинам" хэш-таблицы.
     *
     * @param k  хэшируемый ключ.
     *
     * @return модифицированный хэш-код для переданного ключа.
     */
    private int hash(Long k) {
        int h;
        return (k == null) ? 0 : (h = k.hashCode()) ^ (h >>> 16);
    }

    /**
     * Возвращает индекс "корзины", вычисленный по переданным
     * хэш-коду и длине хэш-таблицы.
     *
     * @param hash   хэш-код (предварительно модифицированный);
     * @param length длина хэш-таблицы.
     *
     * @return индекс в массиве, соответствующий переданному хэш-коду.
     */
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    /**
     * Возвращает запись из хэш-таблицы, соответствующую переданному ключу.
     *
     * @param key  ключ.
     *
     * @return запись, соответствующая переданному ключу.
     */
    private Entry getEntry(Long key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Entry entry = table[index];
        if (entry != null) {
            while (entry != null) {
                if (entry.getKey() == key || entry.getKey().equals(key)) {
                    return entry;
                }
                entry = entry.next;
            }
        }
        return null;
    }

    /**
     * Расширяет хэш-таблицу до заданного размера.
     *
     * @param newCapacity  новая ёмкость хранилища.
     */
    private void resize(int newCapacity) {
        int n = newCapacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = (n < 0) ? 1 : n + 1;

        transfer(new Entry[n]);
    }

    /**
     * Выполняет перенос элементов из существующей хэш-таблицы
     * в новую (расширенную) хэш-таблицу.
     *
     * @param newTable  ссылка на новую хэш-таблицу,
     *                  в которую будут перенесены элементы.
     */
    private void transfer(Entry[] newTable) {
        Entry[] prevTable = table;
        table = newTable;
        size = 0;
        threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
        for (Entry entry : prevTable) {
            while (entry != null) {
                Long key = entry.getKey();
                String value = entry.getValue();
                put(key, value);
                entry = entry.next;
            }
        }
    }

    /**
     * Создаёт новую запись типа Entry и присоединяет её к соответствующей
     * "цепочке" хэш-таблицы.
     *
     * @param hash         модифицированный хэш-код, вычисленный по хэшу ключа;
     * @param key          ключ;
     * @param value        значение, ассоциируемое с ключом;
     * @param bucketIndex  индекс "цепочки", к которой присоединяется запись.
     */
    private void addEntry(int hash, Long key, String value, int bucketIndex) {
        Entry newEntry = new Entry(hash, key, value, null);
        Entry preEntry = table[bucketIndex];
        if (preEntry == null) {
            throw new RuntimeException("Bucket is empty: " + table[bucketIndex]);
        }
        while (preEntry.next != null) {
            preEntry = preEntry.next;
        }
        preEntry.next = newEntry;
        ++size;
    }

    /**
     * Создаёт новую запись типа Entry и помещает её в пустую "корзину" хэш-таблицы.
     *
     * @param hash         модифицированный хэш-код, вычисленный по хэшу ключа;
     * @param key          ключ;
     * @param value        значение, ассоциируемое с ключом;
     * @param bucketIndex  индекс ячейки, в которой размещается запись.
     */
    private void createEntry(int hash, Long key, String value, int bucketIndex) {
        Entry newEntry = new Entry(hash, key, value, null);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newEntry;
            ++size;
        } else {
            throw new RuntimeException("Bucket is not empty: " + table[bucketIndex]);
        }
    }
}
