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
     * Sends a GET request to the specified Redmine API URL and retrieves the response.
     *
     * @param url The URL from which to fetch data.
     * @return The response body containing the JSON data.
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
