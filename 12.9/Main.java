class BinaryFormatException extends Exception {
    public BinaryFormatException(){
        super("The number is not binary");
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(bin2Dec("0000"));
            System.out.println(bin2Dec("1234"));
        }
        catch (BinaryFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static int bin2Dec(String binaryString) throws BinaryFormatException {
        int decimal = 0;
        
        for (int i = 0; i < binaryString.length(); i++) {
            char ch = binaryString.charAt(i);
            if (ch != '0' && ch != '1') {
                throw new BinaryFormatException();
            }
            decimal = decimal * 2 + (ch - '0');
        }
        
        return decimal;
    }
}