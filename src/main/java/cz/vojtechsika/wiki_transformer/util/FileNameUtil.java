package cz.vojtechsika.wiki_transformer.util;

import java.net.URI;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.UUID;

/**
 * Utility class for working with file names and URLs.
 * <ul>
 *   <li>Sanitizes file names by normalizing Unicode, removing diacritics and special marks,
 *       replacing invalid file system characters, and cleaning up redundant underscores or whitespace.</li>
 *   <li>Generates a deterministic suffix from a URL’s hash code to ensure stable, unique file names.</li>
 *   <li>Extracts the file name (including extension) from a given image URL.</li>
 * </ul>
 */

public class FileNameUtil {

    /**
     * Converts a given file name string into a safe format for file system usage
     * @param fileName  the original file name to be sanitized
     * @return a sanitized version of the file name
     */
    public static String sanitizeFileName(String fileName) {
        // Text trim white spaces on the start and the end
        fileName = fileName.trim();

        // Normalize the string to decompose characters with diacritics ( á → a + ́)
        String normalizedFileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);

        // Remove all combining diacritical marks using Unicode character class (a + ́ →  a), used regex
        String withoutDiacritics = normalizedFileName.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Replace all forbidden characters (everything except a-z, A-Z, 0-9, hyphen, underscore, dot) with underscore
        String replacedFileName = withoutDiacritics.replaceAll("[^a-zA-Z0-9-_\\.]", "_");

        // Clean up the result
        String clearFileName = replacedFileName
                .replaceAll("_+", "_")   // Collapse multiple consecutive underscores into one
                .replaceAll("^_+|_+$", "");  // Remove leading and trailing underscores
        return clearFileName;
    }

    /**
     * Creates a deterministic suffix from the given wiki URL.
     * <p>
     * This method computes the absolute value of the URL’s {@code hashCode()}
     * and returns it as a string. Using the URL’s hash ensures that the same
     * page always yields the same suffix, avoiding accidental overwrites
     * while keeping names stable across runs.
     * </p>
     *
     * @param wikiUrl the full URL of the wiki page to hash
     * @return a string representation of the absolute hash code of {@code wikiUrl}
     */
    public static String createUniqueSuffix(String wikiUrl){
        int code = Math.abs(wikiUrl.hashCode());
        return String.valueOf(code);
    }


    /**
     * Extracts the file name (including its extension) from the given image URL.
     * <p>
     * Parses the URL into a {@code URI}, retrieves the path component, and then
     * returns the last segment of that path (the file name with extension).
     * </p>
     *
     * @param imageUrl the absolute or relative URL of an image
     * @return the file name plus extension (e.g. {@code "logo-2.png"})
     */
    public static String getNameWithExtension(String imageUrl) {
        // Create a URI object from the URL and extract the path (everything after the domain)
        URI uri = URI.create(imageUrl);
        String path = uri.getPath();

        // Extract the last segment of the path, including its extension
        return Paths.get(path).getFileName().toString();


    }
}
