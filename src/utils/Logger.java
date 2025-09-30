package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread-safe Singleton Logger.
 */
public class Logger {

    private static Logger instance;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    private String getTimestamp() {
        return formatter.format(new Date());
    }

    public synchronized void info(String message) {
        System.out.println("[INFO] " + getTimestamp() + " - " + message);
    }

    public synchronized void warn(String message) {
        System.out.println("[WARN] " + getTimestamp() + " - " + message);
    }

    public synchronized void error(String message, Throwable t) {
        System.err.println("[ERROR] " + getTimestamp() + " - " + message);
        if (t != null) {
            t.printStackTrace(System.err);
        }
    }
}
