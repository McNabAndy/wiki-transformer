package cz.vojtechsika.wiki_transformer.cli;

import cz.vojtechsika.wiki_transformer.dto.RedmineWikiResponseDTO;
import cz.vojtechsika.wiki_transformer.dto.WikiConversionContext;
import cz.vojtechsika.wiki_transformer.exception.ExceptionHandler;
import cz.vojtechsika.wiki_transformer.exception.RedmineFetchException;
import cz.vojtechsika.wiki_transformer.service.PandocService;
import cz.vojtechsika.wiki_transformer.service.RedmineService;
import cz.vojtechsika.wiki_transformer.service.image.ImageService;
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

    private final ImageService imageService;

    /**
     * Instance of {@link RedmineService}
     */
    private final RedmineService redmineService;

    /**
     * Instance of {@link PandocService}
     */
    private final  PandocService pandocService;

    /**
     *  Instance of {@link ExceptionHandler}
     */
    private final  ExceptionHandler exceptionHandler;

    /**
     * Path object representing the storage location
     */
    private Path filePath;

    /**
     * The URL of the Redmine Wiki page to be transformed.
     */
    @CommandLine.Option(names = "--url", description = "URL of the Redmine Wiki page", required = true)
    private String wikiUrl;

    /**
     *  CLI option for specifying the output directory where the converted MediaWiki file will be saved.
     */
    @CommandLine.Option(names = {"--output-dir", "-o"}, description = "File path for outputs", required = true )
    private String outputDirectory;


    /**
     * Constructor that initializes the required services.
     *
     * @param theRedmineService    the service responsible for fetching Redmine Wiki page data
     * @param thePandocService     the service responsible for converting content from Textile to MediaWiki format
     * @param theExceptionHandler  the centralized exception handler used to manage CLI errors and terminate gracefully
     */
    @Autowired
    public WikiTransformerCommand(RedmineService theRedmineService,
                                  PandocService thePandocService,
                                  ExceptionHandler theExceptionHandler,
                                  ImageService theImageService) {
        this.redmineService = theRedmineService;
        this.pandocService = thePandocService;
        this.exceptionHandler = theExceptionHandler;
        this.imageService = theImageService;
    }

    /**
     * Runs the transformation process by fetching the Redmine Wiki page and converting its content.
     */
    @Override
    public void run() {
        initializePath(outputDirectory);
        initializeOutputDirectory(filePath);

        WikiConversionContext context = getRedmineWikiPageContext();

        convertWikiPage(context);

        downloadImages(context);

    }

    /**
     * Fetches the Redmine Wiki page content and converts it to MediaWiki format.
     *
     */
    private WikiConversionContext getRedmineWikiPageContext() {
        // Construct the API endpoint for fetching the Redmine Wiki page
        String jsonWikiUrl = createJsonUrl(wikiUrl);
        // Fetch data from Redmine API
        RedmineWikiResponseDTO response = fetchRedmineWikiPage(jsonWikiUrl);


        // Create unique file name from title and add suffix
        String uniqueTitle = createUniqueTitle(response.getWikiPage().getTitle());
        // Extract the content from the response
        String contentWikiPage = response.getWikiPage().getText();

        // Create WikiConversionContext
        WikiConversionContext context = new WikiConversionContext();
        context.setUniqueTitle(uniqueTitle);
        context.setWikiText(contentWikiPage);
        context.setWikiUrl(wikiUrl);
        context.setFilePath(filePath);
        context.setOutputDir(outputDirectory);

        return context;
    }


    private void convertWikiPage(WikiConversionContext context) {
        // Convert and save the content
        runPandocService(context.getWikiText(), context.getUniqueTitle());
    }

    /**
     * Creates a unique, filesystem-safe title by sanitizing the input and appending a unique suffix.
     *
     * @param theTitle the original title to be sanitized
     * @return a unique and safe title string
     */
    private String createUniqueTitle(String theTitle) {
        String sanitizeTitle = FileNameUtil.sanitizeFileName(theTitle);
        return sanitizeTitle + "_" + FileNameUtil.createUniqueSuffix(wikiUrl);
    }


    /**
     * Initializes the output path from the provided string and validates it as a {@link Path}.
     *
     * @param outputDirectory the output directory path as a string, provided via CLI
     */
    private void initializePath(String outputDirectory) {
        try {
            filePath = Path.of(outputDirectory);   // nastaví třídě atribut filePath který pak mužu dál zpracovat
        } catch (InvalidPathException e) {
            exceptionHandler.exitWithError("The path can not be converted to a valid path: ", e);
        }
    }


    /**
     * Creates the output directory if it does not exist and ensures it is writable.
     *
     * @param filePath the path to the output directory
     */
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


    /**
     * Retrieves the content of a Redmine Wiki page in JSON format from the provided URL.
     *
     * @param jsonWikiUrl the full URL with .json suffix to fetch the wiki page data
     * @return the deserialized {@link RedmineWikiResponseDTO} containing wiki content
     */
    private RedmineWikiResponseDTO fetchRedmineWikiPage(String jsonWikiUrl) {
        try{
            return redmineService.getRedmine(jsonWikiUrl);
        } catch (RedmineFetchException e) {
            exceptionHandler.exitWithError("Failed to retrieve redmine wiki page", e);
            throw new RuntimeException("This line should never be reached");  // tento kod nidky nenastane, jen pro IDE a abych nemusel vracet null
        }
    }

    /**
     * Runs the conversion logic using {@link PandocService} to transform the wiki content.
     *
     * @param contentWikiPage the raw Textile content of the Redmine Wiki page
     * @param uniqueTitle     the sanitized and timestamped output file name
     */
    private void runPandocService(String contentWikiPage, String uniqueTitle) {
        try {
            pandocService.convertTextileToMediaWiki(contentWikiPage, uniqueTitle, filePath, outputDirectory);
        } catch (IOException e) {
            exceptionHandler.exitWithError("Failed during run Pandoc", e);
        }
    }

    /**
     * Builds a complete Redmine API URL by appending the .json suffix to the provided base URL.
     *
     * @param wikiUrl the original wiki page URL
     * @return the modified URL pointing to the JSON version of the page
     */
    private String createJsonUrl(String wikiUrl) {
        if (wikiUrl == null) {
            exceptionHandler.exitWithError("WikiUrl can not be null");
        }
        System.out.println("Fetching Redmine Wiki page from url: " + wikiUrl + ".json" + "\n");
        return wikiUrl + ".json";
    }


    /**
     * Checks whether the specified path is writable by attempting to create and delete a temporary file.
     *
     * @param filePath the directory to be validated for write permissions
     * @throws IOException if the directory is not writable or an error occurs during testing
     */
    private void checkWrite(Path filePath) throws IOException {
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


    private void downloadImages(WikiConversionContext context) {
        try {
            imageService.downloadAllImages(context);
        } catch (IOException e) {
            exceptionHandler.exitWithError("Error downloading images", e);
        }
    }

}



