package cz.vojtechsika.wiki_transformer.service.image;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service for extracting image references from an HTML page.
 * <p>
 * Uses Jsoup to fetch and parse a document, then finds all &lt;img&gt; tags
 * and returns their absolute URLs or raw Elements.
 * </p>
 */
@Service
@NoArgsConstructor
public class ImageExtractor {



    /**
     * Fetches the HTML document at the given URL.
     *
     * @param url the page URL to fetch
     * @return a Jsoup {@link Document} representing the page
     * @throws IOException if the page cannot be retrieved or parsed
     */
    private Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    /**
     * Selects all &lt;img&gt; elements from the page at the given URL.
     *
     * @param url the page URL to scan for &lt;img&gt; tags
     * @return an {@link Elements} collection of all &lt;img&gt; elements
     * @throws IOException if the page cannot be retrieved or parsed
     */
    public Elements extractElements(String url) throws IOException {
        return fetchDocument(url).select("img");
    }

    /**
     * Extracts the absolute URLs of all images on the page.
     *
     * @param url the page URL to scan
     * @return a list of absolute image URLs (the src attribute)
     * @throws IOException if the page cannot be retrieved or parsed
     */
    public List<String> extractImageUrls(String url) throws IOException {
        return extractElements(url).eachAttr("abs:src");
    }

    /**
     * Filters out unwanted URLs, such as thumbnail endpoints.
     *
     * @param extractedUrls a list of image URLs to filter
     * @return a new list containing only URLs that do not match thumbnail patterns
     */
    public List<String> filteredImageUrls(List<String> extractedUrls) throws IOException {
        return extractedUrls.stream()
                .filter(url -> !url.contains("/attachments/thumbnail/"))
                .collect(Collectors.toList());
    }

}
