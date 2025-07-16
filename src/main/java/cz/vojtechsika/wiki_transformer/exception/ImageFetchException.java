package cz.vojtechsika.wiki_transformer.exception;

/**
 * Thrown to indicate that an error occurred while fetching an image.
 */
public class ImageFetchException extends RuntimeException {


    /**
     * Creates a new ImageFetchException with a default message.
     */
    public ImageFetchException(){
        super("Image Downloader fetch exception");
    }

    /**
     * Creates a new ImageFetchException with the specified detail message.
     *
     * @param message the detail message explaining the error
     */
    public ImageFetchException(String message) {
        super(message);
    }

    /**
     * Creates a new ImageFetchException with the specified detail message and cause.
     *
     * @param message the detail message explaining the error
     * @param cause   the underlying cause of this exception
     */
    public ImageFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
