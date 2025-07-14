package cz.vojtechsika.wiki_transformer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the Data Transfer Object (DTO) for an author.
 * This class is a nested JSON object within the {@link WikiPageDTO}.
 */

@Getter
@Setter
public class AuthorDTO {

    /**
     * Author id
     */
    private int id;

    /**
     * Name of author
     */
    private String name;


}
