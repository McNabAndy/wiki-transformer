package cz.vojtechsika.wiki_transformer.service;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.exception.RedmineFetchException;

/**
 * Service interface for interacting with the Redmine API.
 * Provides methods to fetch wiki page data from Redmine.
 */
public interface RedmineService {

    /**
     * Retrieves wiki page data from a specified Redmine URL.
     *
     * @param url the Redmine API endpoint to fetch data from.
     * @return a {@link RedmineWikiResponseDTO} containing the retrieved wiki page data.
     */
    RedmineWikiResponseDTO getRedmine(String url) throws RedmineFetchException;
}
