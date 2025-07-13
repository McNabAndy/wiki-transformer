package cz.vojtechsika.wiki_transformer.util;

import java.net.URI;
import java.nio.file.Paths;
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
    public static String createUniqueSuffix(String wikiUrl){
        // Vypočítá z url hash který pokud budu stejnou url adresu stahovat znovu vygeneruje stejný has, kterému odstarním mínus tím že ho převedu na absolutní hodnotu
        int code = Math.abs(wikiUrl.hashCode());

        System.out.println(code);
        return String.valueOf(code);
    }


    public static String getNameWithExtension(String imageUrl) {
        // Crate URI object from url and get path from url - dostanu všechno co je za doménou
        URI uri = URI.create(imageUrl);
        String path = uri.getPath();

        // dostanu poslední segment cesty včetně přípony
        return Paths.get(path).getFileName().toString();


    }
}
