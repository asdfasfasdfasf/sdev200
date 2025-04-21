# Project Overview
CodeMark will be a JavaFX application which serves as a lightweight IDE supporting markdown,
similar to Notepad++. The purpose of this app is to provide developers and students with a
lightweight environment for basic code editing. This will support syntax highlighting and other
things to make the experience easier and more enjoyable.
# User Interface
The application will look like a typical code editor, people will interact with it using the main
editor area. There will be a toolbar at the top that lets people manage options like syntax
highlighting, auto save, and more.
# Classes
The app will have 3 classes.
## Document
This is the parent abstract class. This will be the foundation for all documents in the app. It will
contain common fields like filePath, content, lastModified, lastSaved, isModified, title, and
common definitions like save(), open(), and validate().
## MarkdownDocument
This is the child class that will inherit the Document. It will contain properties specific to
markdown (.md & other) documents, like showPreview (bool), which will indicate whether to
show a split screen preview of how the markdown code actually renders.
## EditorSettings
This class will contain settings for the code editor like syntaxHighlighting (bool), autoSave
(bool), indentSize (int). This will be accessed by the view to understand which settings to apply.
The app will highlight invalid code in red, possibly even displaying the error message in a
terminal area.
