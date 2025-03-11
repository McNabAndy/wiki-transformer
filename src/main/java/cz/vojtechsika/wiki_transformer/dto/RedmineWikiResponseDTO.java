package cz.vojtechsika.wiki_transformer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO class representing a wrapper for the JSON response from the Redmine Wiki API.
 */
public class RedmineWikiResponseDTO {

    /**
     * The WikiPageDTO instance containing the details of the retrieved wiki page.
     */
    @JsonProperty("wiki_page")
    private WikiPageDTO wikiPage;


    // Getter and Setter region

    public WikiPageDTO getWikiPage() {
        return wikiPage;
    }

    public void setWikiPage(WikiPageDTO wikiPage) {
        this.wikiPage = wikiPage;
    }
}
