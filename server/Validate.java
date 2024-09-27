public class Validate {
    public static boolean check(float x, float y, float r) {
        return square(x, y, r) || circle(x, y, r) || triangle(x, y, r);
    }
    private static boolean circle(float x, float y, float r) {
        return (x >= 0 && y >= 0 && x <= r/2 && y <= r/2 && (Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(r, 2) < 0));
    }
    private static boolean square(float x, float y, float r) {
        return  (x >= 0 && y <= 0 && x <= r && y >= -r);
    }
    private static boolean triangle(float x, float y, float r) {
        return (x <= 0 && y <= 0 && x >= -r && y >= (r / 2) && Math.abs(x) + Math.abs(y) - Math.abs(r) < 0);
    }
}
