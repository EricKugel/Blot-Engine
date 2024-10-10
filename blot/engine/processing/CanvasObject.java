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

    private double width;
    private double height;

    private boolean isFocused = false;
    private Knob pressedKnob = null;

    private Knob[] knobs = Knob.generateKnobs(this);

    public CanvasObject(DrawingObject drawingObject) {
        this.drawingObject = drawingObject;
        this.position = new Point(Canvas.WIDTH / 2, Canvas.HEIGHT / 2);
        this.rotation = 0;
        this.scaleX = 1;
        this.scaleY = 1;

        double minLeft = Double.MAX_VALUE;
        double maxRight = -Double.MAX_VALUE;
        double minBottom = Double.MAX_VALUE;
        double maxTop = -Double.MAX_VALUE;

        for (ArrayList<Point> path : drawingObject) {
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

        this.width = maxRight - minLeft;
        this.height = maxTop - minBottom;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        for (ArrayList<Point> path : this.drawingObject) {
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
                Point difference = Point.sub(knobs[i + 1].getRenderPoint(), knobs[i].getRenderPoint());
                // Point midPoint1 = Point.add(knobs[i].getRenderPoint(), Point.mult(difference, 0.2));
                // Point midPoint2 = Point.sub(knobs[i + 1].getRenderPoint(), Point.mult(difference, .2));
                g.drawLine((int) knobs[i].getRenderPoint().getX(), (int) knobs[i].getRenderPoint().getY(), (int) knobs[i + 1].getRenderPoint().getX(), (int) knobs[i + 1].getRenderPoint().getY());
                
                // g.drawLine((int) knobs[i].getRenderPoint().getX(), (int) knobs[i].getRenderPoint().getY(), (int) midPoint1.getX(), (int) midPoint2.getY());
                // g.drawLine((int) midPoint2.getX(), (int) midPoint2.getY(), (int) knobs[i + 1].getRenderPoint().getX(), (int) knobs[i + 1].getRenderPoint().getY());

                // g.drawLine((int) knobs[i + 1].getRenderPoint().getX(), (int) knobs[i + 1].getRenderPoint().getY(), (int) midPoint2.getX(), (int) midPoint2.getY());
            }

            Point difference = Point.sub(knobs[7].getRenderPoint(), knobs[0].getRenderPoint());
            // Point midPoint1 = Point.add(knobs[0].getRenderPoint(), Point.mult(difference, 0.33));
            // Point midPoint2 = Point.add(knobs[0].getRenderPoint(), Point.mult(difference, 0.66));
            // g.drawLine((int) knobs[0].getRenderPoint().getX(), (int) knobs[0].getRenderPoint().getY(), (int) midPoint1.getX(), (int) midPoint2.getY());
            // g.drawLine((int) midPoint2.getX(), (int) midPoint2.getY(), (int) knobs[7].getRenderPoint().getX(), (int) knobs[7].getRenderPoint().getY());
            g.drawLine((int) knobs[0].getRenderPoint().getX(), (int) knobs[0].getRenderPoint().getY(), (int) knobs[6 + 1].getRenderPoint().getX(), (int) knobs[6 + 1].getRenderPoint().getY());
        
        }
    }

    public void press(Knob knob) {
        if (this.pressedKnob != null) {
            this.pressedKnob.setPressed(false);
        }
        this.pressedKnob = knob;
        knob.setPressed(true);
    }

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
}