package strategy;

import java.nio.file.Path;

/**
 * Strategy interface for searching a file.
 */
public interface SearchStrategy {

    /**
     * Decide if this file matches the query.
     *
     * @param filePath file to check
     * @param query file name or extension to look for
     * @return true if match
     */
    boolean search(Path filePath, String query);
}
