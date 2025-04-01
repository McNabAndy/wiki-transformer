package cz.vojtechsika.wiki_transformer.cli;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.service.PandocService;
import cz.vojtechsika.wiki_transformer.service.RedmineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

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
     * The URL of the Redmine Wiki page to be transformed.
     */
    @CommandLine.Option(names = "--url", description = "URL of the Redmine Wiki page", required = true)
    private String wikiUrl;


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

        // Extract the content from the response
        String contentWikiPage = response.getWikiPage().getText();

        // Convert and save the content
        pandocService.convertTextileToMediaWiki(contentWikiPage);

    }
}
