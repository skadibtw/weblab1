public class Validate {
    public static boolean check(double x, double y, double r) {
        return square(x, y, r) || circle(x, y, r) || triangle(x, y, r);
    }
    private static boolean circle(double x, double y, double r) {
        return (x >= 0 && y >= 0 && x <= r/2 && y <= r/2 && (Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(r/2, 2) <= 0));
    }
    private static boolean square(double x, double y, double r) {
        return  (x >= 0 && y <= 0 && x <= r && y >= -r);
    }

    private static boolean triangle(double x, double y, double r) {
        return (x <= 0 && y <= 0 && x >= -r && y >= (-r / 2) && y >= (-1/2d * x) - (r / 2));
    }
    // Validation methods
    public static boolean validateX(double x) {
        return x >= -3 && x <= 5;
    }

    public static boolean validateY(double y) {
        return y >= -3 && y <= 3;
    }

    public static boolean validateR(double r) {
        return r >= 1 && r <= 5;
    }
}
