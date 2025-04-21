public class Main {
    public static void main(String[] args) {
        MyDate date1 = new MyDate();
        System.out.println("Date 1:");
        System.out.println("Year: " + date1.getYear());
        System.out.println("Month: " + date1.getMonth());
        System.out.println("Day: " + date1.getDay());

        MyDate date2 = new MyDate(34355555133101L);
        System.out.println("\nDate 2:");
        System.out.println("Year: " + date2.getYear());
        System.out.println("Month: " + date2.getMonth());
        System.out.println("Day: " + date2.getDay());
    }
}
