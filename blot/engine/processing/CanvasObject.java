package blot.engine.processing;

import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Point;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class for representing a drawingObject on a canvas. 
 * Includes the center position, the rotation, and the scaling.
 */
public class CanvasObject {
    private static int totalObjects = 0;
    private DrawingObject drawingObject;
    private Point position;
    private double rotation;
    private double scaleX;
    private double scaleY;

    private double width;
    private double height;

    private boolean isFocused = false;
    private Knob pressedKnob = null;
    private Knob[] knobs = Knob.generateKnobs(this);

    private String name;

    /**
     * Construct a new canvasObject!
     * 
     * @param drawingObject The drawingObject to wrap
     */
    public CanvasObject(DrawingObject drawingObject) {
        this.drawingObject = drawingObject;
        this.rotation = 0;
        this.scaleX = 1;
        this.scaleY = 1;

        Point[] bounds = drawingObject.getBounds();
        this.width = bounds[1].getX();
        this.height = bounds[1].getY();

        this.position = Point.add(bounds[0], Point.mult(bounds[1], 0.5));
        drawingObject.setOrigin(this.position);

        totalObjects += 1;
        this.setName("Canvas Object " + totalObjects);
    }

    /**
     * Construct a new canvasObject with a special name!
     * 
     * @param drawingObject The drawingObject to wrap
     * @param name The canvasObject's new name
     */
    public CanvasObject(DrawingObject drawingObject, String name) {
        this(drawingObject);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Draw the drawingObject (transformed), and the knobs around it if needed
     * 
     * @param g The graphics to draw on
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        for (ArrayList<Point> path : this.drawingObject.getPaths()) {
            ArrayList<Point> transformedPath = new ArrayList<Point>();
            for (int i = 0; i < path.size(); i++) {
                Point point = path.get(i).clone();
                point.translate(position.getX(), position.getY(), Canvas.HEIGHT / 2, Canvas.WIDTH / 2);
                point.scale((double) this.scaleX, (double) this.scaleY, position.getX(), position.getY());
                if (rotation != 0) {
                    point.rotate(this.rotation, position.getX(), position.getY());
                }   
                transformedPath.add(point);
            }
            for (int i = 0; i < transformedPath.size() - 1; i++) {
                Point start = transformedPath.get(i);
                Point end = transformedPath.get(i + 1);
                start = Canvas.transformToCanvas(start);
                end = Canvas.transformToCanvas(end);
                g.drawLine(
                    (int) start.getX(),
                    (int) start.getY(),
                    (int) end.getX(),
                    (int) end.getY()
                );
            }
        }

        if (this.isFocused()) {
            for (Knob knob : knobs) {
                knob.draw(g);
            }

            // Draw lines between all knobs except center_center
            g.setColor(Color.BLUE);
            for (int i = 0; i < knobs.length - 2; i++) {
                g.drawLine((int) knobs[i].getRenderPoint().getX(), (int) knobs[i].getRenderPoint().getY(), (int) knobs[i + 1].getRenderPoint().getX(), (int) knobs[i + 1].getRenderPoint().getY());
            }
            g.drawLine((int) knobs[0].getRenderPoint().getX(), (int) knobs[0].getRenderPoint().getY(), (int) knobs[6 + 1].getRenderPoint().getX(), (int) knobs[6 + 1].getRenderPoint().getY());
        }
    }

    /**
     * Which knob we're dragging around
     * 
     * @param knob The knob we're dragging around
     */
    public void press(Knob knob) {
        if (this.pressedKnob != null) {
            this.pressedKnob.setPressed(false);
        }
        this.pressedKnob = knob;
        knob.setPressed(true);
    }

    /**
     * Let go of all knobs
     */
    public void unpress() {
        if (this.pressedKnob != null) {
            this.pressedKnob.setPressed(false);
        }
        this.pressedKnob = null;
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

    public void setFocused(boolean isFocused) {
        if (!isFocused) {
            this.unpress();
        }
        this.isFocused = isFocused;
    }

    public boolean isFocused() {
        return this.isFocused;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public Knob[] getKnobs() {
        return this.knobs;
    }

    public Knob getPressedKnob() {
        return this.pressedKnob;
    }

    public String getName() {
        return this.name;
    }
}