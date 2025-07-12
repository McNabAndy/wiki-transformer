package cz.vojtechsika.wiki_transformer.service.image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageExtractor {


    public ImageExtractor() {

    }

    /** Stáhne HTML a vrátí Jsoup Document */
    private Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    /** Vrátí všechny <img> elementy z dokumentu */
    public Elements extractElements(String url) throws IOException {
        return fetchDocument(url).select("img");
    }

    /** Vrátí seznam absolutních URL obrázků */
    public List<String> extractImageUrls(String url) throws IOException {
        return extractElements(url).eachAttr("abs:src");
    }

    public List<String> filteredImageUrls(List<String> extractedUrls) throws IOException {
        return extractedUrls.stream()
                .filter(url -> !url.contains("/attachments/thumbnail/"))
                .collect(Collectors.toList());
    }

}
