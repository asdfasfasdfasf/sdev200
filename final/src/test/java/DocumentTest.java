import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Document abstract class functionality.
 */
public class DocumentTest {
    
    private TestDocument document;
    @TempDir
    Path tempDir;
    
    /**
     * Document impl for testing purposes.
     */
    private static class TestDocument extends Document {
        public TestDocument(String title) {
            super(title);
        }
        
        public TestDocument(File file) throws IOException {
            super(file);
        }
        
        @Override
        public boolean validate() {
            // Simple validation for testing
            return content != null && !content.isEmpty();
        }
    }
    
    @BeforeEach
    void setUp() {
        document = new TestDocument("Test Document");
    }
    
    @Test
    void testNewDocument() {
        assertEquals("Test Document", document.getTitle());
        assertEquals("", document.getContent());
        assertFalse(document.isModified());
        assertNotNull(document.getLastModified());
    }
    
    @Test
    void testUpdateContent() {
        String newContent = "This is test content";
        document.updateContent(newContent);
        
        assertEquals(newContent, document.getContent());
        assertTrue(document.isModified());
        assertNotNull(document.getLastModified());
    }
    
    @Test
    void testSaveAndOpen() throws IOException {
        // Create a test file
        File testFile = tempDir.resolve("test_document.txt").toFile();
        String content = "Test content for saving and opening";
        document.updateContent(content);
        document.saveAs(testFile.getAbsolutePath());
        
        // Verify file was created and saved
        assertTrue(testFile.exists());
        assertEquals(content, Files.readString(testFile.toPath()));
        
        // Test opening the file
        TestDocument newDocument = new TestDocument("New Document");
        newDocument.open(testFile.getAbsolutePath());
        
        assertEquals(content, newDocument.getContent());
        assertEquals("test_document.txt", newDocument.getTitle());
        assertFalse(newDocument.isModified());
    }
    
    @Test
    void testValidate() {
        assertFalse(document.validate()); // Empty content
        
        document.updateContent("Some content");
        assertTrue(document.validate()); // Non-empty content
    }
    
    @Test
    void testSetTitle() {
        String newTitle = "New Title";
        document.setTitle(newTitle);
        
        assertEquals(newTitle, document.getTitle());
        assertTrue(document.isModified());
    }
    
    @Test
    void testSaveWithNoPath() {
        // Test saving without a path
        assertFalse(document.save());
    }
    
    @Test
    void testFileOperationExceptions() {
        // Test with invalid file path
        File invalidFile = new File("/invalid/path/file.txt");
        assertThrows(IOException.class, () -> new TestDocument(invalidFile));
        
        // Test opening non-existent file
        TestDocument doc = new TestDocument("Test");
        assertFalse(doc.open("/invalid/path/nonexistent.txt"));
    }
} 