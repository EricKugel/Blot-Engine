package blot.engine.input.blotLibrary;

import java.util.ArrayList;

import blot.engine.processing.Canvas;

/**
 * A structure similar to Blot's polylines. 
 * The connection from the input layer to the processing layer.
 */
public class DrawingObject {
    private ArrayList<ArrayList<Point>> paths = new ArrayList<ArrayList<Point>>();
    private Point origin;

    public DrawingObject() {
        this.origin = new Point(Canvas.WIDTH / 2, Canvas.HEIGHT / 2);
    }

    public DrawingObject(DrawingObject drawingObject) {
        DrawingObject clone = drawingObject.clone();
        paths = clone.paths;
        origin = clone.origin;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        for (ArrayList<Point> path : paths) {
            for (Point point : path) {
                point.translate(this.origin.getX(), this.origin.getY(), origin.getX(), origin.getY());
            }
        }
        this.origin = origin;
    }

    /**
     * Adds a new path to the DrawingObject. In practice, this equates to lifting the pen.
     */
    public void newLine() {
        ArrayList<Point> arrayList = new ArrayList<Point>();
        paths.add(arrayList);
    }

    /**
     * Appends a point to the current path.
     * 
     * @param point the point to add to the current path.
     */
    public void addPoint(Point point) {
        paths.get(paths.size() - 1).add(point);
    }

    /**
     * Removes the last point from the current point.
     */
    public void deletePoint() {
        if (getLastPath().size() > 0) {
            getLastPath().remove(getLastPath().size() - 1);
        }
    }

    /**
     * Returns the last path in the DrawingObject.
     * 
     * @return the last path.
     */
    public ArrayList<Point> getLastPath() {
        return paths.getLast();
    }

    /**
     * Rescales and translates a drawing object to fit
     * within a specified bounds.
     * 
     * @param left the bound's left x value
     * @param bottom the bound's bottom y value
     * @param right the bound's right x value
     * @param top the bound's top y value
     */
    public ConfinedDrawingObject confine(double left, double bottom, double right, double top) {
        double newWidth = right - left;
        double newHeight = top - bottom;

        Point[] bounds = getBounds();

        double scale = Math.min(newWidth / bounds[1].getX(), newHeight / bounds[1].getY());

        Point oldCenter = Point.add(bounds[0], Point.mult(bounds[1], 0.5));
        Point center = new Point((left + right) / 2, (top + bottom) / 2);

        for (ArrayList<Point> path : paths) {
            for (Point point : path) {
                point.translate(center.getX(), center.getY(), oldCenter.getX(), oldCenter.getY());
                point.scale(scale, scale, center.getX(), center.getY());
            }
        }
        return new ConfinedDrawingObject(this);
    }

    /**
     * Gets the bounds for the drawingObject.
     * 
     * @return Two points: the bottom left corner, and the width/height.
     */
    public Point[] getBounds() {
        double minLeft = Double.MAX_VALUE;
        double maxRight = -Double.MAX_VALUE;
        double minBottom = Double.MAX_VALUE;
        double maxTop = -Double.MAX_VALUE;

        for (ArrayList<Point> path : paths) {
            for (Point point : path) {
                if (point.getX() < minLeft) {
                    minLeft = point.getX();
                } if (point.getX() > maxRight) {
                    maxRight = point.getX();
                } if (point.getY() < minBottom) {
                    minBottom = point.getY();
                } if (point.getY() > maxTop) {
                    maxTop = point.getY();
                }
            }
        }

        double width = maxRight - minLeft;
        double height = maxTop - minBottom;
        return new Point[] {new Point(minLeft, minBottom), new Point(width, height)};
    }

    public ArrayList<ArrayList<Point>> getPaths() {
        return paths;
    }

    @Override
    public DrawingObject clone() {
        DrawingObject clone = new DrawingObject();
        for (ArrayList<Point> path : paths) {
            ArrayList<Point> pathClone = new ArrayList<Point>();
            for (Point point : path) {
                pathClone.add(point.clone());
            }
            clone.getPaths().add(pathClone);
        }
        clone.setOrigin(origin);

        return clone;
    }

    public void update(DrawingObject drawingObject) {
        for (ArrayList<Point> path : drawingObject.clone().getPaths()) {
            paths.add(path);
        }
    }
}