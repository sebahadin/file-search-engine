package core;

import java.nio.file.Path;

import config.AppConfig;
import strategy.SearchStrategy;
import utils.Logger;

/**
 * Task that checks one file.
 */
public class SearchTask implements Runnable {

    private final Path filePath;
    private final String query;
    private final SearchStrategy strategy;
    private final FileSearchService service;
    private final AppConfig config;

    public SearchTask(Path filePath,
                      String query,
                      SearchStrategy strategy,
                      FileSearchService service,
                      AppConfig config) {
        this.filePath = filePath;
        this.query = query;
        this.strategy = strategy;
        this.service = service;
        this.config = config;
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();
        service.incrementFilesScanned();

        boolean found = strategy.search(filePath, query);

        if (found) {
            logger.info("File matched: " + filePath);
            service.handleMatch(filePath);
        }
    }
}
