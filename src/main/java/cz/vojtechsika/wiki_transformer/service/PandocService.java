package cz.vojtechsika.wiki_transformer.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Service interface for converting text from Textile format to MediaWiki format using Pandoc.
 */
public interface PandocService {

    /**
     * Converts content from Textile format to MediaWiki format using Pandoc and saves the result as a file.
     *
     * @param content          the original wiki page content in Textile format
     * @param fileName         the sanitized and unique file name (without path) to be used for the output
     * @param filePath         the path to the output directory as a {@link Path} object
     * @param outputDirectory  the output directory as a string, typically passed from the CLI
     * @throws IOException if an I/O error occurs during the conversion process or file writing
     */
    void convertTextileToMediaWiki(String content, String fileName, Path filePath, String outputDirectory) throws IOException;
}
