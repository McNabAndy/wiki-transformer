package cz.vojtechsika.wiki_transformer.util;

import java.text.Normalizer;
import java.util.UUID;

/**
 *  Utility class providing functionality for sanitizing file names to make them safe for use
 *  in file systems. This includes normalization, removal of diacritics, replacement of forbidden
 *  characters, and cleanup of redundant underscores.
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
     * Generates a unique suffix string based on a random UUID.
     * @return unique suffix string without dashes
     */
    public static String createUniqueSuffix(){
        // Generate random 128-bit long number in hex characters separated by “-“
        String uniqueSuffix = UUID.randomUUID().toString();

        // Remove underscores
        uniqueSuffix = uniqueSuffix.replaceAll("-", "");

        return uniqueSuffix;
    }
}
