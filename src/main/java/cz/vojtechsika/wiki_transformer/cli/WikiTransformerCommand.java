package cz.vojtechsika.wiki_transformer.cli;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.service.PandocService;
import cz.vojtechsika.wiki_transformer.service.RedmineService;
import cz.vojtechsika.wiki_transformer.util.FileNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * CLI command for transforming a Redmine Wiki page written in Textile format to MediaWiki format.
 * This class fetches content from a Redmine Wiki page and converts it using Pandoc.
 */
@Component
@CommandLine.Command(name="wiki-transformer", description = "Transforms Textile file to MediaWiki")
public class WikiTransformerCommand implements Runnable {


    /**
     * Instance of {@link RedmineService}
     */
    private final RedmineService redmineService;

    /**
     * Instance of {@link PandocService}
     */
    private final  PandocService pandocService;

    /**
     * Path object representing the storage location
     */
    private Path filePath;

    /**
     * The URL of the Redmine Wiki page to be transformed.
     */
    @CommandLine.Option(names = "--url", description = "URL of the Redmine Wiki page", required = true)
    private String wikiUrl;

    @CommandLine.Option(names = {"--output-dir", "-o"}, description = "File path for outputs", required = true )
    private String outputDirectory;


    /**
     * Constructor that initializes the required services.
     *
     * @param theRedmineService The service responsible for fetching Redmine wiki pages.
     * @param thePandocService  The service responsible for converting Textile to MediaWiki format.
     */
    @Autowired
    public WikiTransformerCommand(RedmineService theRedmineService, PandocService thePandocService) {
        this.redmineService = theRedmineService;
        this.pandocService = thePandocService;
    }

    /**
     * Runs the transformation process by fetching the Redmine Wiki page and converting its content.
     */
    @Override
    public void run() {
        initializePath(outputDirectory);
        initializeOutputDirectory(filePath);
        getRedmineWikiPage(redmineService, pandocService);
        System.exit(0); // Zde to ukončí JVM po dokončení příkazu - musím ještě ošetřit vyjímky
    }

    /**
     * Fetches the Redmine Wiki page content and converts it to MediaWiki format.
     *
     * @param redmineService The service used to fetch the Redmine Wiki page.
     * @param pandocService  The service used to convert Textile format to MediaWiki format.
     */
    private void getRedmineWikiPage(RedmineService redmineService, PandocService pandocService) {

        // Construct the API endpoint for fetching the Redmine Wiki page
        String jsonWikiUrl = wikiUrl + ".json";
        System.out.println("Fetching Redmine Wiki page from url: " + jsonWikiUrl + "\n");

        // Fetch data from Redmine API
        RedmineWikiResponseDTO response = redmineService.getRedmine(jsonWikiUrl);

        // Create unique file name from title and add suffix
        String uniqueTitle = createUniqueTitle(response.getWikiPage().getTitle());


        // Extract the content from the response
        String contentWikiPage = response.getWikiPage().getText();

        // Convert and save the content
        pandocService.convertTextileToMediaWiki(contentWikiPage, uniqueTitle, filePath, outputDirectory);

    }

    /**
     * Creates a unique, filesystem-safe title by sanitizing the input and appending a unique suffix.
     *
     * @param theTitle the original title to be sanitized
     * @return a unique and safe title string
     */
    private String createUniqueTitle(String theTitle) {
        String sanitizeTitle = FileNameUtil.sanitizeFileName(theTitle);
        return sanitizeTitle + "_" + FileNameUtil.createUniqueSuffix();
    }



    // Vytvořit metodu pro kontrolu a ověření cesty tu apka mužu rovnou předat Pandocu - SoC princip - zodpovednosta za to že existuje správná cesta nese CLI třída Pandoc už se postará pouze o převod => jedná se očistčí



    private void initializePath(String outputDirectory) {
        try {
            filePath = Path.of(outputDirectory);   // nastaví třídě atribut Path který pak mužu dál zpracovat
        } catch (InvalidPathException e) {
            System.out.println("Error: The path can not be converted to a valid path: " + e.getMessage());
            System.exit(1);
        }
    }


    private void initializeOutputDirectory(Path filePath){
        try {
            // If the directory does not exist, it will be created automatically.
            Files.createDirectories(filePath);
            System.out.println("Output directory path : " + filePath.toAbsolutePath() +"\n");
        } catch (FileAlreadyExistsException e) {
            System.out.println("Error: The path exists but it is not directory  " + e.getMessage());
            System.exit(1);
        } catch (SecurityException e) {
            System.out.println("Error: You do not have permission to write to the output directory " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: creating output directory: " + e.getMessage());
            System.exit(1);
        }
    }



}



