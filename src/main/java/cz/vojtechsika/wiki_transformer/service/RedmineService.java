package cz.vojtechsika.wiki_transformer.service;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.exception.RedmineFetchException;

/**
 * Service interface for interacting with the Redmine API.
 * Provides methods to fetch wiki page data from Redmine.
 */
public interface RedmineService {

    /**
     * Fetches a Redmine Wiki page as a JSON response from the given URL.
     *
     * @param url the full URL (ending with {@code .json}) pointing to the Redmine Wiki page API endpoint
     * @return the deserialized {@link RedmineWikiResponseDTO} containing the page data
     * @throws RedmineFetchException if the fetch operation fails due to a network error, an invalid response,
     * or unexpected content from the Redmine API
     */
    RedmineWikiResponseDTO getRedmine(String url) throws RedmineFetchException;
}
