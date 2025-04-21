import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditorSettings class functionality.
 */
public class EditorSettingsTest {
    
    private EditorSettings settings;
    private File configFile;
    
    @BeforeEach
    void setUp() throws Exception {
        // Set the CONFIG_FILE field to a temporary file for testing
        configFile = new File("test_editor_settings.properties");
        EditorSettings.setConfigFile(configFile.getName());
        settings = new EditorSettings();
    }
    
    @AfterEach
    void tearDown() {
        // Clean up the test config file
        if (configFile.exists()) {
            configFile.delete();
        }
    }
    
    @Test
    void testDefaultSettings() {
        assertTrue(settings.isSyntaxHighlighting());
        assertFalse(settings.isAutoSave());
        assertEquals(4, settings.getIndentSize());
        assertEquals("Monospace", settings.getFontName());
        assertEquals(12, settings.getFontSize());
        assertEquals("Light", settings.getTheme());
        assertTrue(settings.isWordWrap());
    }
    
    @Test
    void testCustomSettings() {
        EditorSettings customSettings = new EditorSettings(
            false, // syntaxHighlighting
            true,  // autoSave
            2,     // indentSize
            "Courier", // fontName
            14,    // fontSize
            "Dark", // theme
            false  // wordWrap
        );
        
        assertFalse(customSettings.isSyntaxHighlighting());
        assertTrue(customSettings.isAutoSave());
        assertEquals(2, customSettings.getIndentSize());
        assertEquals("Courier", customSettings.getFontName());
        assertEquals(14, customSettings.getFontSize());
        assertEquals("Dark", customSettings.getTheme());
        assertFalse(customSettings.isWordWrap());
    }
    
    @Test
    void testSaveAndLoadSettings() {
        // Modify default settings
        settings.setSyntaxHighlighting(false);
        settings.setAutoSave(true);
        settings.setIndentSize(2);
        settings.setFontName("Arial");
        settings.setFontSize(16);
        settings.setTheme("Dark");
        settings.setWordWrap(false);
        
        // Save settings
        assertTrue(settings.saveSettings());
        
        // Create a new settings object
        EditorSettings loadedSettings = new EditorSettings();
        
        // Load settings from file
        assertTrue(loadedSettings.loadSettings());
        
        // Verify loaded settings match saved settings
        assertFalse(loadedSettings.isSyntaxHighlighting());
        assertTrue(loadedSettings.isAutoSave());
        assertEquals(2, loadedSettings.getIndentSize());
        assertEquals("Arial", loadedSettings.getFontName());
        assertEquals(16, loadedSettings.getFontSize());
        assertEquals("Dark", loadedSettings.getTheme());
        assertFalse(loadedSettings.isWordWrap());
    }
    
    @Test
    void testResetToDefaults() {
        // Modify default settings
        settings.setSyntaxHighlighting(false);
        settings.setAutoSave(true);
        settings.setIndentSize(2);
        settings.setFontName("Arial");
        settings.setFontSize(16);
        settings.setTheme("Dark");
        settings.setWordWrap(false);
        
        // Reset to defaults
        settings.resetToDefaults();
        
        // Verify settings are reset to defaults
        assertTrue(settings.isSyntaxHighlighting());
        assertFalse(settings.isAutoSave());
        assertEquals(4, settings.getIndentSize());
        assertEquals("Monospace", settings.getFontName());
        assertEquals(12, settings.getFontSize());
        assertEquals("Light", settings.getTheme());
        assertTrue(settings.isWordWrap());
    }

    @Test
    void testSettersWithInvalidValues() {
        // Test setting invalid indentSize
        settings.setIndentSize(-1);
        assertEquals(4, settings.getIndentSize()); // Should remain unchanged
        
        // Test setting invalid fontSize
        settings.setFontSize(0);
        assertEquals(12, settings.getFontSize()); // Should remain unchanged
        
        // Test setting invalid fontName
        settings.setFontName("");
        assertEquals("Monospace", settings.getFontName()); // Should remain unchanged
        
        settings.setFontName(null);
        assertEquals("Monospace", settings.getFontName()); // Should remain unchanged
        
        // Test setting invalid theme
        settings.setTheme("");
        assertEquals("Light", settings.getTheme()); // Should remain unchanged
        
        settings.setTheme(null);
        assertEquals("Light", settings.getTheme()); // Should remain unchanged
    }
} 