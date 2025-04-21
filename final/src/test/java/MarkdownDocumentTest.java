import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MarkdownDocument class functionality.
 */
public class MarkdownDocumentTest {
    
    private MarkdownDocument document;
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        document = new MarkdownDocument("Test Markdown");
    }
    
    @Test
    void testNewMarkdownDocument() {
        assertEquals("Test Markdown", document.getTitle());
        assertEquals("", document.getContent());
        assertTrue(document.isShowPreview());
    }
    
    @Test
    void testValidateMarkdown() {
        // Empty document should not be valid
        assertFalse(document.validate());
        
        // Document with no headers should not be valid
        document.updateContent("This is just some text without headers");
        assertFalse(document.validate());
        
        // Document with proper header should be valid
        document.updateContent("# This is a header\nWith some content");
        assertTrue(document.validate());
        
        // Test different header levels
        document.updateContent("## Level 2 header");
        assertTrue(document.validate());
        
        document.updateContent("###### Level 6 header");
        assertTrue(document.validate());
    }

    @Test
    void testIsMarkdownFile() {
        // No file path set
        assertFalse(document.isMarkdownFile());
        
        // Set a markdown file path
        try {
            File mdFile = tempDir.resolve("test.md").toFile();
            Files.writeString(mdFile.toPath(), "# Test");
            document = new MarkdownDocument(mdFile);
            assertTrue(document.isMarkdownFile());
            
            // Set a non-markdown file path
            File txtFile = tempDir.resolve("test.txt").toFile();
            Files.writeString(txtFile.toPath(), "# Test");
            document = new MarkdownDocument(txtFile);
            assertFalse(document.isMarkdownFile());
            
            // Test with .markdown extension
            File markdownFile = tempDir.resolve("test.markdown").toFile();
            Files.writeString(markdownFile.toPath(), "# Test");
            document = new MarkdownDocument(markdownFile);
            assertTrue(document.isMarkdownFile());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    void testShowPreview() {
        assertTrue(document.isShowPreview()); // Default should be true
        
        document.setShowPreview(false);
        assertFalse(document.isShowPreview());
        
        document.setShowPreview(true);
        assertTrue(document.isShowPreview());
    }
    
    @Test
    void testSaveAndOpenMarkdownDocument() throws IOException {
        // Create a test markdown file
        File mdFile = tempDir.resolve("test_document.md").toFile();
        String content = "# Test Header\nThis is markdown content";
        document.updateContent(content);
        document.saveAs(mdFile.getAbsolutePath());
        
        // Verify file was created and saved
        assertTrue(mdFile.exists());
        assertEquals(content, Files.readString(mdFile.toPath()));
        
        // Test opening the markdown file
        MarkdownDocument newDocument = new MarkdownDocument("New Markdown");
        newDocument.open(mdFile.getAbsolutePath());
        
        assertEquals(content, newDocument.getContent());
        assertEquals("test_document.md", newDocument.getTitle());
        assertTrue(newDocument.isMarkdownFile());
    }
} 