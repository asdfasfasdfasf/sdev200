import java.io.IOException;
import java.util.regex.Pattern;
import java.io.File;

/**
 * MarkdownDocument class represents a markdown document in the application.
 * Extends the abstract Document class with markdown-specific functionality.
 */
public class MarkdownDocument extends Document {
    private boolean showPreview;
    
    // Regex pattern to validate markdown headers
    private static final Pattern HEADER_PATTERN = Pattern.compile("^#{1,6}\\s.*$", Pattern.MULTILINE);
    
    /**
     * Constructor for a new markdown document.
     * 
     * @param title The title of the document
     */
    public MarkdownDocument(String title) {
        super(title);
        this.showPreview = true;
    }
    
    /**
     * Constructor for an existing markdown document.
     * 
     * @param file The file to load the document from
     * @throws IOException If the file cannot be read
     */
    public MarkdownDocument(File file) throws IOException {
        super(file);
        this.showPreview = true;
    }
    
    /**
     * Validates the markdown document structure.
     * Checks for proper markdown syntax elements.
     * 
     * @return true if the document contains valid markdown, false otherwise
     */
    @Override
    public boolean validate() {
        // Simple validation: check if it has at least one markdown header
        if (content == null || content.isEmpty()) {
            return false;
        }
        
        return HEADER_PATTERN.matcher(content).find();
    }
    
    /**
     * Converts markdown content to HTML for preview.
     * 
     * @return HTML representation of the markdown content
     */
    public String generatePreview() {
        if (content == null || content.isEmpty()) {
            return "<html><body></body></html>";
        }
        
        // Basic markdown to HTML conversion for headers, bold, and italic
        String html = content;
        
        // Convert code blocks (```code```) - do this first to preserve newlines in code blocks
        html = html.replaceAll("(?s)```(.*?)```", "<pre><code>$1</code></pre>");
        
        // Convert headers (# Header)
        html = html.replaceAll("(?m)^# (.*)$", "<h1>$1</h1>");
        html = html.replaceAll("(?m)^## (.*)$", "<h2>$1</h2>");
        html = html.replaceAll("(?m)^### (.*)$", "<h3>$1</h3>");
        html = html.replaceAll("(?m)^#### (.*)$", "<h4>$1</h4>");
        html = html.replaceAll("(?m)^##### (.*)$", "<h5>$1</h5>");
        html = html.replaceAll("(?m)^###### (.*)$", "<h6>$1</h6>");
        
        // Convert bold (**text**)
        html = html.replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>");
        
        // Convert italic (*text*)
        html = html.replaceAll("\\*(.*?)\\*", "<em>$1</em>");
        
        // Convert line breaks
        html = html.replaceAll("\\n", "<br>");
        
        return "<html><body>" + html + "</body></html>";
    }
    
    /**
     * Checks if the document is a valid markdown file based on extension.
     * 
     * @return true if the file has a markdown extension, false otherwise
     */
    public boolean isMarkdownFile() {
        if (filePath == null) {
            return false;
        }
        
        return filePath.toLowerCase().endsWith(".md") || 
               filePath.toLowerCase().endsWith(".markdown");
    }
    
    public boolean isShowPreview() {
        return showPreview;
    }
    
    public void setShowPreview(boolean showPreview) {
        this.showPreview = showPreview;
    }
} 