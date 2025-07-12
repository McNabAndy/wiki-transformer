package cz.vojtechsika.wiki_transformer.service.image;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ImageService {

    void downloadAllImages(String url, Path filePath) throws IOException;
}
