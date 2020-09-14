package javarush.cashmachine;

import javarush.cashmachine.exception.NotEnoughMoneyException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CurrencyManipulator {
    private String currencyCode;                                    // Имя валюты (3 буквы)
    private Map<Integer, Integer> denominations = new HashMap<>();  // Map<номинал, количество>

    /* ----- КОНСТРУКТОРЫ ----- */

    public CurrencyManipulator(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /* ----- ОСНОВНЫЕ МЕТОДЫ ----- */

    /**
     * Возвращает код валюты настоящего манипулятора.
     *
     * @return строка с кодом валюты.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Добавляет банкноты в хранилище манипулятора.
     *
     * @param denomination  номинал вносимых банкнот;
     * @param count         количество вносимых банкнот.
     */
    public void addAmount(int denomination, int count) {
        denominations.compute(denomination, (k, v) -> (v == null) ? count : v + count);
    }

    /**
     * Возвращает общую сумму средств в хранилище манипулятора.
     *
     * @return общая сумма средств.
     */
    public int getTotalAmount() {
        int amount = 0;
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            amount += entry.getKey() * entry.getValue();
        }
        return amount;
    }

    /**
     * Проверяет, есть ли средства в хранилище манипулятора.
     *
     * @return true, если в хранилище есть средства;
     *               иначе возвращает false.
     */
    public boolean hasMoney() {
        for (Integer i : denominations.values()) {
            if (i > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет, достаточно ли в хранилище средств для выдачи запрашиваемой суммы.
     *
     * @param expectedAmount  запрашиваемая сумма.
     *
     * @return true, если средств достаточно для выдачи запрашиваемой суммы;
     *               иначе возвращает false.
     */
    public boolean isAmountAvailable(int expectedAmount) {
        return getTotalAmount() >= expectedAmount;
    }

    /**
     * Выбирает оптимальный набор банкнот для выдачи запрашиваемой суммы
     * (т.е. набор с наименьшим количеством банкнот).
     *
     * @param expectedAmount  запрашиваемая сумма.
     *
     * @return отображение {@code Map<Integer, Integer>}, представляющее набор банкнот,
     *         где ключи - номиналы банкнот, и значения - их количество.
     *
     * @throws NotEnoughMoneyException в случае, если банкнотами в хранилище
     *                                 невозможно выдать запрашиваемую сумму.
     */
    public Map<Integer, Integer> withdrawAmount(int expectedAmount) throws NotEnoughMoneyException {
        TreeMap<Integer, Integer> stock = new TreeMap<>(denominations);  // банкноты в банкомате
        TreeMap<Integer, Integer> issue = new TreeMap<>();               // банкноты к выдаче

        int sum = 0;
        Integer note = stock.lastKey();
        List<TreeMap<Integer, Integer>> cashMaps = new LinkedList<>();

        do {
            // Проверка правильности значения текущей банкноты:
            if (!stock.containsKey(note)) {
                Integer key = stock.lowerKey(note);
                if (key != null) {
                    note = key;
                } else if ((key = stock.higherKey(note)) != null) {
                    note = key;
                } else break;
            }

            // Обработка 3 вариантов добавления банкноты к сумме выдачи:
            int tempSum = sum + note;
            if (tempSum <= expectedAmount) {
                sum = tempSum;
                transferNote(stock, issue, note);
                if (tempSum == expectedAmount) {
                    cashMaps.add(new TreeMap<>(issue));
                } else continue;
            } else {
                Integer key = stock.lowerKey(note);
                if (key != null) {
                    note = key;
                    continue;
                }
            }

            // "Откат" до ближайшей "развилки" перед поиском следующего набора банкнот:
            while (issue.containsKey(note)) {
                transferNote(issue, stock, note);
                sum = sum - note;
            }
            Integer lesserNote = issue.isEmpty() ? null : issue.firstKey();
            if (lesserNote != null) {
                transferNote(issue, stock, lesserNote);
                sum = sum - lesserNote;
            } else break;
            Integer key = stock.isEmpty() ? null : stock.lowerKey(lesserNote);
            if (key != null) {
                note = key;
            } else break;

        } while (!issue.isEmpty());

        if (cashMaps.isEmpty()) {
            throw new NotEnoughMoneyException();
        }

        Map<Integer, Integer> resultMap = getOptimalMap(cashMaps);
        debit(resultMap);

        return resultMap;
    }

    /* ----- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ----- */

    /**
     * Выбирает из передаваемого списка оптимальный набор банкнот для выдачи.
     *
     * @param cashMaps  список отображений, каждое из которых представляет
     *                  набор банкнот для выдачи.
     *
     * @return отображение, представляющее оптимальный набор банкнот для выдачи.
     */
    private Map<Integer, Integer> getOptimalMap(List<TreeMap<Integer, Integer>> cashMaps) {
        TreeMap<Integer, Integer> optimalMap = cashMaps.get(0);
        for (TreeMap<Integer, Integer> nextMap : cashMaps) {
            int qty1 = getQtyOfNotes(optimalMap);
            int qty2 = getQtyOfNotes(nextMap);
            if (qty2 < qty1) {
                optimalMap = nextMap;
            } else if (qty2 == qty1) {
                if (optimalMap.get(optimalMap.lastKey()) < nextMap.get(nextMap.lastKey())) {
                    optimalMap = nextMap;
                }
            }
        }
        return optimalMap;
    }

    /**
     * Возвращает общее количество банкнот в заданном наборе.
     *
     * @param map  набор банкнот (отображение {@code Map<Integer, Integer>},
     *             где ключи - номиналы банкнот, а значения - их количество).
     *
     * @return общее количество банкнот в наборе {@code map}.
     */
    private int getQtyOfNotes(TreeMap<Integer, Integer> map) {
        int sum = 0;
        for (Integer integer : map.values()) {
            sum += integer;
        }
        return sum;
    }

    /**
     * Удаляет из хранилища манипулятора все банкноты, содержащиеся
     * в передаваемом отображении.
     *
     * @param issue  отображение с банкнотами для выдачи.
     */
    private void debit(Map<Integer, Integer> issue) {
        for (Map.Entry<Integer, Integer> entry : issue.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                if (getNote(denominations, entry.getKey()) == null) {
                    throw new RuntimeException("Списываемая банкнота не найдена.");
                }
            }
        }
    }

    /**
     * Перемещает банкноту заданного номинала из отображения {@code from}
     * в отображение {@code to}. Если в исходном хранилище {@code from}
     * запрашиваемая банкнота отсутствует, то перемещение на произойдёт.
     *
     * @param from  отдающее хранилище;
     * @param to    принимающее хранилище.
     */
    private void transferNote(Map<Integer, Integer> from, Map<Integer, Integer> to, Integer note) {
        if (getNote(from, note).equals(note)) {
            setNote(to, note);
        }
    }

    /**
     * Извлекает запрашиваемую банкноту из заданного хранилища.
     *
     * @param storage  отображение (хранилище банкнот);
     * @param note     запрашиваемая банкнота.
     *
     * @return номинал извлечённой банкноты, если такая банкнота была в хранилище;
     *         если запрашиваемая банкнота отсутствует, возвращает {@code null}.
     */
    private Integer getNote(Map<Integer, Integer> storage, Integer note) {
        Integer result = null;
        if (storage.containsKey(note)) {
            int qty = storage.get(note) - 1;
            result = note;
            if (qty > 0) {
                storage.put(note, qty);
            } else {
                storage.remove(note);
            }
        }
        return result;
    }

    /**
     * Помещает передаваемую банкноту в указанное хранилище.
     *
     * @param storage  отображение (хранилище банкнот);
     * @param note     банкнота.
     */
    private void setNote(Map<Integer, Integer> storage, Integer note) {
        storage.computeIfPresent(note, (k, v) -> v + 1);
        storage.putIfAbsent(note, 1);
    }
}
