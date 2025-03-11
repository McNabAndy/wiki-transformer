package cz.vojtechsika.wiki_transformer.service;

/**
 * Service interface for converting text from Textile format to MediaWiki format using Pandoc.
 */
public interface PandocService {

    /**
     * Converts the given Textile-formatted content into MediaWiki format using Pandoc.
     *
     * @param content the Textile-formatted text to be converted.
     */
    void convertTextileToMediaWiki(String content);
}
