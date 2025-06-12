package com.waracle.cakemgr.web.controller;

import com.waracle.cakemgr.CakeManagerWebApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test class that manages the lifecycle of the Spring Boot server
 * and runs Postman collection tests against the running application.
 * <p>
 * The Spring Boot application is started asynchronously before all tests
 * and stopped after all tests complete.
 * <p>
 * The Postman collection is executed using Newman CLI, allowing validation
 * of API endpoints via predefined HTTP request scenarios.
 * </p>
 * <p>
 * Note: The Postman test method is currently commented out because it requires
 * Newman CLI installed and available in the system path to execute.
 * </p>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostmanCollectionIntegrationTest {

    /**
     * The Spring application context for managing the server lifecycle.
     */
    private ConfigurableApplicationContext context;

    /**
     * Starts the Spring Boot application asynchronously before any tests run.
     * <p>
     * This method launches the {@link CakeManagerWebApplication} in a daemon thread,
     * then waits a fixed duration to allow the server to start.
     * Ideally, this wait should be replaced by a more robust health check.
     * </p>
     *
     * @throws Exception if the thread sleep is interrupted or application fails to start
     */
    @BeforeAll
    public void startServer() throws Exception {
        Thread serverThread = new Thread(() -> context = SpringApplication.run(CakeManagerWebApplication.class));
        serverThread.setDaemon(true);
        serverThread.start();

        // Naive wait to allow server startup; consider polling health endpoint instead
        Thread.sleep(5000);
    }

    /**
     * Runs the Postman collection tests using the Newman CLI.
     * <p>
     * Executes Newman as an external process with specified collection and environment files,
     * capturing output and asserting the tests pass with exit code 0.
     * </p>
     * <p>
     * This test is currently commented out to prevent build failures when Newman is not installed.
     * To enable, ensure Newman is installed globally via npm (e.g. `npm install -g newman`),
     * then uncomment this method.
     * </p>
     *
     * @throws Exception if execution of the Newman CLI or process IO fails
     */
    //@Test
    public void runPostmanCollection() throws Exception {
        String newmanCmd = "newman run src/test/resources/CakeController.postman_collection.json -e src/test/resources/CakeController.env.json --insecure";

        Process process = Runtime.getRuntime().exec(newmanCmd);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
            System.out.println(line); // optional: print output for debug
        }

        int exitCode = process.waitFor();

        // Newman returns 0 if all tests pass
        assertEquals(0, exitCode, "Postman tests failed:\n" + output);
    }

    /**
     * Stops the Spring Boot application after all tests have run.
     * <p>
     * Closes the Spring application context to release resources and shut down the server gracefully.
     * </p>
     */
    @AfterAll
    public void stopServer() {
        if (context != null) {
            context.close();
        }
    }
}
