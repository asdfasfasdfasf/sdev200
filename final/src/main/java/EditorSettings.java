import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * EditorSettings class manages the config settings for the code editor.
 * Includes options for syntax highlighting, auto-save, theme, indendation, etc.
 */
public class EditorSettings {
    private boolean syntaxHighlighting;
    private boolean autoSave;
    private int indentSize;
    private String fontName;
    private int fontSize;
    private String theme;
    private boolean wordWrap;
    
    private static String CONFIG_FILE = "editor_settings.properties";
    private static final String DEFAULT_FONT = "Monospace";
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final String DEFAULT_THEME = "Light";
    
    /**
     * Set the config file path (for testing purposes).
     */
    public static void setConfigFile(String configFile) {
        CONFIG_FILE = configFile;
    }
    
    /**
     * Constructor with default settings.
     */
    public EditorSettings() {
        this.syntaxHighlighting = true;
        this.autoSave = false;
        this.indentSize = 4;
        this.fontName = DEFAULT_FONT;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.theme = DEFAULT_THEME;
        this.wordWrap = true;
    }
    
    /**
     * Constructor with customized settings.
     * 
     * @param syntaxHighlighting Whether syntax highlighting is enabled
     * @param autoSave Whether auto-save is enabled
     * @param indentSize Size of indentation in spaces
     * @param fontName Name of the editor font
     * @param fontSize Size of the editor font
     * @param theme Editor theme name
     * @param wordWrap Whether word wrap is enabled
     */
    public EditorSettings(
        boolean syntaxHighlighting, boolean autoSave, int indentSize,
        String fontName, int fontSize, String theme, boolean wordWrap
    ) {
        this.syntaxHighlighting = syntaxHighlighting;
        this.autoSave = autoSave;
        this.indentSize = indentSize;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.theme = theme;
        this.wordWrap = wordWrap;
    }
    
    /**
     * Saves the current settings to a properties file.
     * 
     * @return true if save was successful, false otherwise
     */
    public boolean saveSettings() {
        Properties properties = new Properties();
        properties.setProperty("syntaxHighlighting", String.valueOf(syntaxHighlighting));
        properties.setProperty("autoSave", String.valueOf(autoSave));
        properties.setProperty("indentSize", String.valueOf(indentSize));
        properties.setProperty("fontName", fontName);
        properties.setProperty("fontSize", String.valueOf(fontSize));
        properties.setProperty("theme", theme);
        properties.setProperty("wordWrap", String.valueOf(wordWrap));
        
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Editor Settings");
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Loads settings from a properties file.
     * 
     * @return true if load was successful, false otherwise
     */
    public boolean loadSettings() {
        Properties properties = new Properties();
        
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            
            this.syntaxHighlighting = Boolean.parseBoolean(properties.getProperty("syntaxHighlighting", "true"));
            this.autoSave = Boolean.parseBoolean(properties.getProperty("autoSave", "false"));
            this.indentSize = Integer.parseInt(properties.getProperty("indentSize", "4"));
            this.fontName = properties.getProperty("fontName", DEFAULT_FONT);
            this.fontSize = Integer.parseInt(properties.getProperty("fontSize", String.valueOf(DEFAULT_FONT_SIZE)));
            this.theme = properties.getProperty("theme", DEFAULT_THEME);
            this.wordWrap = Boolean.parseBoolean(properties.getProperty("wordWrap", "true"));
            
            return true;
        } catch (IOException e) {
            // Use default settings if file not found
            return false;
        } catch (NumberFormatException e) {
            // Use default settings if values are invalid
            return false;
        } catch (Exception e) {
            // Handle any other exceptions (like malformed properties files)
            resetToDefaults();
            return false;
        }
    }
    
    /**
     * Resets settings to defaults.
     */
    public void resetToDefaults() {
        this.syntaxHighlighting = true;
        this.autoSave = false;
        this.indentSize = 4;
        this.fontName = DEFAULT_FONT;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.theme = DEFAULT_THEME;
        this.wordWrap = true;
    }
    
    public boolean isSyntaxHighlighting() {
        return syntaxHighlighting;
    }
    
    public void setSyntaxHighlighting(boolean syntaxHighlighting) {
        this.syntaxHighlighting = syntaxHighlighting;
    }
    
    public boolean isAutoSave() {
        return autoSave;
    }
    
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }
    
    public int getIndentSize() {
        return indentSize;
    }
    
    public void setIndentSize(int indentSize) {
        if (indentSize >= 0) {
            this.indentSize = indentSize;
        }
    }
    
    public String getFontName() {
        return fontName;
    }
    
    public void setFontName(String fontName) {
        if (fontName != null && !fontName.isEmpty()) {
            this.fontName = fontName;
        }
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontSize(int fontSize) {
        if (fontSize > 0) {
            this.fontSize = fontSize;
        }
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        if (theme != null && !theme.isEmpty()) {
            this.theme = theme;
        }
    }
    
    public boolean isWordWrap() {
        return wordWrap;
    }
    
    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }
} 