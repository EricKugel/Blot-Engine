package blot.engine.input.blotLibrary;

import java.util.ArrayList;

/**
 * A structure similar to Blot's polylines. 
 * The connection from the input layer to the processing layer.
 */
public class DrawingObject extends ArrayList<ArrayList<Point>> {
    private ArrayList<Point> lastArrayList = null;

    /**
     * Adds a new path to the DrawingObject. In practice, this equates to lifting the pen.
     */
    public void newLine() {
        ArrayList<Point> arrayList = new ArrayList<Point>();
        add(arrayList);
        lastArrayList = arrayList;
    }

    /**
     * Appends a point to the current path.
     * 
     * @param point the point to add to the current path.
     */
    public void addPoint(Point point) {
        lastArrayList.add(point);
    }

    /**
     * Removes the last point from the current point.
     */
    public void deletePoint() {
        if (lastArrayList.size() > 0) {
            lastArrayList.remove(lastArrayList.size() - 1);
        }
    }

    /**
     * Returns the last path in the DrawingObject.
     * 
     * @return the last path.
     */
    public ArrayList<Point> getLastPath() {
        return lastArrayList;
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
    public void confine(double left, double bottom, double right, double top) {
        double newWidth = right - left;
        double newHeight = top - bottom;

        double minLeft = Double.MAX_VALUE;
        double maxRight = -Double.MAX_VALUE;
        double minBottom = Double.MAX_VALUE;
        double maxTop = -Double.MAX_VALUE;

        for (ArrayList<Point> path : this) {
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

        double oldWidth = maxRight - minLeft;
        double oldHeight = maxTop - minBottom;

        double scaleX = newWidth / oldWidth;
        double scaleY = newHeight / oldHeight;

        Point oldCenter = new Point((minLeft + maxRight) / 2, (maxTop + minBottom) / 2);
        Point center = new Point((left + right) / 2, (top + bottom) / 2);

        for (ArrayList<Point> path : this) {
            for (Point point : path) {
                point.translate(center.getX(), center.getY(), oldCenter.getX(), oldCenter.getY());
                point.scale(scaleX, scaleY, center.getX(), center.getY());
            }
        }
    }
}