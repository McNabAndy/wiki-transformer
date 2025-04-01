package cz.vojtechsika.wiki_transformer.util;

import java.text.Normalizer;

/**
 *
 */

public class FileNameUtil {

    /**
     *
     * @param fileName
     * @return
     */
    public static String sanitizeFileName(String fileName) {
        // Text to lower case and trim white spaces on the start and the end
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
}
