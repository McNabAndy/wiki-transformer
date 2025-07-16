package cz.vojtechsika.wiki_transformer.service.image;

import cz.vojtechsika.wiki_transformer.exception.ImageFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageDownloaderTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private ImageDownloader imageDownloader;



    @BeforeEach
    void setUp() {
        imageDownloader = new ImageDownloader(restClient);
    }

    @Test
    @DisplayName("Download image request")
    void getImage_validUrl_returnsResponseWithBytes() throws ImageFetchException {
        // Arrange
        byte[] expectedBytes = new byte[]{1, 2, 3};
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(expectedBytes);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(byte[].class)).thenReturn(responseEntity);

        // Act
        ResponseEntity<byte[]> actual = imageDownloader.getImage("https://example.com/page/image.jpg");

        // Assert
        assertEquals(expectedBytes, actual.getBody(), "Should return the expected bytes");

    }

    @Test
    @DisplayName("Throw ImageFetchException on HttpClientErrorException")
    void getImage_onHttpClientError_shouldThrowImageFetchException() {
        // Arrange
        when(restClient.get()).thenThrow(HttpClientErrorException.NotFound.class);

        // Act and Assert
        assertThrows(ImageFetchException.class, () ->
                imageDownloader.getImage("https://example.com/page/image.jpg"), "Should throw HttpClientErrorException.NotFound");

    }

    @Test
    @DisplayName("Throw ImageFetchException on RestClientException")
    void getImage_onRestClientException_shouldThrowImageFetchException() {
        // Arrange
        when(restClient.get()).thenThrow(RestClientException.class);

        // Act and Assert
        assertThrows(ImageFetchException.class, () ->
                imageDownloader.getImage("https://example.com/page/image.jpg"), "Should throw RestClientException");

    }

}


