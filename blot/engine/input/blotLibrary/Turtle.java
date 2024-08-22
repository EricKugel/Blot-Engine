package blot.engine.input.blotLibrary;

import java.util.ArrayList;

/**
 * Mimicks the blot library's turtle, to ease rewriting.
 */
public class Turtle {
    private boolean isDrawing = true;
    private Point position = new Point(0, 0);
    private double angle = 0;
    private DrawingObject drawingObject = new DrawingObject();

    /**
     * See https://github.com/hackclub/blot/blob/main/src/drawingToolkit/Turtle.js
     */
    public Turtle() {
        drawingObject.newLine();
        drawingObject.addPoint(new Point(0, 0));
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Lifts the pen.
     */
    public void up() {
        if (!this.isDrawing) {
            return;
        }
        this.isDrawing = false;
        this.drawingObject.addPoint(position);
    }

    /**
     * Sets the pen down.
     */
    public void down() {
        this.isDrawing = true;
    }

    /**
     * Goes to the specified coordinate, regardless if the pen is up or down.
     * 
     * @param x the destination's x value.
     * @param y the destination's y value.
     */
    public void goTo(float x, float y) {
        if (this.isDrawing) {
            float lastX = this.position.getX();
            float lastY = this.position.getY();
            double dist = Math.sqrt(Math.pow(x - lastX, 2) + Math.pow(y - lastY, 2));
            if (dist < 0.0001) {
                return;
            } else if (this.drawingObject.getLastPath().size() == 1) {
                this.drawingObject.deletePoint();
                this.drawingObject.addPoint(new Point(x, y));
            }
        }

        this.position = new Point(x, y);
    }

    /**
     * Goes in the x direction by dx, and in the y direction by dy.
     * 
     * @param dx how far to move in the x direction.
     * @param dy how far to move in the y direction.
     */
    public void step(float dx, float dy) {
        float x = this.position.getX();
        float y = this.position.getY();
        this.goTo(x + dx, y + dy);
    }

    /**
     * Goes to the specified coordinate WITHOUT making a line.
     * 
     * @param x the destination's x value.
     * @param y the destination's y value.
     */
    public void jump(float x, float y) {
        if (this.drawingObject.getLastPath().size() == 1) {
            this.drawingObject.deletePoint();
            this.drawingObject.addPoint(new Point(x, y));
            return;
        }

        this.up();
        this.goTo(x, y);
        this.down();
    }

    /**
     * Moves the blot a certain distance in the current direction, regardless if the pen is up or down.
     * 
     * @param distance how far to move.
     */
    public void forward(double distance) {
        float lastX = this.position.getX();
        float lastY = this.position.getY();
        double a = (this.angle / 180) * Math.PI;
        float x = (float) (lastX + distance * Math.cos(a));
        float y = (float) (lastY + distance * Math.sin(a));

        this.goTo(x, y);
    }

    /**
     * Draws an arc.
     * 
     * @param angle how much of an arc in degrees (e.g. 360 = full circle, 180 = semicircle, etc)
     * @param radius the radius of the arc.
     */
    public void arc(double angle, double radius) {
        if (angle * radius == 0) {
            return;
        }

        final int n = 64;
        ArrayList<Point> points = new ArrayList<Point>();
        angle = angle / 180 * Math.PI;
        
        for (int i = 0; i <= n; i++) {
            double theta = angle / n * i;
            points.add(new Point((float) (radius * Math.cos(theta)), (float) (radius * Math.sin(theta))));
        }

        for (Point point : points) {
            point.translate(this.position.getX(), this.position.getY(), points.get(0).getX(), points.get(0).getY());
            point.rotate(this.angle + (angle < 0 ? 90 : -90), points.get(0).getX(), points.get(0).getY());
        }

        for (int i = 1; i <= n; i++) {
            this.goTo(points.get(i).getX(), points.get(i).getY());
        }

        this.setAngle(this.angle + angle);
    }
    /**
     * Turns to the right a certain number of degrees.
     * 
     * @param theta the number of degrees to turn to the right
     */
    public void right(double theta) {
        this.angle -= theta;
    }

    /**
     * Turns to the left a certain number of degrees.
     * 
     * @param theta the number of degrees to turn to the left.
     */
    public void left(double theta) {
        this.angle += theta;
    }

    /**
     * Returns the paths made by the turtle as a DrawingObject.
     * 
     * @return the DrawingObject paths created by the turtle.
     */
    public DrawingObject getDrawingObject() {
        return drawingObject;
    }
}