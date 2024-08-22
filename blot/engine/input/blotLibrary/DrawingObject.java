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
}