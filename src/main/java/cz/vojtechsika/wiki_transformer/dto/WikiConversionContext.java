package cz.vojtechsika.wiki_transformer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Context object carrying all necessary data for converting a Redmine wiki page.
 * <ul>
 *   <li><b>uniqueTitle</b> – sanitized and suffixed title used for naming output files.</li>
 *   <li><b>wikiText</b> – raw Textile content of the wiki page.</li>
 *   <li><b>filePath</b> – base directory path where output files will be written.</li>
 *   <li><b>wikiUrl</b> – original URL of the Redmine wiki page.</li>
 *   <li><b>outputDir</b> – string representation of the output directory path.</li>
 * </ul>
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WikiConversionContext {

    /**
     * Sanitized unique title
     */
    private String uniqueTitle;

    /**
     * Raw Textile content of the wiki page.
     */
    private String wikiText;

    /**
     * Base directory path where all output files (converted text and images)
     */
    private Path filePath;

    /**
     * Original URL of the Redmine wiki page.
     */
    private String wikiUrl;

    /**
     * String form of the output directory
     */
    private String outputDir;
}
