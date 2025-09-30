package config;

/**
 * Application configuration class.
 */
public class AppConfig {

    private final int threadPoolSize = 4;

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
