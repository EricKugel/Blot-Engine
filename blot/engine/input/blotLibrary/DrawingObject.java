package blot.engine.input.blotLibrary;

import java.util.ArrayList;

public class DrawingObject extends ArrayList<ArrayList<Point>> {
    private ArrayList<Point> lastArrayList = null;

    public void newLine() {
        ArrayList<Point> arrayList = new ArrayList<Point>();
        add(arrayList);
        lastArrayList = arrayList;
    }

    public void addPoint(Point point) {
        lastArrayList.add(point);
    }

    public void deletePoint() {
        if (lastArrayList.size() > 0) {
            lastArrayList.remove(lastArrayList.size() - 1);
        }
    }

    public ArrayList<Point> getLastPath() {
        return lastArrayList;
    }
}