import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <filename>");
            System.exit(1);
        }
        
        try {
            boolean isBalanced = checkGroupingPairs(args[0]);
            if (isBalanced) {
                System.out.println("Correct grouping pairs");
            } else {
                System.out.println("Incorrect grouping pairs");
            }
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
    }
    
    public static boolean checkGroupingPairs(String filename) throws IOException {
        Stack<Character> stack = new Stack<>();    
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int c;
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                if (ch == '(' || ch == '{' || ch == '[') {
                    stack.push(ch);
                } else if (ch == ')' || ch == '}' || ch == ']') {
                    if (stack.isEmpty()) return false;
                    
                    char top = stack.pop();
                    if ((ch == ')' && top != '(') ||
                        (ch == '}' && top != '{') ||
                        (ch == ']' && top != '[')) {
                        return false;
                    }
                }
            }
        }
        return stack.isEmpty();
    }
}
