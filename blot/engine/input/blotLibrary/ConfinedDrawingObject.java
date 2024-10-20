package blot.engine.input.blotLibrary;

import blot.engine.processing.Canvas;

/**
 * Offers some peace of mind that this DrawingObject will fit on a Canvas,
 * and that it's properly centered.
 */
public class ConfinedDrawingObject extends DrawingObject {
    public ConfinedDrawingObject(DrawingObject drawingObject) {
        super(drawingObject);

        Point[] bounds = getBounds();
        Point bottomLeft = bounds[0];
        Point topRight = Point.add(bottomLeft, bounds[1]);

        // TODO: Throw an exception?
        if (!(bottomLeft.getX() >= 0 && bottomLeft.getY() >= 0 && 
              topRight.getX() <= Canvas.WIDTH && topRight.getY() <= Canvas.HEIGHT)) {
            System.out.println("Invalid Confined Drawing Object"); 
        }
    }
}
