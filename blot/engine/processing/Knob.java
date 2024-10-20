package blot.engine.processing;

import java.awt.Color;
import java.awt.Graphics;

import blot.engine.input.blotLibrary.Point;

/**
 * The things around an image in MS Paint that let you scale the image and stuff
 */
public class Knob extends Point {
    public static final int RIGHT_TOP = 0;
    public static final int RIGHT_CENTER = 1;
    public static final int RIGHT_BOTTOM = 2;
    public static final int CENTER_BOTTOM = 3;
    public static final int LEFT_BOTTOM = 4;
    public static final int LEFT_CENTER = 5;
    public static final int LEFT_TOP = 6;
    public static final int CENTER_TOP = 7;
    public static final int ROTATOR = 8;
    public static final int CENTER_CENTER = 9;

    private int type;
    private CanvasObject canvasObject;

    private boolean isPressed = false;

    private Point renderPoint = new Point(-1, -1);

    /**
     * This is in a very specific order--don't change!
     * This is so gross...
     * 
     * @param canvasObject
     * @return
     */
    public static Knob[] generateKnobs(CanvasObject canvasObject) {
        return new Knob[] {
            new Knob(1, 1, Knob.RIGHT_TOP, canvasObject),
            new Knob(1, 0, Knob.RIGHT_CENTER, canvasObject),
            new Knob(1, -1, Knob.RIGHT_BOTTOM, canvasObject),
            new Knob(0, -1, Knob.CENTER_BOTTOM, canvasObject),
            new Knob(-1, -1, Knob.LEFT_BOTTOM, canvasObject),
            new Knob(-1, 0, Knob.LEFT_CENTER, canvasObject),
            new Knob(-1, 1, Knob.LEFT_TOP, canvasObject),
            new Knob(0, 1, Knob.CENTER_TOP, canvasObject),
            new Knob(0, 0, Knob.ROTATOR, canvasObject),
            new Knob(0, 0, Knob.CENTER_CENTER, canvasObject)
        };
    }

    /**
     * Create a knob at the coordinates x, y (which are both -1 -> 1)
     * 
     * @param x The x coordinate, -1 to 1
     * @param y The y coordinate, -1 to 1
     * @param type The type of knob. Should be a Knob.RIGHT_TOP, Knob.CENTER_BOTTOM, etc
     * @param canvasObject The parent canvasObject to scale and move and rotate
     */
    public Knob(double x, double y, int type, CanvasObject canvasObject) {
        super(x, y);
        this.type = type;
        this.canvasObject = canvasObject;
    }

    /**
     * Draw a circle for everything except CENTER_CENTER -- draw a crosshairs
     * 
     * @param g The graphics to draw on
     */
    public void draw(Graphics g) {
        g.setColor(this.isPressed ? Color.RED : Color.BLUE);
        
        if (type == ROTATOR) {
            Knob referenceKnob = null;
            for (Knob knob : this.canvasObject.getKnobs()) {
                if (knob.type == CENTER_TOP) {
                    referenceKnob = knob;
                }
            }

            Point difference = new Point(0, 30);
            difference.rotate(-this.canvasObject.getRotation(), 0, 0);
            renderPoint = Point.sub(referenceKnob.getRenderPoint(), difference);
        } else {
            renderPoint = ((Point) this).clone();
            renderPoint.scale(this.canvasObject.getWidth() / 2 * this.canvasObject.getScaleX(), this.canvasObject.getHeight() / 2 * this.canvasObject.getScaleY(), 0, 0);
            renderPoint.translate(this.canvasObject.getPosition().getX(), this.canvasObject.getPosition().getY(), 0, 0);
            renderPoint.rotate(this.canvasObject.getRotation(), this.canvasObject.getPosition().getX(), this.canvasObject.getPosition().getY());
            renderPoint = Canvas.transformToCanvas(renderPoint);
        }
        
        if (this.type != CENTER_CENTER) {
            g.fillArc((int) renderPoint.getX() - 3, (int) renderPoint.getY() - 3, 6, 6, 0, 360);
        } else {
            int crossSize = 7;
            Point[] vertices = {
                Point.add(renderPoint, new Point(crossSize, 0)),
                Point.add(renderPoint, new Point(-crossSize, 0)),
                Point.add(renderPoint, new Point(0, crossSize)),
                Point.add(renderPoint, new Point(0, -crossSize)),
            };
            for (Point vertex : vertices) {
                vertex.rotate(-this.canvasObject.getRotation(), renderPoint.getX(), renderPoint.getY());
            }
            g.drawLine((int) vertices[0].getX(), (int) vertices[0].getY(), (int) vertices[1].getX(), (int) vertices[1].getY());
            g.drawLine((int) vertices[2].getX(), (int) vertices[2].getY(), (int) vertices[3].getX(), (int) vertices[3].getY());
        } 
    }

    /**
     * Helper function to see if the mouse is close
     * 
     * @param point The mouse location
     * @return Am I here?
     */
    public boolean contains(Point point) {
        int maxDistance = type == CENTER_CENTER ? 6 : 4;
        if (renderPoint != null) {
            double distance = Math.hypot((point.getX() - this.renderPoint.getX()), (point.getY() - this.renderPoint.getY()));
            return (distance <= maxDistance);
        }
        return false;
    }

    /**
     * If you ever need to debug this...
     * The lord have mercy on your soul.
     * 
     * Sending thoughts and prayers
     * 
     * @param point Where the knob was dragged to
     * @param isShiftPressed Whether the shift button is pressed
     */
    public void drag(Point point, boolean isShiftPressed) {
        Point difference = Point.sub(Canvas.transformFromCanvas(point), Canvas.transformFromCanvas(renderPoint));
        // if shift pressed and on a diagonal, project the difference onto the diagonal...
        // I feel like a genius
        if ((this.type == LEFT_TOP || this.type == RIGHT_TOP ||
             this.type == LEFT_BOTTOM || this.type == RIGHT_BOTTOM) && isShiftPressed) {
            Point goodDifference = Point.sub(Canvas.transformFromCanvas(renderPoint), canvasObject.getPosition());
            goodDifference = Point.direction(goodDifference);
            difference = Point.mult(goodDifference, Point.dot(difference, goodDifference));
        }

        difference.rotate(-canvasObject.getRotation(), 0, 0);

        if (this.type < 8) {
            Point widthDifference = difference.clone();
            if (4 <= this.type && this.type < 7) { // left
                widthDifference.setX(-widthDifference.getX());
            } if (2 <= this.type && this.type < 5) { // bottom
                widthDifference.setY(-widthDifference.getY());
            }

            double newScaleX = (canvasObject.getWidth() * canvasObject.getScaleX() + widthDifference.getX()) / canvasObject.getWidth();
            double newScaleY = (canvasObject.getHeight() * canvasObject.getScaleY() + widthDifference.getY()) / canvasObject.getHeight();
        
            if (!(this.type == CENTER_TOP || this.type == CENTER_BOTTOM)) canvasObject.setScaleX(newScaleX);
            if (!(this.type == LEFT_CENTER || this.type == RIGHT_CENTER)) canvasObject.setScaleY(newScaleY);

            if (this.type == LEFT_CENTER || this.type == RIGHT_CENTER) difference.setY(0);
            if (this.type == CENTER_TOP || this.type == CENTER_BOTTOM) difference.setX(0);
            difference.rotate(canvasObject.getRotation(), 0, 0);
            canvasObject.setPosition(new Point(canvasObject.getPosition().getX() + difference.getX() / 2, canvasObject.getPosition().getY() + difference.getY() / 2));
        } else if (this.type == ROTATOR) {
            Point vector = Point.sub(Canvas.transformFromCanvas(point), canvasObject.getPosition());
            double angle = 0;
            if (vector.getX() == 0) {
                angle = vector.getY() > 0 ? 90 : 270;
            } else {
                angle = Math.toDegrees(Math.atan(vector.getY() / vector.getX())) + (vector.getX() < 0 ? 180 : 0);
            }
            canvasObject.setRotation(angle - 90 + (canvasObject.getScaleY() < 0 ? 180 : 0));
        } else if (this.type == CENTER_CENTER) {
            difference.rotate(canvasObject.getRotation(), 0, 0);
            canvasObject.setPosition(new Point(canvasObject.getPosition().getX() + difference.getX(), canvasObject.getPosition().getY() + difference.getY()));
        }
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public Point getRenderPoint() {
        return renderPoint;
    }
}

