package cz.vojtechsika.wiki_transformer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * DTO class representing the main structure to which JSON response data from Redmine Wiki is bound.
 */
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


    // Getter and Setter region

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
