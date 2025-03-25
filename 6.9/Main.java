public class Main {
    /** Convert from feet to meters */
    public static double footToMeter(double foot) {
        return 0.305 * foot;
    }

    /** Convert from meters to feet */
    public static double meterToFoot(double meter) {
        return 3.279 * meter;
    }

    public static void main(String[] args) {
        System.out.println("Feet to Meters | Meters to Feet");
        System.out.println("-------------------------------------");
        
        double feet = 1;
        double meters = 20;
        
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.1f = %.3f | %.1f = %.3f\n", feet, footToMeter(feet), meters, meterToFoot(meters));
            feet += 1;
            meters += 5;
        }
    }
}
