package cz.vojtechsika.wiki_transformer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathValidationServiceTest {

    private PathValidationService pathValidationService;

    @BeforeEach
    void setUp() {
        pathValidationService = new PathValidationService();
    }

    @Test
    @DisplayName("Does not throw when output path is writable")
    void checkWrite_validPath_doesNotThrowAnyException() throws IOException {
        // Arrange
        Path pathFolder = Path.of("some/path");
        Path testPath = pathFolder.resolve("test.txt");

        //  I have to always use try-with-resources for MockedStatic! - DON´T FORGET
        // Ensures the mock is properly closed after the test and doesn't affect other tests.
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {

            mockedFiles.when(() -> Files.createFile(testPath)).thenReturn(testPath);
            mockedFiles.when(() -> Files.deleteIfExists(testPath)).thenReturn(true);

            // Act and Assert
            assertDoesNotThrow(() -> pathValidationService.checkWrite(pathFolder),
                    "Should don't throw when output path is writable");
        }
    }

    @Test
    @DisplayName("Throw IOException on IOException")
    void checkWrite_onIOException_throwsIOException() throws IOException {
        // Arrange
        Path pathFolder = Path.of("some/path");
        Path testPath = pathFolder.resolve("test.txt");

        //  I have to always use try-with-resources for MockedStatic! - DON´T FORGET
        // Ensures the mock is properly closed after the test and doesn't affect other tests.
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {

            mockedFiles.when(() -> Files.createFile(testPath)).thenThrow(IOException.class);

            // Act and Assert
            assertThrows(IOException.class, () -> pathValidationService.checkWrite(pathFolder),
                    "On IOException Should throw IOException");
        }
    }

    @Test
    @DisplayName("Throw IOException on SecurityException")
    void checkWrite_onSecurityException_throwsIOException() throws IOException {
        // Arrange
        Path pathFolder = Path.of("some/path");
        Path testPath = pathFolder.resolve("test.txt");

        //  I have to always use try-with-resources for MockedStatic! - DON´T FORGET
        // Ensures the mock is properly closed after the test and doesn't affect other tests.
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {

            mockedFiles.when(() -> Files.createFile(testPath)).thenThrow(SecurityException.class);

            // Act and Assert
            assertThrows(IOException.class, () -> pathValidationService.checkWrite(pathFolder),
                    "On SecurityException Should throw IOException");
        }
    }
}