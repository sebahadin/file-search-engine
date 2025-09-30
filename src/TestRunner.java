import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import config.AppConfig;
import core.FileSearchService;
import core.SearchTaskFactory;
import strategy.FileNameSearch;
import strategy.SearchStrategy;
import utils.Logger;

/**
 * Test harness for file-search-engine.
 *
 * Demonstrates both valid and invalid cases.
 */
public class TestRunner {

    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getInstance();
        Scanner scanner = new Scanner(System.in); // not used here
        AppConfig config = new AppConfig();

        Path root = Paths.get("./test").toAbsolutePath().normalize();
        System.out.println("Running tests in: " + root);

        runTest(root, "notes.txt");   // valid file name
        runTest(root, ".md");         // valid extension
        runTest(root, "todo.java");   // valid file in subfolder
        runTest(root, "missing.pdf"); // invalid case
    }

    private static void runTest(Path root, String query) throws Exception {
        System.out.println("\n=== Running test with query: " + query + " ===");

        int threads = Math.max(1, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        SearchStrategy strategy = new FileNameSearch();
        FileSearchService service = new FileSearchService(
                executor, Logger.getInstance(), new Scanner(System.in), new AppConfig()
        );
        SearchTaskFactory factory = new SearchTaskFactory(strategy, service);
        service.setTaskFactory(factory);

        FileSearchService.SearchSummary summary = service.search(root, query);

        System.out.println("--- Test Summary ---");
        System.out.println("Query: " + query);
        System.out.println("Files scanned: " + summary.totalFilesScanned);
        System.out.println("Matches found: " + summary.matchesFound);
        System.out.println("Time: " + summary.timeMillis + " ms");
        System.out.println("--------------------\n");
    }
}
