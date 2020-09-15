package javarush.cashmachine.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;


/**
 * Виртуальная консоль для эмуляции ввода данных с клавиатуры
 * при выполнении Unit-тестов.
 */
public class VirtualConsole extends InputStream {
    private static VirtualConsole instance;
    private LinkedList<Byte> queue = new LinkedList<>();

    private VirtualConsole() {}

    public static VirtualConsole getInstance() {
        if (instance == null) {
            instance = new VirtualConsole();
        }
        return instance;
    }

    @Override
    public int read() throws IOException {
        if (queue.isEmpty())
            return -1;
        return queue.poll();
    }

    public void setInputData(String data) {
        offer(data.getBytes());
    }

    private void offer(byte[] bytes) {
        for (Byte b : bytes) {
            queue.offer(b);
        }
    }

    public void clear() {
        queue.clear();
    }

}
