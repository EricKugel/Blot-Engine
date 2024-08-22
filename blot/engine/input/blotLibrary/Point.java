package blot.engine.input.blotLibrary;

/**
 * Basic class for storing blot coords. Includes point transformation functions as well.
 */
public class Point {
    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
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
    public void translate(float dx, float dy, float originX, float originY) {
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
    public void rotate(double angle, float originX, float originY) {
        angle = angle / 180 * Math.PI;

        float hereX = this.x - originX;
        float hereY = this.y - originY;

        this.setX((float) (hereX * Math.cos(angle) - hereY * Math.sin(angle) + originX));
        this.setY((float) (hereY * Math.cos(angle) + hereX * Math.sin(angle) + originY));
    }

    /**
     * see https://github.com/hackclub/blot/blob/main/src/drawingToolkit/affineTransformations.js
     * 
     * @param factorX how much to scale x by.
     * @param factorY how much to scale y by.
     * @param originX the x coordinate of the transformation's origin.
     * @param originY the y coordinate of the transformation's origin.
     */
    public void scale(float factorX, float factorY, float originX, float originY) {
        setX((this.x - originX) * factorX + originX);
        setY((this.y - originY) * factorY + y);
    }
}
