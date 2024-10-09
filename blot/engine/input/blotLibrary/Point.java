package blot.engine.input.blotLibrary;

/**
 * Basic class for storing blot coords. Includes point transformation functions as well.
 */
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        Point otherPoint = (Point) other;
        return this.x == otherPoint.getX() && this.y == otherPoint.getY();
    }

    public Point clone() {
        return new Point(this.x, this.y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    /**
     * see https://github.com/hackclub/blot/blob/main/src/drawingToolkit/affineTransformations.js
     * 
     * @param dx how much to shift x by.
     * @param dy how much to shift y by.
     * @param originX the x coordinate of the transformation's origin.
     * @param originY the y coordinate of the transformation's origin.
     */
    public void translate(double dx, double dy, double originX, double originY) {
        this.setX(this.x + dx - originX);
        this.setY(this.y + dy - originY);
    }

    /**
     * see https://github.com/hackclub/blot/blob/main/src/drawingToolkit/affineTransformations.js
     * 
     * @param angle how much to rotate by, in degrees.
     * @param originX the x coordinate of the transformation's origin.
     * @param originY the y coordinate of the transformation's origin.
     */
    public void rotate(double angle, double originX, double originY) {
        angle = angle / 180 * Math.PI;

        double hereX = this.x - originX;
        double hereY = this.y - originY;

        this.setX((double) (hereX * Math.cos(angle) - hereY * Math.sin(angle) + originX));
        this.setY((double) (hereY * Math.cos(angle) + hereX * Math.sin(angle) + originY));
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
        setX((this.x - originX) * factorX + originX);
        setY((this.y - originY) * factorY + originY);
    }
}
