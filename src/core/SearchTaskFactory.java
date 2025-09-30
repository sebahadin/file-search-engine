package core;

import java.nio.file.Path;

import config.AppConfig;
import strategy.SearchStrategy;

/**
 * Factory for SearchTask.
 */
public class SearchTaskFactory {

    private final SearchStrategy strategy;
    private final FileSearchService service;

    public SearchTaskFactory(SearchStrategy strategy, FileSearchService service) {
        this.strategy = strategy;
        this.service = service;
    }

    public SearchTask create(Path filePath, String query, AppConfig config) {
        return new SearchTask(filePath, query, strategy, service, config);
    }
}
