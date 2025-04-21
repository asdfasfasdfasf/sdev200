import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    
    private TextArea editor;
    private WebView previewArea;
    private MarkdownDocument currentDocument;
    private EditorSettings editorSettings;
    private Label statusBar;
    private BorderPane root;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize editor settings
        editorSettings = new EditorSettings();
        editorSettings.loadSettings();
        
        // Create a new empty document
        currentDocument = new MarkdownDocument("Untitled");
        
        // Create the main layout
        root = new BorderPane();
        
        // Create the menu bar
        MenuBar menuBar = createMenuBar(primaryStage);
        root.setTop(menuBar);
        
        // Create the editor area
        editor = createEditor();
        
        // Create the preview area
        previewArea = new WebView();
        previewArea.getEngine().loadContent("<html><body><h1>Preview</h1><p>Content will appear here.</p></body></html>");
        
        // Create a split pane for editor and preview
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(editor, previewArea);
        splitPane.setDividerPositions(0.5);
        root.setCenter(splitPane);
        
        // Create a status bar
        statusBar = new Label("Ready");
        HBox statusBarContainer = new HBox(statusBar);
        statusBarContainer.setPadding(new Insets(5));
        root.setBottom(statusBarContainer);
        
        // Create toolbar
        ToolBar toolBar = createToolbar();
        VBox topContainer = new VBox(menuBar, toolBar);
        root.setTop(topContainer);
        
        // Create and show the scene
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("CodeMark Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Set up event handlers
        setupEventHandlers();
    }
    
    /**
     * Creates the editor component with the appropriate settings.
     * 
     * @return The configured text editor
     */
    private TextArea createEditor() {
        TextArea textArea = new TextArea();
        textArea.setFont(Font.font(editorSettings.getFontName(), editorSettings.getFontSize()));
        textArea.setWrapText(editorSettings.isWordWrap());
        return textArea;
    }
    
    /**
     * Creates the menu bar with all necessary menus.
     * 
     * @param stage The main application stage
     * @return The configured menu bar
     */
    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(e -> newDocument());
        
        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> openDocument(stage));
        
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> saveDocument(stage));
        
        MenuItem saveAsItem = new MenuItem("Save As");
        saveAsItem.setOnAction(e -> saveDocumentAs(stage));
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> stage.close());
        
        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);
        
        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setOnAction(e -> editor.undo());
        
        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setOnAction(e -> editor.redo());
        
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setOnAction(e -> editor.cut());
        
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(e -> editor.copy());
        
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(e -> editor.paste());
        
        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), cutItem, copyItem, pasteItem);
        
        // Settings menu
        Menu settingsMenu = new Menu("Settings");
        CheckMenuItem syntaxItem = new CheckMenuItem("Syntax Highlighting");
        syntaxItem.setSelected(editorSettings.isSyntaxHighlighting());
        syntaxItem.setOnAction(e -> {
            editorSettings.setSyntaxHighlighting(syntaxItem.isSelected());
            editorSettings.saveSettings();
            updateEditor();
        });
        
        CheckMenuItem autoSaveItem = new CheckMenuItem("Auto Save");
        autoSaveItem.setSelected(editorSettings.isAutoSave());
        autoSaveItem.setOnAction(e -> {
            editorSettings.setAutoSave(autoSaveItem.isSelected());
            editorSettings.saveSettings();
        });
        
        CheckMenuItem wordWrapItem = new CheckMenuItem("Word Wrap");
        wordWrapItem.setSelected(editorSettings.isWordWrap());
        wordWrapItem.setOnAction(e -> {
            editorSettings.setWordWrap(wordWrapItem.isSelected());
            editor.setWrapText(editorSettings.isWordWrap());
            editorSettings.saveSettings();
        });
        
        MenuItem resetItem = new MenuItem("Reset to Defaults");
        resetItem.setOnAction(e -> {
            editorSettings.resetToDefaults();
            editorSettings.saveSettings();
            updateEditor();
            
            // Update menu items
            syntaxItem.setSelected(editorSettings.isSyntaxHighlighting());
            autoSaveItem.setSelected(editorSettings.isAutoSave());
            wordWrapItem.setSelected(editorSettings.isWordWrap());
        });
        
        settingsMenu.getItems().addAll(syntaxItem, autoSaveItem, wordWrapItem, new SeparatorMenuItem(), resetItem);
        
        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog(stage));
        
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, editMenu, settingsMenu, helpMenu);
        return menuBar;
    }
    
    /**
     * Creates the toolbar with common actions.
     * 
     * @return The configured toolbar
     */
    private ToolBar createToolbar() {
        ToolBar toolBar = new ToolBar();
        
        Button newButton = new Button("New");
        newButton.setOnAction(e -> newDocument());
        
        Button openButton = new Button("Open");
        openButton.setOnAction(e -> openDocument((Stage) root.getScene().getWindow()));
        
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveDocument((Stage) root.getScene().getWindow()));
        
        CheckBox previewCheckBox = new CheckBox("Show Preview");
        previewCheckBox.setSelected(currentDocument.isShowPreview());
        previewCheckBox.setOnAction(e -> {
            currentDocument.setShowPreview(previewCheckBox.isSelected());
            updatePreview();
        });
        
        // Indent size slider
        Label indentLabel = new Label("Indent Size:");
        Slider indentSlider = new Slider(2, 8, editorSettings.getIndentSize());
        indentSlider.setShowTickMarks(true);
        indentSlider.setShowTickLabels(true);
        indentSlider.setMajorTickUnit(2);
        indentSlider.setMinorTickCount(1);
        indentSlider.setSnapToTicks(true);
        indentSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            editorSettings.setIndentSize(newVal.intValue());
            editorSettings.saveSettings();
        });
        
        toolBar.getItems().addAll(newButton, openButton, saveButton, new Separator(), 
                                 previewCheckBox, new Separator(), indentLabel, indentSlider);
        return toolBar;
    }
    
    /**
     * Sets up event handlers for the editor.
     */
    private void setupEventHandlers() {
        // Update the document when text changes
        editor.textProperty().addListener((obs, oldText, newText) -> {
            if (currentDocument != null) {
                currentDocument.updateContent(newText);
                updatePreview();
                updateTitle();
                
                // Validate the document
                if (!currentDocument.validate()) {
                    statusBar.setText("Warning: Document contains invalid markdown");
                    statusBar.setTextFill(Color.RED);
                } else {
                    statusBar.setText("Ready");
                    statusBar.setTextFill(Color.BLACK);
                }
                
                // Auto-save if enabled
                if (editorSettings.isAutoSave() && currentDocument.getFilePath() != null) {
                    currentDocument.save();
                }
            }
        });
    }
    
    /**
     * Updates the preview pane with current document content.
     */
    private void updatePreview() {
        if (currentDocument.isShowPreview()) {
            previewArea.getEngine().loadContent(currentDocument.generatePreview());
            previewArea.setVisible(true);
        } else {
            previewArea.setVisible(false);
        }
    }
    
    /**
     * Updates the editor settings.
     */
    private void updateEditor() {
        editor.setFont(Font.font(editorSettings.getFontName(), editorSettings.getFontSize()));
        editor.setWrapText(editorSettings.isWordWrap());
    }
    
    /**
     * Updates the window title with the current document title.
     */
    private void updateTitle() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setTitle("CodeMark Editor - " + currentDocument.getTitle() + 
                      (currentDocument.isModified() ? " *" : ""));
    }
    
    /**
     * Creates a new empty document.
     */
    private void newDocument() {
        // Check if current document has unsaved changes
        if (currentDocument != null && currentDocument.isModified()) {
            boolean save = showUnsavedChangesDialog();
            if (save) {
                saveDocument((Stage) root.getScene().getWindow());
            }
        }
        
        // Create a new document
        currentDocument = new MarkdownDocument("Untitled");
        editor.setText("");
        updateTitle();
        updatePreview();
    }
    
    /**
     * Opens an existing document.
     * 
     * @param stage The current stage
     */
    private void openDocument(Stage stage) {
        // Check if current document has unsaved changes
        if (currentDocument != null && currentDocument.isModified()) {
            boolean save = showUnsavedChangesDialog();
            if (save) {
                saveDocument(stage);
            }
        }
        
        // Show file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Document");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Markdown Files", "*.md", "*.markdown"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                // Open the document using File constructor
                currentDocument = new MarkdownDocument(file);
                editor.setText(currentDocument.getContent());
                updateTitle();
                updatePreview();
                statusBar.setText("Opened: " + file.getName());
                statusBar.setTextFill(Color.BLACK);
            } catch (IOException e) {
                showErrorDialog("Error opening file", e.getMessage());
            }
        }
    }
    
    /**
     * Saves the current document.
     * 
     * @param stage The current stage
     */
    private void saveDocument(Stage stage) {
        if (currentDocument == null) {
            return;
        }
        
        // If the document hasn't been saved before, use Save As
        if (currentDocument.getFilePath() == null || currentDocument.getFilePath().isEmpty()) {
            saveDocumentAs(stage);
        } else {
            // Save the document
            if (currentDocument.save()) {
                updateTitle();
                statusBar.setText("Saved: " + currentDocument.getTitle());
                statusBar.setTextFill(Color.BLACK);
            } else {
                showErrorDialog("Error Saving", "Could not save the document.");
            }
        }
    }
    
    /**
     * Saves the current document to a new location.
     * 
     * @param stage The current stage
     */
    private void saveDocumentAs(Stage stage) {
        if (currentDocument == null) {
            return;
        }
        
        // Show file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Markdown Files", "*.md"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        // Set initial filename
        if (currentDocument.getTitle() != null && !currentDocument.getTitle().equals("Untitled")) {
            fileChooser.setInitialFileName(currentDocument.getTitle());
        } else {
            fileChooser.setInitialFileName("document.md");
        }
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            // Save the document
            if (currentDocument.saveAs(file.getAbsolutePath())) {
                updateTitle();
                statusBar.setText("Saved: " + currentDocument.getTitle());
                statusBar.setTextFill(Color.BLACK);
            } else {
                showErrorDialog("Error Saving", "Could not save the document.");
            }
        }
    }
    
    /**
     * Shows dialog when there are unsaved changes.
     * 
     * @return true if the user wants to save, false otherwise
     */
    private boolean showUnsavedChangesDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("There are unsaved changes");
        alert.setContentText("Do you want to save the changes?");
        
        ButtonType buttonTypeSave = new ButtonType("Save");
        ButtonType buttonTypeDontSave = new ButtonType("Don't Save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeDontSave, buttonTypeCancel);
        
        alert.showAndWait();
        ButtonType result = alert.getResult();
        
        return result == buttonTypeSave;
    }
    
    /**
     * Shows an error dialog.
     * 
     * @param title The dialog title
     * @param message The error message
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows the about dialog.
     * 
     * @param stage The current stage
     */
    private void showAboutDialog(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About CodeMark");
        alert.setHeaderText("CodeMark Editor");
        alert.setContentText("A lightweight IDE for editing markdown files.\n\n" +
                            "Version: 1.0\n" +
                            "© 2025 CodeMark");
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
