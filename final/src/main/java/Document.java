import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Abstract Document class that serves as a foundation for all document types in the application.
 * Contains common fields and methods for document handling.
 */
public abstract class Document {
    protected String filePath;
    protected String content;
    protected LocalDateTime lastModified;
    protected LocalDateTime lastSaved;
    protected boolean isModified;
    protected String title;
    
    /**
     * Constructor for a new document.
     * 
     * @param title The title of the document
     */
    public Document(String title) {
        this.title = title;
        this.content = "";
        this.isModified = false;
        this.lastModified = LocalDateTime.now();
    }
    
    /**
     * Constructor for an existing document.
     * 
     * @param file The file to load the document from
     * @throws IOException If the file cannot be read
     */
    public Document(File file) throws IOException {
        this.filePath = file.getAbsolutePath();
        Path path = file.toPath();
        this.content = Files.readString(path);
        this.title = file.getName();
        this.isModified = false;
        this.lastModified = LocalDateTime.now();
        this.lastSaved = LocalDateTime.now();
    }
    
    /**
     * Saves the document to the specified path.
     * 
     * @return true if save was successful, false otherwise
     */
    public boolean save() {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            this.lastSaved = LocalDateTime.now();
            this.isModified = false;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Saves the document to a new path.
     * 
     * @param path The new path to save the document to
     * @return true if save was successful, false otherwise
     */
    public boolean saveAs(String path) {
        this.filePath = path;
        return save();
    }
    
    /**
     * Opens a document from the file system.
     * 
     * @param path The path to the document
     * @return true if open was successful, false otherwise
     */
    public boolean open(String path) {
        try {
            Path filePath = Paths.get(path);
            this.content = Files.readString(filePath);
            this.filePath = path;
            this.title = new File(path).getName();
            this.isModified = false;
            this.lastModified = LocalDateTime.now();
            this.lastSaved = LocalDateTime.now();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Validates the document content.
     * Implementation depends on document type.
     * 
     * @return true if document is valid, false otherwise
     */
    public abstract boolean validate();
    
    /**
     * Updates the document content and marks it as modified.
     * 
     * @param newContent The new content of the document
     */
    public void updateContent(String newContent) {
        this.content = newContent;
        this.isModified = true;
        this.lastModified = LocalDateTime.now();
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    
    public LocalDateTime getLastSaved() {
        return lastSaved;
    }
    
    public boolean isModified() {
        return isModified;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.isModified = true;
    }
} 