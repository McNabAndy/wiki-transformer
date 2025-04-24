package cz.vojtechsika.wiki_transformer.cli;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.exception.ExceptionHandler;
import cz.vojtechsika.wiki_transformer.exception.RedmineFetchException;
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
     *  Onstance of {@link ExceptionHandler}
     */
    private ExceptionHandler exceptionHandler;

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
    public WikiTransformerCommand(RedmineService theRedmineService, PandocService thePandocService, ExceptionHandler theExceptionHandler) {
        this.redmineService = theRedmineService;
        this.pandocService = thePandocService;
        this.exceptionHandler = theExceptionHandler;
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
        String jsonWikiUrl = createJsonUrl(wikiUrl);

        // Fetch data from Redmine API
        RedmineWikiResponseDTO response = fetchRedmineWikiPage(jsonWikiUrl);

        // Create unique file name from title and add suffix
        String uniqueTitle = createUniqueTitle(response.getWikiPage().getTitle());    // tady mi kompilátor hlásí že by mohla nastat vyjimka NullPointerException, kvuli tomu že response muže být dle kodu null (ve skutečnosti nikdy nemuže  být null protože mi to handler ukončí...)

        // Extract the content from the response
        String contentWikiPage = response.getWikiPage().getText();

        // Convert and save the content
        runPandocService(contentWikiPage, uniqueTitle);
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



    private void initializePath(String outputDirectory) {
        try {
            filePath = Path.of(outputDirectory);   // nastaví třídě atribut Path který pak mužu dál zpracovat
        } catch (InvalidPathException e) {
            exceptionHandler.exitWithError("The path can not be converted to a valid path: ", e);
        }
    }


    private void initializeOutputDirectory(Path filePath){
        try {
            // If the directory does not exist, it will be created automatically.
            Files.createDirectories(filePath);
            System.out.println("Output directory path : " + filePath.toAbsolutePath() +"\n");
            checkWrite(filePath);  // ruční kontrola zda lze do uvedené cesty opravdu zapisovat, pokud ne ukončí mi to program
        } catch (FileAlreadyExistsException e) {
            exceptionHandler.exitWithError("The path exists but it is not directory", e);
        } catch (SecurityException e) {
            exceptionHandler.exitWithError("You do not have permission to write to the output directory", e);
        } catch (UnsupportedOperationException e){
            exceptionHandler.exitWithError("Unsupported operation while writing to the output directory ", e);
        } catch (IOException e) {
            exceptionHandler.exitWithError("Failed to create or write to the specified output path", e);
        }
    }

    private RedmineWikiResponseDTO fetchRedmineWikiPage(String jsonWikiUrl) {
        try{
            return redmineService.getRedmine(jsonWikiUrl);
        } catch (RedmineFetchException e) {
            exceptionHandler.exitWithError("Failed to retrieve redmine wiki page", e);
            return null;  //  tady tu část musím ještě najak poladit , tento krok se nikdy neučiní, jen to umlčuje kompilátor protože tam musí být return, protože má metoda navratový typ
        }
    }


    private void runPandocService(String contentWikiPage, String uniqueTitle) {
        try {
            pandocService.convertTextileToMediaWiki(contentWikiPage, uniqueTitle, filePath, outputDirectory);
        } catch (IOException e) {
            exceptionHandler.exitWithError("Failed during run Pandoc", e);
        }
    }

    private String createJsonUrl(String wikiUrl) {
        if (wikiUrl == null) {
            exceptionHandler.exitWithError("WikiUrl can not be null");
        }
        System.out.println("Fetching Redmine Wiki page from url: " + wikiUrl + ".json" + "\n");
        return wikiUrl + ".json";
    }


    private void checkWrite(Path filePath) throws IOException {  // nemel bych zde osšetřit  i tu IvalidPAthException ?
        Path testPath = filePath.resolve("test.txt");
        try {
            Files.createFile(testPath);
            Files.deleteIfExists(testPath);
        } catch (IOException e) {
            throw new IOException("Failed to write to the specified output path: " + filePath.toAbsolutePath(), e);
        } catch (SecurityException e) {
            throw new IOException("Write access denied for the directory: " + filePath.toAbsolutePath(), e);
        }

    }

}



