package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for file operations.
 */
public class FileUtils {

    private FileUtils() {}

    public static void copyFile(Path source, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path target = targetDir.resolve(source.getFileName());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        Logger.getInstance().info("Copied file to: " + target);
    }
}
