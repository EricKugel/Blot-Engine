package blot.engine.input.blotLibrary;

import java.util.ArrayList;

public class Turtle {
    private boolean isDrawing = true;
    private Point position = new Point(0, 0);
    private double angle = 0;
    private DrawingObject drawingObject = new DrawingObject();

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

    public void up() {
        if (!this.isDrawing) {
            return;
        }
        this.isDrawing = false;
        this.drawingObject.addPoint(position);
    }

    public void down() {
        this.isDrawing = true;
    }

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

    public void step(float dx, float dy) {
        float x = this.position.getX();
        float y = this.position.getY();
        this.goTo(x + dx, y + dy);
    }

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

    public void forward(double distance) {
        float lastX = this.position.getX();
        float lastY = this.position.getY();
        double a = (this.angle / 180) * Math.PI;
        float x = (float) (lastX + distance * Math.cos(a));
        float y = (float) (lastY + distance * Math.sin(a));

        this.goTo(x, y);
    }

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

    public void right(double theta) {
        this.angle -= theta;
    }

    public void left(double theta) {
        this.angle += theta;
    }

    public DrawingObject getDrawingObject() {
        return drawingObject;
    }
}