package cz.vojtechsika.wiki_transformer.service.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


/**
 * Service responsible for downloading raw image bytes from a given URL.
 */

@Service
public class ImageDownloader {

    /**
     * The RestClient used to perform HTTP GET requests
     */
    private final RestClient restClient;


    /**
     * Constructs an ImageDownloader with the provided RestClient.
     *
     * @param theRestClient the RestClient used to perform HTTP GET requests
     */
    @Autowired
    public ImageDownloader(RestClient theRestClient) {
        this.restClient = theRestClient;
    }


    /**
     * Sends an HTTP GET request to the specified image URL and returns
     * the full {@link ResponseEntity} containing the image data as a byte array.
     * <p>
     * The caller can inspect the status code and headers before processing the body.
     * </p>
     *
     * @param imageUrl the absolute URL of the image to download
     * @return a ResponseEntity whose body is the raw image bytes
     */
    public ResponseEntity<byte[]> getImage(String imageUrl) {
        ResponseEntity<byte[]> response = restClient.get()
                .uri(imageUrl)
                .retrieve()
                .toEntity(byte[].class);

        return response;
    }


}
