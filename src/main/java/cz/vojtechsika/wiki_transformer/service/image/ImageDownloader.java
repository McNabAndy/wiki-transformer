package cz.vojtechsika.wiki_transformer.service.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ImageDownloader {

    private RestClient restClient;

    @Autowired
    public ImageDownloader(RestClient theRestClient) {
        this.restClient = theRestClient;
    }


    public ResponseEntity<byte[]> getImage(String imageUrl) {
        ResponseEntity<byte[]> response = restClient.get()
                .uri(imageUrl)
                .retrieve()
                .toEntity(byte[].class);

        return response;
    }


}
