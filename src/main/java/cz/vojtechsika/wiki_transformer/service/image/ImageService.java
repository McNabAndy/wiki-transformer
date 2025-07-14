package cz.vojtechsika.wiki_transformer.service.image;

import cz.vojtechsika.wiki_transformer.dto.WikiConversionContext;

import java.io.IOException;

/**
 * Service interface for downloading all images referenced in a wiki conversion context.
 */
public interface ImageService {

    /**
     * Downloads every image found in the given context (from wiki url)
     * and saves them into a folder named after the context’s unique title.
     *
     * @param context contains the wiki URL, output path, and unique folder name
     * @throws IOException if any error occurs during download or file writing
     */
    void downloadAllImages(WikiConversionContext context) throws IOException;
}
