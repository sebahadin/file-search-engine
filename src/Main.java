import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import core.FileSearchService;
import core.SearchTaskFactory;
import strategy.FileNameSearch;
import strategy.SearchStrategy;
import utils.Logger;
import config.AppConfig;

public class Main {

    public static void main(String[] args) {
        final Logger logger = Logger.getInstance();
        final Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("     file-search-engine (Java) - by name ");
        System.out.println("========================================");

        // 1) Ask for root directory until valid
        Path rootPath = null;
        while (true) {
            System.out.print("Enter root directory path: ");
            String rootInput = scanner.nextLine().trim();

            if (rootInput.isEmpty()) {
                System.out.println("❌ Root directory cannot be empty.");
                continue;
            }

            rootPath = Paths.get(rootInput).toAbsolutePath().normalize();
            if (!Files.exists(rootPath) || !Files.isDirectory(rootPath)) {
                System.out.println("❌ Path does not exist or is not a directory: " + rootPath);
                continue;
            }

            break; // valid
        }

        // 2) Ask for file name pattern
        String keyword = null;
        while (true) {
            System.out.print("Enter file name or extension to search (e.g., report.txt or .md): ");
            keyword = scanner.nextLine().trim();

            if (keyword.isEmpty()) {
                System.out.println("❌ File name cannot be empty.");
                continue;
            }
            break;
        }

        // 3) Config & dependencies
        AppConfig config = new AppConfig();
        int threads = config.getThreadPoolSize();
        if (threads <= 0) {
            threads = Math.max(1, Runtime.getRuntime().availableProcessors());
        }

        System.out.println("Using a fixed thread pool of size: " + threads);
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        SearchStrategy strategy = new FileNameSearch();
        FileSearchService service = new FileSearchService(executor, logger, scanner, config);
        SearchTaskFactory taskFactory = new SearchTaskFactory(strategy, service);
        service.setTaskFactory(taskFactory);

        // 4) Run search
        try {
            FileSearchService.SearchSummary summary = service.search(rootPath, keyword);

            System.out.println("\n------------- SUMMARY -------------");
            System.out.println("Root directory : " + rootPath);
            System.out.println("File name/ext  : " + keyword);
            System.out.println("Files scanned  : " + summary.totalFilesScanned);
            System.out.println("Matches found  : " + summary.matchesFound);
            System.out.println("Time taken     : " + summary.timeMillis + " ms");
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            logger.error("Search failed due to an unexpected error.", e);
        } finally {
            executor.shutdownNow();
        }
    }
}
