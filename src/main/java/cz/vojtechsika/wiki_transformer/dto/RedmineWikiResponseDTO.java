package cz.vojtechsika.wiki_transformer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class representing a wrapper for the JSON response from the Redmine Wiki API.
 */

@Getter
@Setter
public class RedmineWikiResponseDTO {

    /**
     * The WikiPageDTO instance containing the details of the retrieved wiki page.
     */
    @JsonProperty("wiki_page")
    private WikiPageDTO wikiPage;


}
