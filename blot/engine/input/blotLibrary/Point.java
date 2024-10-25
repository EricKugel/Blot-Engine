package blot.engine.input.blotLibrary;

/**
 * Basic class for storing blot coords. Includes point transformation functions as well.
 */
public class Point {
    public static final Point ZERO = new Point(0, 0);
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        Point otherPoint = (Point) other;
        return x == otherPoint.getX() && y == otherPoint.getY();
    }

    public Point clone() {
        return new Point(x, y);
    }

    // The following up to and including dot are vector operations
    public static Point sub(Point point1, Point point2) {
        return new Point(point1.getX() - point2.getX(), point1.getY() - point2.getY());
    }

    public static Point add(Point point1, Point point2) {
        return new Point(point1.getX() + point2.getX(), point1.getY() + point2.getY());
    }

    public static Point mult(Point point, double scalar) {
        return new Point(point.getX() * scalar, point.getY() * scalar);
    }

    public static double magnitude(Point point) {
        return Math.hypot(point.getX(), point.getY());
    }

    public static Point direction(Point point) {
        return mult(point, 1 / magnitude(point));
    }

    public static double dot(Point point1, Point point2) {
        return point1.getX() * point2.getX() + point1.getY() * point2.getY();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * see https://github.com/hackclub/blot/blob/main/src/drawingToolkit/affineTransformations.js
     * 
     * Honestly Leo McElroy is such a chad for these next three methods, it's shocking how many times I've
     * ended up using them.
     * 
     * Bless up Leo 🙏🙏🙏🙏 I see you
     * 
     * @param dx how much to shift x by.
     * @param dy how much to shift y by.
     * @param originX the x coordinate of the transformation's origin.
     * @param originY the y coordinate of the transformation's origin.
     */
    public void translate(double dx, double dy, double originX, double originY) {
        x += dx - originX;
        y += dy - originY;
    }

    /**
     * see https://github.com/hackclub/blot/blob/main/src/drawingToolkit/affineTransformations.js
     * 
     * @param angle how much to rotate by, in degrees.
     * @param originX the x coordinate of the transformation's origin.
     * @param originY the y coordinate of the transformation's origin.
     */
    public void rotate(double angle, double originX, double originY) {
        angle = Math.toRadians(angle);

        double hereX = x - originX;
        double hereY = y - originY;

        x = (double) (hereX * Math.cos(angle) - hereY * Math.sin(angle) + originX);
        y = (double) (hereY * Math.cos(angle) + hereX * Math.sin(angle) + originY);
    }

    /**
     * see https://github.com/hackclub/blot/blob/main/src/drawingToolkit/affineTransformations.js
     * 
     * @param factorX how much to scale x by.
     * @param factorY how much to scale y by.
     * @param originX the x coordinate of the transformation's origin.
     * @param originY the y coordinate of the transformation's origin.
     */
    public void scale(double factorX, double factorY, double originX, double originY) {
        x = (x - originX) * factorX + originX;
        y = (y - originY) * factorY + originY;
    }
}
