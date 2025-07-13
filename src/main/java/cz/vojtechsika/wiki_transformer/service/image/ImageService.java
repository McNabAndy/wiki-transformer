package cz.vojtechsika.wiki_transformer.service.image;

import cz.vojtechsika.wiki_transformer.dto.WikiConversionContext;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ImageService {

    void downloadAllImages(WikiConversionContext context) throws IOException;
}
