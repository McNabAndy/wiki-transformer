package cz.vojtechsika.wiki_transformer.exception;

/**
 * Exception indicating a failure while fetching data from the Redmine API.
 * This runtime exception is used to wrap errors related to Redmine communication,
 * such as invalid responses, connection issues, or deserialization problems.
 * It allows for consistent error signaling and centralized handling in CLI workflows.
 */
public class RedmineFetchException extends RuntimeException {

    /**
     * Constructs with a default message.
     */
    public RedmineFetchException() {
        super("Redmine fetch exception");
    }

    /**
     * Constructs with a custom error message.
     *
     * @param message the detail message describing the exception
     */
    public RedmineFetchException(String message) {
        super(message);
    }

    /**
     * Constructs with a custom error message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause of the exception
     */
    public RedmineFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
