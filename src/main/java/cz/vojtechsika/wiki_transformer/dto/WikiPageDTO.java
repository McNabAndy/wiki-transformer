package cz.vojtechsika.wiki_transformer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO class representing the main structure to which JSON response data from Redmine Wiki is bound.
 */

@Getter
@Setter
public class WikiPageDTO {

    /**
     * Title of the wiki page.
     */
    private String title;

    /**
     * Content of the wiki page in textile format
     */
    private String text;

    /**
     * Version number of the wiki page.
     */
    private int version;

    /**
     * Author information of the wiki page.
     */
    @JsonProperty("author")
    private AuthorDTO author;

    /**
     * Comments associated with the wiki page.
     */
    private String comments;

    /**
     * Timestamp when the wiki page was created.
     */
    @JsonProperty("created_on")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdOn;

    /**
     * Timestamp when the wiki page was last updated.
     */
    @JsonProperty("updated_on")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedOn;

}
