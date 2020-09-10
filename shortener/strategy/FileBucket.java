package javarush.shortener.strategy;


import javarush.shortener.ExceptionHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBucket {
    private Path path;

    public FileBucket() {
        try {
            this.path = Files.createTempFile("bucket_", "");
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        }
        path.toFile().deleteOnExit();
    }

    /**
     * Возвращвает размер файла, на который указывает поле path.
     */
    public long getFileSize() {
        long fileSize = 0;
        try {
            fileSize = Files.size(path);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        }
        return fileSize;
    }

    /**
     * Сериализует переданную запись типа Entry в файл.
     */
    public void putEntry(Entry entry) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(entry);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        }
    }

    /**
     * Десериализует запись типа Entry из файла, на который указывает поле path.
     */
    public Entry getEntry() {
        if (getFileSize() == 0) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (Entry) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            ExceptionHandler.log(e);
            return null;
        }
    }

    /**
     * Удаляет файл, на который указывает поле path.
     */
    public void remove() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        }
    }
}
