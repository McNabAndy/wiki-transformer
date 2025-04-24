package cz.vojtechsika.wiki_transformer.service;
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
    public void convertTextileToMediaWiki(String content, String fileName, Path filePath, String outputDirectory) throws IOException {

            // Create a temporary file for storing Textile data
            Path tempInputFile = createTempFile(content);

            // Define the output file location
            Path outputFile = createOutputFile(outputDirectory, fileName);

            // Run Pandoc process
            runPandoc(tempInputFile, outputFile);

            // Remove the temporary input file
            deleteTempFile(tempInputFile);

            System.out.println("Pandoc converted Textile to MediaWiki");

    }


    private Path createTempFile(String content) throws IOException {
        try{
            Path tempInputFile = Files.createTempFile("wikiTempInput",".textile");
            Files.writeString(tempInputFile, content);
            return tempInputFile;

        }  catch (IllegalArgumentException e){
            throw new IOException("Invalid file name or format when writing to system´s temporary directory ", e);
        } catch (SecurityException e){
            throw new IOException("Permission denied, you cannot write to system´s temporary directory ", e);
        } catch (UnsupportedOperationException e){
            throw new IOException("Unsupported operation while writing to system´s temporary directory ", e);
        } catch (IOException e) {
            throw new IOException("I/O error while writing to the system's temporary directory", e);
        }
    }

    private Path createOutputFile(String outputDirectory, String fileName) throws IOException {
        try {
            return Path.of(outputDirectory, fileName + ".mediawiki");
        } catch (IllegalArgumentException e){
            throw new IOException("Invalid file name or format when pandoc try write to output  directory ", e);
        } catch (UnsupportedOperationException e) {
            throw new IOException("Unsupported operation when pandoc try write to output  directory", e);
        }
    }

    private void runPandoc(Path tempInputFile, Path outputFile) throws IOException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("pandoc", "-f", "textile", "-t", "mediawiki", tempInputFile.toString(), "-o", outputFile.toString());

            // Start the conversion process
            Process process = processBuilder.start();

            // Wait for the Pandoc process to complete
            process.waitFor();
        } catch (NullPointerException e) {
            throw new IOException("Null argument found in Pandoc command list", e);
        } catch (IndexOutOfBoundsException e) {
            throw new IOException("Pandoc cannot be executed because the command is incomplete or empty", e);
        } catch (SecurityException e) {
            throw new IOException("Pandoc could not be started due to insufficient permissions", e);
        } catch (UnsupportedOperationException e) {
            throw new IOException("Your system does not allow running external tools such as Pandoc", e);
        } catch (IllegalArgumentException e) {
            throw new IOException("Illegal argument passed to Pandoc command", e);
        } catch (InterruptedException e) {
            throw new IOException("Pandoc was interrupted", e);
        } catch (IOException e) {
            throw new IOException("I/O error while executing Pandoc command", e);
        }

    }

    private void deleteTempFile(Path tempInputFile) throws IOException {
        try {
            Files.deleteIfExists(tempInputFile);
        } catch (SecurityException e) {
            throw new IOException("Permission denied, you cannot delete the temporary file after Pandoc conversion ", e);
        } catch (IOException e) {
            throw new IOException("I/O error while deleting the temporary file after Pandoc conversion", e);
        }
    }

}
