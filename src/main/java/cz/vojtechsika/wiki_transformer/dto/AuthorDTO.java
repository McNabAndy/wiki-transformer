package cz.vojtechsika.wiki_transformer.dto;

/**
 * Represents the Data Transfer Object (DTO) for an author.
 * This class is a nested JSON object within the {@link WikiPageDTO}.
 */

public class AuthorDTO {


    private int id;

    private String name;


    // Getter and Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
