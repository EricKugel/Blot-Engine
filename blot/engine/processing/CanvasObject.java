package blot.engine.processing;

import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Point;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class for representing a drawing object on a canvas. 
 * Includes the center position, the rotation, and the scaling.
 */
public class CanvasObject {
    private DrawingObject drawingObject;
    private Point position;
    private double rotation;
    private double scaleX;
    private double scaleY;

    public CanvasObject(DrawingObject drawingObject) {
        this.drawingObject = drawingObject;
        this.position = new Point(Canvas.WIDTH / 2, Canvas.HEIGHT / 2);
        this.rotation = 0;
        this.scaleX = 1;
        this.scaleY = 1;
    }

    public void draw(Graphics g) {
        Rectangle bounds = g.getClipBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        for (ArrayList<Point> path : this.drawingObject) {
            ArrayList<Point> transformedPath = new ArrayList<Point>();
            for (int i = 0; i < path.size(); i++) {
                Point point = path.get(i).clone();
                point.translate(position.getX(), position.getY(), Canvas.HEIGHT / 2, Canvas.WIDTH / 2);
                point.scale((float) this.scaleX, (float) this.scaleY, position.getX(), position.getY());
                if (rotation != 0) {
                    point.rotate(this.rotation, position.getX(), position.getY());
                }   
                transformedPath.add(point);
            }
            for (int i = 0; i < transformedPath.size() - 1; i++) {
                Point start = transformedPath.get(i);
                Point end = transformedPath.get(i + 1);
                g.drawLine((int) (start.getX() / Canvas.WIDTH * width), (int) (start.getY() / Canvas.HEIGHT * height), (int) (end.getX() / Canvas.WIDTH * width), (int) (end.getY() / Canvas.HEIGHT * height));
            }
        }
    }

    public DrawingObject getDrawingObject() {
        return this.drawingObject;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }
}