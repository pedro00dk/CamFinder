package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class that helps classifiers serialization.
 *
 * @author Pedro Henrique
 */
public final class SerializationUtils {

    /**
     * Prevents instantiation.
     */
    private SerializationUtils() {
    }

    /**
     * Serializes the received object saving the the path
     *
     * @param object the object to serialize
     * @param path   the path of the object
     * @param <T>    the object class
     * @throws IOException if fail to serialize
     */
    public static <T extends Serializable> void serialize(T object, Path path) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path));
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    /**
     * Deserialize the object in the received path
     *
     * @param path the path with the serialized object
     * @param <T>  the object class
     * @return the deserialized object
     * @throws IOException            if fails to deserialize
     * @throws ClassNotFoundException if the object class was not found
     */
    public static <T> T deserialize(Path path) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path));
        @SuppressWarnings("unchecked") T object = (T) objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }
}
