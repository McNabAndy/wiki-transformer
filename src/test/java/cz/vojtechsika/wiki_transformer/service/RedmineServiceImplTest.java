package cz.vojtechsika.wiki_transformer.service;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.exception.ImageFetchException;
import cz.vojtechsika.wiki_transformer.exception.RedmineFetchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedmineServiceImplTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private RedmineService redmineService;

    @BeforeEach
    void setUp() {
        redmineService = new RedmineServiceImpl(restClient);
    }

    @Test
    @DisplayName("Download wiki page")
    void getRedmine_validWikiPageUrl_returnsResponseEntityWithRedmineWikiResponseDTO() throws RedmineFetchException {
        // Arrange
        RedmineWikiResponseDTO redmineWikiResponseDTO = new RedmineWikiResponseDTO();
        ResponseEntity<RedmineWikiResponseDTO> responseEntity = ResponseEntity.ok(redmineWikiResponseDTO);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(RedmineWikiResponseDTO.class)).thenReturn(responseEntity);


        // Act
        RedmineWikiResponseDTO actual = redmineService.getRedmine("https://example.com/page");

        // Assert
        assertEquals(responseEntity.getBody(), actual, "Should be the same object");
    }


    @Test
    @DisplayName("Throw RedmineFetchException on HttpClientErrorException")
    void getImage_onHttpClientError_shouldThrowRedmineFetchException() {
        // Arrange
        when(restClient.get()).thenThrow(HttpClientErrorException.NotFound.class);

        // Act and Assert
        assertThrows(RedmineFetchException.class, () ->
                redmineService.getRedmine("https://example.com/page"), "Should throw HttpClientErrorException.NotFound");

    }

    @Test
    @DisplayName("Throw RedmineFetchException on RestClientException")
    void getImage_onRestClientException_shouldThrowRedmineFetchException() {
        // Arrange
        when(restClient.get()).thenThrow(RestClientException.class);

        // Act and Assert
        assertThrows(RedmineFetchException.class, () ->
                redmineService.getRedmine("https://example.com/page"), "Should throw RestClientException");

    }
}