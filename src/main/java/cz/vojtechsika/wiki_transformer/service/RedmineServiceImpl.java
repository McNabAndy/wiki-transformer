package cz.vojtechsika.wiki_transformer.service;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.exception.RedmineFetchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Service class responsible for handling communication with the Redmine API.
 */

@Service
public class RedmineServiceImpl implements RedmineService {

    /**
     * RestClient instance for making HTTP requests.
     */
    private RestClient restClient;


    /**
     * Constructor-based dependency injection.
     *
     * @param theRestClient The RestClient instance to be injected.
     */
    @Autowired
    public RedmineServiceImpl(RestClient theRestClient) {
        this.restClient = theRestClient;
    }


    /**
     * Retrieves a Redmine Wiki page from the specified JSON endpoint and deserializes it into a DTO.
     * This method sends a GET request to the provided URL, expecting a JSON response that represents
     * a Redmine Wiki page. The response body is mapped to a {@link RedmineWikiResponseDTO} object.
     *
     * @param url the full URL of the Redmine Wiki page with the {@code .json} suffix
     * @return the parsed {@link RedmineWikiResponseDTO} containing the wiki page data
     * @throws RedmineFetchException if the wiki page is not found (404), or if any communication or
     * deserialization error occurs while calling the Redmine API
     */
    @Override
    public RedmineWikiResponseDTO getRedmine(String url) throws RedmineFetchException {
        try{
            ResponseEntity<RedmineWikiResponseDTO> response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(RedmineWikiResponseDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RedmineFetchException("Wiki page not found", e);
        } catch (RestClientException e) {
            throw new RedmineFetchException("Communication error with the server", e);
        }
    }
}
