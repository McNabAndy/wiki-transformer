package cz.vojtechsika.wiki_transformer.service.image;

import cz.vojtechsika.wiki_transformer.exception.ExceptionHandler;
import cz.vojtechsika.wiki_transformer.util.FileNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService{

    private final ImageDownloader imageDownloader;

    private final ImageExtractor imageExtractor;

    private final ExceptionHandler exceptionHandler;

    @Autowired
    public ImageServiceImpl(ImageDownloader theImageDownloader, ImageExtractor theImageExtractor, ExceptionHandler theExceptionHandler) {
        imageDownloader = theImageDownloader;
        imageExtractor = theImageExtractor;
        exceptionHandler = theExceptionHandler;

    }



    @Override
    public void downloadAllImages( String url, Path filePath) throws IOException {

        // získání listu url
        List<String> imageUrls = imageExtractor.extractImageUrls(url);
        System.out.println(imageUrls);

        List<String> filteredImageUrls = imageExtractor.filteredImageUrls(imageUrls);
        System.out.println(filteredImageUrls);

        // musim si připravit kam budu stahovat - adresář
        Path downloadDir = filePath.resolve("download-img");
        try {
            Files.createDirectories(downloadDir);
            System.out.println("Složka pro obrázky připravena: " + downloadDir.toAbsolutePath());
        } catch (IOException e) {
            exceptionHandler.exitWithError("Nepodařilo se vytvořit složku: " + downloadDir.toAbsolutePath(), e);
        }

        // ted tu bude nejaký cyklus který mi postupně projde seznamem url a uloží je do cesty
        for (String imageUrl : filteredImageUrls) {

            // Vytvořím si cílovou cestu pro obráze dle url
            Path targetFilepath = downloadDir.resolve(FileNameUtil.getNameWithExtension(imageUrl));

            // stáhnu obrázek z Url
            byte[] imageData = imageDownloader.getImage(imageUrl).getBody();

            // uložím obrázek
            Files.write( targetFilepath, imageData);

            System.out.println("Obrázek : " + FileNameUtil.getNameWithExtension(imageUrl) + " uložen");
        }




    }



}
