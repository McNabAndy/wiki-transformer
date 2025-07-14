package cz.vojtechsika.wiki_transformer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileNameUtilTest {

    @Test
    @DisplayName("Sanitized file name")
    void sanitizeFileName_inputWithDiacriticsAndSymbols_returnsSanitized() {
        // Arrange
        String input = "   Článek: Moje fotky & videa (verze 2).jpg   ";
        String expected = "Clanek_Moje_fotky_videa_verze_2_.jpg";

        // Act
        String actual = FileNameUtil.sanitizeFileName(input);

        // Assert
        assertEquals(expected, actual, "Sanitize file name failed.");

    }

    @Test
    @DisplayName("Create unique suffix")
    void createUniqueSuffix_validUrl_returnsHashCodeAsString() {
        // Arrange
        String input = "https://example.com/page";
        int expectedHash = input.hashCode();
        String expected = String.valueOf(expectedHash);

        // Act
        String actual = FileNameUtil.createUniqueSuffix(input);

        // Assert
        assertEquals(expected, actual, "Should be same hash");

    }

    @Test
    @DisplayName("Extracts the file name from image url")
    void getNameWithExtension_validImageUrl_returnsFileNameWithExtension() {
        // Arrange
        String input = "https://example.com/page/image.jpg";
        String expected = "image.jpg";

        // Act
        String actual = FileNameUtil.getNameWithExtension(input);

        // Assert
        assertEquals(expected, actual, "Should be same name");
    }
}