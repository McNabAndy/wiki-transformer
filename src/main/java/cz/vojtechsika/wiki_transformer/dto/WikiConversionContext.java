package cz.vojtechsika.wiki_transformer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WikiConversionContext {

    private String uniqueTitle;

    private String wikiText;

    private Path filePath;

    private String wikiUrl;

    private String outputDir;
}
