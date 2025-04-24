package cz.vojtechsika.wiki_transformer.exception;

public class RedmineFetchException extends RuntimeException {

    public RedmineFetchException() {
        super("Redmine fetch exception");
    }

    public RedmineFetchException(String message) {
        super(message);
    }

    public RedmineFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
