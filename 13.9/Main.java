public class Main {
    public static void compareCircles(double size1, double size2) {
        Circle circle1 = new Circle(size1);
        Circle circle2 = new Circle(size2);

        circle1.setColor("red");
        circle2.setColor("blue");

        int comparison = circle1.compareTo(circle2);

        if (comparison == 1) {
            System.out.println("Circle 1 is larger than Circle 2");
        } else if (comparison == -1) {
            System.out.println("Circle 1 is smaller than Circle 2");
        } else {
            System.out.println("Circle 1 is equal to Circle 2");
        }
    }

    public static void main(String[] args) {
        // Test with 3 different combinations
        compareCircles(5, 10);  // First circle smaller
        compareCircles(10, 5);  // First circle larger 
        compareCircles(7, 7);   // Equal circles
    }
}