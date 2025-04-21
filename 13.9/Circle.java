public class Circle extends GeometricObject implements Comparable<Circle> {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getDiameter() {
        return 2 * radius;
    }

    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public void printCircle() {
        System.out.println("The circle is created " + getRadius() + " cm radius and the color is " + getColor());
    }

    @Override
    public int compareTo(Circle other) {
        if (this.radius > other.radius) return 1; // This is larger than other
        else if (this.radius < other.radius) return -1; // This is smaller than other
        return 0; // Both are equal
    }
    
}
