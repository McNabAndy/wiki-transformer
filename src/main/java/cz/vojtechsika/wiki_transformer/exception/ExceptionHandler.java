package cz.vojtechsika.wiki_transformer.exception;

import org.springframework.stereotype.Component;

/**
 * Centralized exception handling utility for CLI-based termination of the application.
 * Provides methods for printing user-friendly error messages, optional exception details,
 * and cleanly exiting the JVM with a non-zero status code.
 */
@Component
public class ExceptionHandler {


    /**
     * Prints a user-friendly error message and detailed exception information,
     * then terminates the application with exit code 1.
     *
     * @param userMessage a custom message describing what went wrong, shown to the user
     * @param e the exception that caused the failure (used to extract details)
     */
    public void exitWithError(String userMessage, Exception e) {
        System.out.println("Error: " + userMessage);
        System.out.println("Detail: " + e.getMessage());
        System.exit(1);
    }

    /**
     * Prints a user-friendly error message and exits the application with exit code 1.
     *
     * @param userMessage a custom message describing what went wrong, shown to the user
     */
    public void exitWithError(String userMessage) {
        System.out.println("Error: " + userMessage);
        System.exit(1);
    }

}
