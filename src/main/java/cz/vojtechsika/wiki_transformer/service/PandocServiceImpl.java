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
     * Converts content from Textile format to MediaWiki format and saves the result as a file.
     *
     * @param content          the Textile-formatted text to be converted
     * @param fileName         the sanitized and unique name of the output file (without extension)
     * @param filePath         the path to the output directory
     * @param outputDirectory  the output directory as a string, used for final file generation
     * @throws IOException if any I/O error occurs during the conversion process
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


    /**
     * Creates a temporary file in the system's temporary directory and writes the Textile content into it.
     *
     * @param content the content to write into the temporary file
     * @return the path to the created temporary file
     * @throws IOException if the file cannot be created or written
     */
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


    /**
     * Creates the final output file path by combining the target directory and file name.
     *
     * @param outputDirectory the output directory as a string
     * @param fileName        the name of the output file (without extension)
     * @return the full path to the output file
     * @throws IOException if the path is invalid or cannot be created
     */
    private Path createOutputFile(String outputDirectory, String fileName) throws IOException {
        try {
            return Path.of(outputDirectory, fileName + ".mediawiki");
        } catch (IllegalArgumentException e){
            throw new IOException("Invalid file name or format when pandoc try write to output  directory ", e);
        } catch (UnsupportedOperationException e) {
            throw new IOException("Unsupported operation when pandoc try write to output  directory", e);
        }
    }


    /**
     * Executes the Pandoc command-line tool to convert the content from Textile to MediaWiki format.
     *
     * @param tempInputFile the path to the temporary input file
     * @param outputFile    the path to the output file to be generated
     * @throws IOException if Pandoc cannot be executed or fails during processing
     */
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

    /**
     * Deletes the temporary input file after conversion is complete.
     *
     * @param tempInputFile the path to the temporary file to be deleted
     * @throws IOException if the file cannot be deleted
     */
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
