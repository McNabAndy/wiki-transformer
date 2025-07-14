package cz.vojtechsika.wiki_transformer.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@NoArgsConstructor
public class PathValidationService {


    /**
     * Checks whether the specified path is writable by attempting to create and delete a temporary file.
     * @param filePath the directory to be validated for write permissions
     * @throws IOException if the directory is not writable or an error occurs during testing
     */
    public void checkWrite(Path filePath) throws IOException {
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
