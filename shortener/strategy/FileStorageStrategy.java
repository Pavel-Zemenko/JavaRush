package javarush.shortener.strategy;

import java.util.Objects;

public class FileStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000;

    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private long maxBucketSize;

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Entry entry = table[i].getEntry();
                while (entry != null) {
                    if (Objects.equals(value, entry.getValue())) {
                        return true;
                    }
                    entry = entry.next;
                }
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
            if (table[index] == null) {
                createEntry(hash, key, value, index);
            } else {
                addEntry(hash, key, value, index);
            }
        }

        if (maxBucketSize >= bucketSizeLimit) {
            resize(table.length + 1);
        }
    }

    @Override
    public Long getKey(String value) {
        for (int i = 0; i < table.length; i++) {
            FileBucket bucket = table[i];
            if (bucket != null) {
                Entry entry = table[i].getEntry();
                while (entry != null) {
                    if (Objects.equals(value, entry.getValue())) {
                        return entry.getKey();
                    }
                    entry = entry.next;
                }
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
        FileBucket bucket = table[index];
        if (bucket != null) {
            Entry entry = bucket.getEntry();
            if (entry != null) {
                while (entry != null) {
                    if (entry.getKey().equals(key)) {
                        return entry;
                    }
                    entry = entry.next;
                }
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

        transfer(new FileBucket[n]);
    }

    /**
     * Выполняет перенос элементов из существующей хэш-таблицы
     * в новую (расширенную) хэш-таблицу.
     *
     * @param newTable  ссылка на новую хэш-таблицу,
     *                  в которую будут перенесены элементы.
     */
    private void transfer(FileBucket[] newTable) {
        FileBucket[] prevTable = table;
        table = newTable;
        size = 0;
        for (FileBucket bucket : prevTable) {
            Entry entry = bucket.getEntry();
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
        FileBucket bucket = table[bucketIndex];
        Entry preEntry = bucket.getEntry();
        if (preEntry == null) {
            throw new RuntimeException("Bucket is empty: " + table[bucketIndex]);
        }
        Entry tempEntry = preEntry;
        while (tempEntry.next != null) {
            tempEntry = tempEntry.next;
        }
        tempEntry.next = newEntry;
        bucket.remove();
        bucket.putEntry(preEntry);
        ++size;
        revalidateMaxBucketSize(bucketIndex);
    }

    /**
     * Создаёт новую запись типа Entry и помещает её в пустую "корзину" хэш-таблицы.
     *
     * @param hash         модифицированный хэш-код, вычисленный по хэшу ключа;
     * @param key          ключ;
     * @param value        значение, ассоциируемое с ключом;
     * @param bucketIndex  индекс "корзины", в которой размещается запись.
     */
    private void createEntry(int hash, Long key, String value, int bucketIndex) {
        Entry newEntry = new Entry(hash, key, value, null);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new FileBucket();
            table[bucketIndex].putEntry(newEntry);
            ++size;
            revalidateMaxBucketSize(bucketIndex);
        } else {
            throw new RuntimeException("Bucket is not empty: " + table[bucketIndex]);
        }
    }

    /**
     * Пересчитывает значение поля maxBucketSize после добавления новой записи.
     *
     * @param bucketIndex  индекс "корзины", в которой была размещена запись.
     */
    private void revalidateMaxBucketSize(int bucketIndex) {
        long bucketSize = table[bucketIndex].getFileSize();
        if (bucketSize > maxBucketSize) {
            maxBucketSize = bucketSize;
        }
    }

}
