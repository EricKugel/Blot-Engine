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
        return angle;
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
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Lifts the pen.
     */
    public void up() {
        if (!isDrawing) {
            return;
        }
        isDrawing = false;
        drawingObject.addPoint(position);
        drawingObject.newLine();
    }

    /**
     * Sets the pen down.
     */
    public void down() {
        isDrawing = true;
    }

    /**
     * Goes to the specified coordinate, regardless if the pen is up or down.
     * 
     * @param x the destination's x value.
     * @param y the destination's y value.
     */
    public void goTo(double x, double y) {
        if (isDrawing) {
            double lastX = position.getX();
            double lastY = position.getY();
            double dist = Math.sqrt(Math.pow(x - lastX, 2) + Math.pow(y - lastY, 2));
            if (dist < 0.0001) {
                return;
            }

            if (drawingObject.getLastPath().size() > 0 && drawingObject.getLastPath().getLast().equals(position)) {
                drawingObject.deletePoint();
            }
            drawingObject.addPoint(position);
            drawingObject.addPoint(new Point(x, y));
        }

        position = new Point(x, y);
    }

    /**
     * Goes in the x direction by dx, and in the y direction by dy.
     * 
     * @param dx how far to move in the x direction.
     * @param dy how far to move in the y direction.
     */
    public void step(double dx, double dy) {
        double x = position.getX();
        double y = position.getY();
        goTo(x + dx, y + dy);
    }

    /**
     * Goes to the specified coordinate WITHOUT making a line.
     * 
     * @param x the destination's x value.
     * @param y the destination's y value.
     */
    public void jump(double x, double y) {
        if (drawingObject.getLastPath().size() == 1) {
            drawingObject.deletePoint();
            drawingObject.addPoint(new Point(x, y));
            return;
        }

        up();
        goTo(x, y);
        down();
    }

    /**
     * Moves the blot a certain distance in the current direction, regardless if the pen is up or down.
     * 
     * @param distance how far to move.
     */
    public void forward(double distance) {
        double lastX = position.getX();
        double lastY = position.getY();
        double a = (angle / 180) * Math.PI;
        double x = (double) (lastX + distance * Math.cos(a));
        double y = (double) (lastY + distance * Math.sin(a));

        goTo(x, y);
    }

    /**
     * Draws an arc. Change the resolution with Turtle.setResolution
     * 
     * @param angle how much of an arc in degrees (e.g. 360 = full circle, 180 = semicircle, etc)
     * @param radius the radius of the arc.
     */
    public void arc(double angle, double radius) {
        ellipticalArc(angle, radius, radius);
    }

    /**
     * Draws an elliptical arc. Change the resolution with Turtle.setResolution
     * 
     * @param angle
     * @param xRadius
     * @param yRadius
     */
    public void ellipticalArc(double angle, double xRadius, double yRadius) {
        if (angle * xRadius * yRadius == 0) {
            return;
        }

        int n = resolution;
        ArrayList<Point> points = new ArrayList<Point>();
        angle = Math.toRadians(angle);
        
        for (int i = 0; i <= n; i++) {
            double theta = angle / n * i;
            points.add(new Point((double) (xRadius * Math.cos(theta)), (double) (yRadius * Math.sin(theta))));
        }

        Point initial = points.get(0).clone();
        for (Point point : points) {
            point.translate(position.getX(), position.getY(), initial.getX(), initial.getY());
            point.rotate(this.angle + (angle < 0 ? 90 : -90), position.getX(), position.getY());
        }

        for (int i = 1; i <= n; i++) {                
            goTo(points.get(i).getX(), points.get(i).getY());
        }

        this.angle += Math.toDegrees(angle);
    }

    /**
     * Draws a quadratic bezier curve (if the pen is down).
     * 
     * @param x1 X component of control point
     * @param y1 Y component of control point
     * @param x2 X component of ending point
     * @param y2 Y component of ending point
     */
    public void quadraticBezier(double x1, double y1, double x2, double y2) {
        double x0 = position.getX();
        double y0 = position.getY();
        for (int i = 0; i <= resolution; i++) {
            double t = (double) i / resolution;
            double x = (1-t)*(1-t)*x0 + 2*(1-t)*t*x1 + t*t*x2;
            double y = (1-t)*(1-t)*y0 + 2*(1-t)*t*y1 + t*t*y2;
            goTo(x, y);
        }
    }

    /**
     * Helper function because Java doesn't have unpacking.
     * 
     * @param buffer The 2 coords of the quadratic bezier
     */
    public void quadraticBezier(double[] buffer) {
        quadraticBezier(buffer[0], buffer[1], buffer[2], buffer[3]);
    }

    /**
     * Helper function to use relative coordinates.
     * 
     * @param buffer The 2 relative coords of the quadratic bezier
     */
    public void relativeQuadraticBezier(double[] buffer) {
        quadraticBezier(buffer[0] + position.getX(), buffer[1] + position.getY(), 
                        buffer[2] + position.getX(), buffer[3] + position.getY());
    }

    /**
     * Draws a cubic bezier curve (if the pen is down).
     * 
     * @param x1 The x-component of the first control point
     * @param y1 The y-component of the first control point
     * @param x2 The x-component of the second control point
     * @param y2 The y-component of the second control point
     * @param x3 The x-component of the ending point
     * @param y3 The y-component of the ending point
     */
    public void cubicBezier(double x1, double y1, double x2, double y2, double x3, double y3) {
        double x0 = position.getX();
        double y0 = position.getY();
        for (int i = 0; i <= resolution; i++) {
            double t = (double) i / resolution;
            double x = Math.pow(1-t, 3)*x0 + 3*(1-t)*(1-t)*t*x1 + 3*(1-t)*t*t*x2 + t*t*t*x3;
            double y = Math.pow(1-t, 3)*y0 + 3*(1-t)*(1-t)*t*y1 + 3*(1-t)*t*t*y2 + t*t*t*y3;
            goTo(x, y);
        }
    }

    /**
     * Helper function because Java doesn't have unpacking.
     * 
     * @param buffer The 3 coords of the cubic bezier
     */
    public void cubicBezier(double[] buffer) {
        cubicBezier(buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], buffer[5]);
    }

    /**
     * Helper function to use relative coordinates.
     * 
     * @param buffer The 3 relative coords of the cubic bezier
     */
    public void relativeCubicBezier(double[] buffer) {
        cubicBezier(buffer[0] + position.getX(), buffer[1] + position.getY(), buffer[2] + position.getX(),
                    buffer[3] + position.getY(), buffer[4] + position.getX(), buffer[5] + position.getY());
    }

    /**
     * Turns to the right a certain number of degrees.
     * 
     * @param theta the number of degrees to turn to the right
     */
    public void right(double theta) {
        angle -= theta;
    }

    public void svgArc(double xRadius, double yRadius, double rotation, 
                       double flag1, double flag2, double x, double y) {
        // TODO: Implement at your own peril
        goTo(x, y);
    }

    /**
     * Turns to the left a certain number of degrees.
     * 
     * @param theta the number of degrees to turn to the left.
     */
    public void left(double theta) {
        angle += theta;
    }

    /**
     * Returns the paths made by the turtle as a DrawingObject.
     * 
     * @return the DrawingObject paths created by the turtle.
     */
    public DrawingObject getDrawingObject() {
        return drawingObject;
    }
    
    /**
     * Goes back to the first point.
     */
    public void closePath() {
        if (drawingObject.getLastPath().size() > 0) {
            Point point = drawingObject.getLastPath().get(0);
            goTo(point.getX(), point.getY());
        }
    }
}