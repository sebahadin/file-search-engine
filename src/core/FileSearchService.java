package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import config.AppConfig;
import utils.FileUtils;
import utils.Logger;

/**
 * Main service that runs the search.
 */
public class FileSearchService {

    private final ExecutorService executor;
    private final Logger logger;
    private final Scanner scanner;
    private final AppConfig config;

    private SearchTaskFactory taskFactory;

    private final AtomicInteger totalFilesScanned = new AtomicInteger(0);
    private final AtomicInteger matchesFound = new AtomicInteger(0);

    public FileSearchService(ExecutorService executor,
                             Logger logger,
                             Scanner scanner,
                             AppConfig config) {
        this.executor = executor;
        this.logger = logger;
        this.scanner = scanner;
        this.config = config;
    }

    public void setTaskFactory(SearchTaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    public SearchSummary search(Path rootPath, String query) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        logger.info("Starting search in: " + rootPath);
        logger.info("Looking for file name/ext: \"" + query + "\"");

        try {
            Files.walk(rootPath)
                 .filter(Files::isRegularFile)
                 .forEach(path -> {
                     SearchTask task = taskFactory.create(path, query, config);
                     executor.submit(task);
                 });
        } catch (IOException e) {
            logger.error("Error walking file tree: " + e.getMessage(), e);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long elapsed = System.currentTimeMillis() - startTime;
        return new SearchSummary(totalFilesScanned.get(), matchesFound.get(), elapsed);
    }

    public void incrementFilesScanned() {
        totalFilesScanned.incrementAndGet();
    }

    public void handleMatch(Path filePath) {
        matchesFound.incrementAndGet();

        synchronized (scanner) {
            String response;
            while (true) {
                System.out.print("Do you want to copy this file to another directory? (yes/no): ");
                response = scanner.nextLine().trim().toLowerCase();
                if (response.equals("yes") || response.equals("no")) break;
                System.out.println("❌ Invalid input. Please type 'yes' or 'no'.");
            }

            if ("yes".equals(response)) {
                Path destPath = null;
                while (true) {
                    System.out.print("Enter destination directory: ");
                    String destInput = scanner.nextLine().trim();
                    if (destInput.isEmpty()) {
                        System.out.println("❌ Destination cannot be empty.");
                        continue;
                    }
                    destPath = Paths.get(destInput).toAbsolutePath().normalize();
                    try {
                        if (!Files.exists(destPath)) {
                            Files.createDirectories(destPath);
                            logger.info("Created directory: " + destPath);
                        }
                        break;
                    } catch (IOException e) {
                        System.out.println("❌ Could not access/create directory: " + destInput);
                    }
                }
                try {
                    FileUtils.copyFile(filePath, destPath);
                } catch (IOException e) {
                    logger.error("Failed to copy file: " + filePath, e);
                }
            }
        }
    }

    public static class SearchSummary {
        public final int totalFilesScanned;
        public final int matchesFound;
        public final long timeMillis;

        public SearchSummary(int totalFilesScanned, int matchesFound, long timeMillis) {
            this.totalFilesScanned = totalFilesScanned;
            this.matchesFound = matchesFound;
            this.timeMillis = timeMillis;
        }
    }
}
