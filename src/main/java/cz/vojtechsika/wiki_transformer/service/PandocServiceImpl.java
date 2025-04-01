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
     *
     * @param content the Textile-formatted text to be converted.
     * @param filePath   the path where the temporary input file will be created
     * @param outputDirectory the directory where the final converted MediaWiki file will be saved
     */
    @Override
    public void convertTextileToMediaWiki(String content, String fileName, Path filePath, String outputDirectory) {
        try {

            // Create a temporary file for storing Textile data
            Path tempInputFile = Files.createTempFile("wikiTempInput",".textile");
            Files.writeString(tempInputFile, content);


            // Define the output file location
            Path outputFile = Path.of(outputDirectory, fileName + ".mediawiki");

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
