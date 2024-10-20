package blot.engine.input.blotLibrary;

import java.util.ArrayList;

/**
 * Mimicks the blot library's turtle, to ease rewriting engines.
 */
public class Turtle {
    private boolean isDrawing = true;
    private Point position = new Point(0, 0);
    private double angle = 0;
    private DrawingObject drawingObject = new DrawingObject();

    private int resolution = 64;

    /**
     * See https://github.com/hackclub/blot/blob/main/src/drawingToolkit/Turtle.js
     */
    public Turtle() {
        drawingObject.newLine();
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Changes the resolution of arcs and bezier curves.
     * 
     * @param resolution Lines per arc/curve
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
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
        this.drawingObject.newLine();
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
    public void goTo(double x, double y) {
        if (this.isDrawing) {
            double lastX = this.position.getX();
            double lastY = this.position.getY();
            double dist = Math.sqrt(Math.pow(x - lastX, 2) + Math.pow(y - lastY, 2));
            if (dist < 0.0001) {
                return;
            }

            if (this.drawingObject.getLastPath().size() > 0 && this.drawingObject.getLastPath().getLast().equals(this.position)) {
                this.drawingObject.deletePoint();
            }
            this.drawingObject.addPoint(this.position);
            this.drawingObject.addPoint(new Point(x, y));
        }

        this.position = new Point(x, y);
    }

    /**
     * Goes in the x direction by dx, and in the y direction by dy.
     * 
     * @param dx how far to move in the x direction.
     * @param dy how far to move in the y direction.
     */
    public void step(double dx, double dy) {
        double x = this.position.getX();
        double y = this.position.getY();
        this.goTo(x + dx, y + dy);
    }

    /**
     * Goes to the specified coordinate WITHOUT making a line.
     * 
     * @param x the destination's x value.
     * @param y the destination's y value.
     */
    public void jump(double x, double y) {
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
        double lastX = this.position.getX();
        double lastY = this.position.getY();
        double a = (this.angle / 180) * Math.PI;
        double x = (double) (lastX + distance * Math.cos(a));
        double y = (double) (lastY + distance * Math.sin(a));

        this.goTo(x, y);
    }

    /**
     * Draws an arc. Change the resolution with Turtle.setResolution
     * 
     * @param angle how much of an arc in degrees (e.g. 360 = full circle, 180 = semicircle, etc)
     * @param radius the radius of the arc.
     */
    public void arc(double angle, double radius) {
        if (angle * radius == 0) {
            return;
        }

        int n = resolution;
        ArrayList<Point> points = new ArrayList<Point>();
        angle = Math.toRadians(angle);
        
        for (int i = 0; i <= n; i++) {
            double theta = angle / n * i;
            points.add(new Point((double) (radius * Math.cos(theta)), (double) (radius * Math.sin(theta))));
        }

        Point initial = points.get(0).clone();
        for (Point point : points) {
            point.translate(this.position.getX(), this.position.getY(), initial.getX(), initial.getY());
            point.rotate(this.angle + (angle < 0 ? 90 : -90), this.position.getX(), this.position.getY());
        }

        for (int i = 1; i <= n; i++) {                
            this.goTo(points.get(i).getX(), points.get(i).getY());
            // if (i == 1) {
            //     this.jump(points.get(i).getX(), points.get(i).getY());
            // } else {
            // }
        }

        this.setAngle(this.angle + Math.toDegrees(angle));
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