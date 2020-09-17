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
     * <br> Метод выбирает оптимальный набор банкнот для выдачи запрашиваемой суммы
     * (т.е. набор с наименьшим количеством банкнот возможно большего номинала). <br/>
     *
     * <br> Для поиска оптимальной комбинации банкнот используется "жадный" алгоритм,
     * начинающий набор необходимой суммы с банкнот наибольшего номинала. Как только
     * при выборе очередной банкноты окажется, что требуемая сумма {@code expectedAmount}
     * будет превышена, выбирается банкнота меньшего номинала. Каждая отобранная банкнота
     * перемещается из хранилища {@code stock} во временное отображение {@code issue}.
     * Когда необходимая сумма набрана, отображение {@code issue} копируется в накопительный
     * список {@code cashMaps}; таким образом, сохраняется найденный вариант набора банкнот. <br/>
     *
     * <br> После нахождения варианта алгоритм производит "откат" до ближайшей "развилки":
     * из временного отображения {@code issue} извлекаются все банкноты наименьшего номинала
     * и возвращаютсяв хранилище {@code stock}; далее из оставшихся в отображении {@code issue}
     * банкнот извлекается одна банкнота наименьшего номинала и также возвращается в хранилище.
     * Когда "откат" произведён, алгоритм может продолжать поиск другого подходящего набора
     * банкнот, следуя по другому "пути". Чтобы это "переключение" произошло, при продолжении
     * поиска необходимо первой взять банкноту, номинал которой на 2 ступени меньше наименьшего
     * номинала, имеющегося в отображении {@code issue}. <br/>
     *
     * <br> К примеру, используется валюта со следующими номиналами: 100, 50, 20, 10, 5,
     * а отображение {@code issue} после "отката" выглядит следующим образом: { 100=2, 50=3 }.
     * В этом случае поиск следует продолжить с банкноты номиналом 10. <br/>
     *
     * <br> В процессе поиска ведётся учёт общего количества банкнот, задействованных
     * для текущего варианта. Этот учёт обеспечивается счётчиком {@code curCount}, значение
     * которого увеличивается/уменьшается при добавлении/извлечении банкноты во временное
     * отображение {@code issue}. Поскольку наиболее оптимальный вариант должен содержать
     * минимальное количество банкнот, то этот параметр также контролируется: когда очередной
     * вариант найден, его общее количество банкнот сравнивается со значением {@code minCount},
     * то есть с минимальным количеством банкнот в одном из предыдущих вариантов.
     * Если в новом варианте количество банкнот превышает значение {@code minCount}, то такой
     * вариант игнорируется и не сохраняется в список {@code cashMaps} как заведомо неоптимальный.
     * В противном случае в список заносится найденный вариант, значение {@code minCount}
     * обновляется в соответсвии с новыми данными, и начинается новый поиск. <br/>
     *
     * <br> Также с целью отсеивания заведомо неоптимальных вариантов перед добавлением
     * следующей банкноты выполняется проверка {@code curCount <= minCount}. Это позволяет
     * существенно повысить производительность алгоритма, особенно в случаях, когда запрашиваемая
     * сумма на несколько порядков превосходит минимальный номинал данного типа валюты
     * (к примеру, запрос выдать 2500 при минимальном номинале банкноты 5). <br/>
     *
     * <br> По завершении цикла поиска все подходящие варианты в виде списка {@code cashMaps}
     * передаются методу {@code getOptimalMap(List<TreeMap<Integer, Integer>> cashMaps)},
     * который выбирает из предоставленного списка наиболее оптимальный вариант. <br/>
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
        int minCount = Integer.MAX_VALUE;
        int curCount = 0;
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
            if (tempSum <= expectedAmount && curCount <= minCount) {
                sum = tempSum;
                transferNote(stock, issue, note);
                curCount++;
                if (tempSum == expectedAmount ) {
                    if (curCount <= minCount) {
                        cashMaps.add(new TreeMap<>(issue));
                        minCount = curCount;
                        curCount = 0;
                    }
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
                curCount--;
            }
            Integer lesserNote = issue.isEmpty() ? null : issue.firstKey();
            if (lesserNote != null) {
                transferNote(issue, stock, lesserNote);
                sum = sum - lesserNote;
                curCount--;
            } else break;
            Integer key = stock.isEmpty() ? null : stock.lowerKey(lesserNote);
            if (key != null) {
                note = key;
            } else break;

        } while (true);

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
