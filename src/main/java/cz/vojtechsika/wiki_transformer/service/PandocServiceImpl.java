package cz.vojtechsika.wiki_transformer.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service implementation for handling Pandoc-based conversions.
 * This service provides methods to convert Textile-formatted text into MediaWiki format.
 */
@Service
public class PandocServiceImpl implements PandocService {


    /**
     * Directory path where converted files will be stored.
     */
    @Value("${file.storage.path}")
    private String outputDirectory;

    /**
     * Path object representing the storage location
     */
    private Path filePath;

    /**
     * Initializes the storage path after bean creation.
     */
    @PostConstruct
    private void initializeStoragePath(){
        try {
            filePath = Path.of(outputDirectory);

            // If the directory does not exist, it will be created automatically.
            Files.createDirectories(filePath);

            System.out.println("Output directory path : " + filePath.toAbsolutePath() +"\n");

        } catch (IOException e) {
            System.out.println("Error creating storage path: " + e.getMessage());
        }
    }

    /**
     * Converts Textile formatted text to MediaWiki format using Pandoc.
     *
     * @param content The Textile formatted text content to be converted.
     */
    @Override
    public void convertTextileToMediaWiki(String content) {
        try {

            // Create a temporary file for storing Textile data
            Path tempInputFile = Files.createTempFile(filePath,"tempInput",".textile");
            Files.writeString(tempInputFile, content);

            // Define the output file location
            Path outputFile = Path.of(outputDirectory, "wiki-page.mediawiki");

            // Execute external Pandoc process
            ProcessBuilder processBuilder = new ProcessBuilder("pandoc", "-f", "textile", "-t", "mediawiki", tempInputFile.toString(), "-o", outputFile.toString());

            // Start the conversion process
            Process process = processBuilder.start();

            // Wait for the Pandoc process to complete
            process.waitFor();

            // Remove the temporary input file
            Files.deleteIfExists(tempInputFile);  // možná toto dát do finally bloku

            System.out.println("Pandoc converted Textile to MediaWiki");

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error conversion was stopped: " + e.getMessage());
        }
    }


}
