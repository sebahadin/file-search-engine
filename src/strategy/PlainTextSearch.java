package strategy;

import java.nio.file.Path;

/**
 * Search by filename and extension.
 * - Case-insensitive
 * - Matches if filename contains the keyword anywhere
 */
public class PlainTextSearch implements SearchStrategy {

    @Override
    public boolean search(Path filePath, String keyword) {
        if (filePath == null || keyword == null) return false;
        String fileName = filePath.getFileName().toString().toLowerCase();
        return fileName.contains(keyword.toLowerCase());
    }
}
