package strategy;

import java.nio.file.Path;

/**
 * Strategy: search by file name or extension.
 */
public class FileNameSearch implements SearchStrategy {

    @Override
    public boolean search(Path filePath, String query) {
        if (filePath == null || query == null) return false;

        String fileName = filePath.getFileName().toString().toLowerCase();
        String q = query.toLowerCase();

        // Match full name or extension
        return fileName.equals(q) || fileName.endsWith(q);
    }
}
