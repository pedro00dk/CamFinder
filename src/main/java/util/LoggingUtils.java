package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class that contains some methods to help logging.
 *
 * @author Pedro Henrique
 */
public final class LoggingUtils {

    /*
     * Setting global console handler.
     */
    static {
        ConsoleHandler globalLoggerConsoleHandler = Stream.of(global().getHandlers())
                .filter(ConsoleHandler.class::isInstance)
                .findFirst()
                .map(ConsoleHandler.class::cast)
                .orElse(null);
        if (globalLoggerConsoleHandler == null) {
            globalLoggerConsoleHandler = new ConsoleHandler();
            global().addHandler(globalLoggerConsoleHandler);
        }
        globalLoggerConsoleHandler.setLevel(Level.FINER);
        global().setLevel(Level.FINER);
    }

    /**
     * Prevents instantiation.
     */
    private LoggingUtils() {
    }

    /**
     * Returns the global logger.
     *
     * @return the global logger
     */
    public static Logger global() {
        return Logger.getGlobal();
    }

    /**
     * Returns the name of the class where this method was called.
     *
     * @return the class name
     */
    public static String className() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    /**
     * Returns the name of the method where this method was called.
     *
     * @return the method name
     */
    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
