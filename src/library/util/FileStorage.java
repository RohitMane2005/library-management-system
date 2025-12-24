package library.util;

import java.io.*;

public final class FileStorage {

    private FileStorage() {
        // prevent instantiation
    }

    public static void save(Object obj, String filePath) {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot save null object");
        }

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {

            oos.writeObject(obj);

        } catch (IOException e) {
            System.err.println("❌ Failed to save data to " + filePath);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String filePath) {

        File file = new File(filePath);
        if (!file.exists()) {
            return null; // first run, no data yet
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filePath))) {

            return (T) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Failed to load data from " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}
