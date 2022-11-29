package rendezvous;

public class Utilities {

    public static double distanceTwoPoints (double latX, double lonX, double latY, double lonY) {
        //example: System.out.println(Utilities.distanceTwoPoints(40, -179.99, 40, -115));

        final int R = 6371; // Radius of the earth (unit: meter)

        double latDistance = Math.toRadians(latY - latX);
        double lonDistance = Math.toRadians(lonY - lonX);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latX)) * Math.cos(Math.toRadians(latY))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }


    public static void main(String[] args) {
        System.out.println(Utilities.distanceTwoPoints(44.9046320326008, -93.3606396487403, 44.905577100422, -93.359601255829));
        System.out.println(10000000.0 / (5115*5404));
    }
}
