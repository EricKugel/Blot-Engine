package blot.engine.input.blotLibrary;

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

    public void translate(float dx, float dy, float originX, float originY) {
        this.setX(this.x + dx - originX);
        this.setY(this.y + dy - originY);
    }

    public void rotate(double angle, float originX, float originY) {
        angle = angle / 180 * Math.PI;

        float hereX = this.x - originX;
        float hereY = this.y - originY;

        this.setX((float) (hereX * Math.cos(angle) - hereY * Math.sin(angle) + originX));
        this.setY((float) (hereY * Math.cos(angle) + hereX * Math.sin(angle) + originY));
    }

    public void scale(float factorX, float factorY, float originX, float originY) {
        setX((this.x - originX) * factorX + originX);
        setY((this.y - originY) * factorY + y);
    }
}
