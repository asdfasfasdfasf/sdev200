import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Main filename");
            System.exit(1);
        }
        
        String filename = args[0];
        File file = new File(filename);
        
        if (file.exists()) {
            System.out.println("The number of keywords in " + filename + 
                " is " + countKeywords(file));
        }
        else {
            System.out.println("File " + filename + " does not exist");
        }
    }
    
    public static int countKeywords(File file) throws Exception {
        // Array of all Java keywords + true, false and null
        String[] keywordString = {"abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum",
            "extends", "for", "final", "finally", "float", "goto",
            "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile",
            "while", "true", "false", "null"};
        
        Set<String> keywordSet = 
            new HashSet<>(Arrays.asList(keywordString));
        int count = 0;
        
        try (Scanner input = new Scanner(file)) {
            CodeParser parser = new CodeParser(keywordSet);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                count += parser.parseLine(line);
            }
        }
        
        return count;
    }
    
    private static class CodeParser {
        private Set<String> keywordSet;
        private boolean inBlockComment = false;
        private boolean inString = false;
        private boolean inChar = false;
        
        public CodeParser(Set<String> keywordSet) {
            this.keywordSet = keywordSet;
        }
        
        public int parseLine(String line) {
            int count = 0;
            int i = 0;
            
            while (i < line.length()) {
                if (isStartOfSingleLineComment(line, i)) {
                    break;
                }
                
                if (isStartOfBlockComment(line, i)) {
                    inBlockComment = true;
                    i += 2;
                    continue;
                }
                
                if (isEndOfBlockComment(line, i)) {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
                
                if (inBlockComment) {
                    i++;
                    continue;
                }
                
                if (isStringDelimiter(line, i)) {
                    i++;
                    continue;
                }
                
                if (isCharDelimiter(line, i)) {
                    i++;
                    continue;
                }
                
                if (inString || inChar) {
                    i++;
                    continue;
                }
                
                if (isJavaIdentifierStart(line, i)) {
                    int start = i;
                    while (i < line.length() && Character.isJavaIdentifierPart(line.charAt(i))) {
                        i++;
                    }
                    
                    String word = line.substring(start, i);
                    if (keywordSet.contains(word)) {
                        count++;
                    }
                    
                    continue;
                }
                
                i++;
            }
            
            return count;
        }
        
        private boolean isStartOfSingleLineComment(String line, int index) {
            return !inBlockComment && !inString && !inChar && 
                index < line.length() - 1 && 
                line.charAt(index) == '/' && line.charAt(index + 1) == '/';
        }
        
        private boolean isStartOfBlockComment(String line, int index) {
            return !inString && !inChar && 
                index < line.length() - 1 && 
                line.charAt(index) == '/' && line.charAt(index + 1) == '*';
        }
        
        private boolean isEndOfBlockComment(String line, int index) {
            return inBlockComment && index < line.length() - 1 && 
                line.charAt(index) == '*' && line.charAt(index + 1) == '/';
        }
        
        private boolean isStringDelimiter(String line, int index) {
            if (!inBlockComment && !inChar && line.charAt(index) == '"') {
                if (!isEscaped(line, index)) {
                    inString = !inString;
                }
                return true;
            }
            return false;
        }
        
        private boolean isCharDelimiter(String line, int index) {
            if (!inBlockComment && !inString && line.charAt(index) == '\'') {
                if (!isEscaped(line, index)) {
                    inChar = !inChar;
                }
                return true;
            }
            return false;
        }
        
        private boolean isEscaped(String line, int index) {
            int backslashCount = 0;
            int j = index - 1;
            while (j >= 0 && line.charAt(j) == '\\') {
                backslashCount++;
                j--;
            }
            return (backslashCount % 2 == 1);
        }
        
        private boolean isJavaIdentifierStart(String line, int index) {
            return Character.isJavaIdentifierStart(line.charAt(index));
        }
    }
}
