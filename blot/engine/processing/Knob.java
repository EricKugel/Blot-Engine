package blot.engine.processing;

import java.awt.Color;
import java.awt.Graphics;

import blot.engine.input.blotLibrary.Point;

public class Knob extends Point {
    public static final int LEFT_BOTTOM = 0;
    public static final int LEFT_CENTER = 1;
    public static final int LEFT_TOP = 2;
    public static final int CENTER_BOTTOM = 3;
    public static final int CENTER_TOP = 4;
    public static final int RIGHT_BOTTOM = 5;
    public static final int RIGHT_CENTER = 6;
    public static final int RIGHT_TOP = 7;
    public static final int CENTER_CENTER = 8;
    public static final int ROTATOR = 9;

    private double rotation;
    private int type;
    private CanvasObject canvasObject;

    private boolean isPressed = false;

    private Point renderPointHEY = new Point(-1, -1);

    public static Knob[] generateKnobs(CanvasObject canvasObject) {
        return new Knob[] {
            new Knob(-1, -1, canvasObject.getRotation(),Knob.LEFT_BOTTOM, canvasObject),
            new Knob(-1, 0, canvasObject.getRotation(), Knob.LEFT_CENTER, canvasObject),
            new Knob(-1, 1, canvasObject.getRotation(), Knob.LEFT_TOP, canvasObject),
            new Knob(0, -1, canvasObject.getRotation(), Knob.CENTER_BOTTOM, canvasObject),
            new Knob(0, 1, canvasObject.getRotation(), Knob.CENTER_TOP, canvasObject),
            new Knob(1, -1, canvasObject.getRotation(), Knob.RIGHT_BOTTOM, canvasObject),
            new Knob(1, 0, canvasObject.getRotation(), Knob.RIGHT_CENTER, canvasObject),
            new Knob(1, 1, canvasObject.getRotation(), Knob.RIGHT_TOP, canvasObject)
        };
    }

    public Knob(double x, double y, double rotation, int type, CanvasObject canvasObject) {
        super(x, y);
        this.rotation = rotation;
        this.type = type;
        this.canvasObject = canvasObject;
    }

    public void draw(Graphics g) {
        if (this.type <= 7) {
            g.setColor(this.isPressed ? Color.RED : Color.BLUE);
            // renderPoint = ((Point) this).clone();
            renderPointHEY = new Point(this.getX(), this.getY());
            renderPointHEY.scale(this.canvasObject.getWidth() / 2 * this.canvasObject.getScaleX(), this.canvasObject.getHeight() / 2 * this.canvasObject.getScaleY(), 0, 0);
            renderPointHEY.translate(this.canvasObject.getPosition().getX(), this.canvasObject.getPosition().getY(), 0, 0);
            renderPointHEY.rotate(this.canvasObject.getRotation(), this.canvasObject.getPosition().getX(), this.canvasObject.getPosition().getY());
            renderPointHEY = Canvas.transformToCanvas(renderPointHEY);
            g.fillArc((int) renderPointHEY.getX(), (int) renderPointHEY.getY(), 7, 7, 0, 360);
        }
    }

    public boolean contains(Point point) {
        if (renderPointHEY != null) {
            double distance = Math.hypot((point.getX() - this.renderPointHEY.getX()), (point.getY() - this.renderPointHEY.getY()));
            if (this.type == Knob.LEFT_TOP) {
                System.out.println(distance);
            }
            return (distance <= 4);
        }
        return false;
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed() {
        return this.isPressed;
    }
}

