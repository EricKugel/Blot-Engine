package blot.engine.input.blotLibrary;

import blot.engine.processing.Canvas;

public class ConfinedDrawingObject extends DrawingObject {
    public ConfinedDrawingObject(DrawingObject drawingObject) {
        super(drawingObject);

        Point[] bounds = getBounds();
        Point bottomLeft = bounds[0];
        Point topRight = Point.add(bottomLeft, bounds[1]);

        if (!(bottomLeft.getX() >= 0 && bottomLeft.getY() >= 0 && 
              topRight.getX() <= Canvas.WIDTH && topRight.getY() <= Canvas.HEIGHT)) {
            System.out.println("Invalid Confined Drawing Object"); 
        }
    }
}
