package blot.engine.processing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import blot.engine.gui.Gui;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Point;

public class Canvas extends JPanel {
    public static final double WIDTH = 100;
    public static final double HEIGHT = 100;

    private ArrayList<CanvasObject> canvasObjects = new ArrayList<CanvasObject>();
    private CanvasObject focusedCanvasObject = null;

    public Canvas() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                for (Knob knob : getFocusedCanvasObject().getKnobs()) {
                    if (knob.contains(new Point(x, y))) {
                        getFocusedCanvasObject().press(knob);
                    }
                }
                repaint();
            }
            public void mouseReleased(MouseEvent e) {
                focusedCanvasObject.unpress();
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (focusedCanvasObject.getPressedKnob() != null) {
                    focusedCanvasObject.getPressedKnob().drag(new Point(x, y));
                }
                repaint();
            }
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                boolean hovering = false;
                for (Knob knob : getFocusedCanvasObject().getKnobs()) {
                    if (knob.contains(new Point(x, y))) {
                        hovering = true;
                    }
                }

                if (hovering) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    public static Point transformToCanvas(Point point) {
        return new Point(point.getX() * (Gui.CANVAS_SIZE / WIDTH), Gui.CANVAS_SIZE - point.getY() * (Gui.CANVAS_SIZE / HEIGHT));
    }

    public static Point transformFromCanvas(Point point) {
        return new Point(point.getX() / (Gui.CANVAS_SIZE / WIDTH), (Gui.CANVAS_SIZE - point.getY()) / (Gui.CANVAS_SIZE / HEIGHT));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Gui.CANVAS_SIZE, Gui.CANVAS_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, Gui.CANVAS_SIZE, Gui.CANVAS_SIZE);
        for (CanvasObject canvasObject : this.canvasObjects) {
            g.setColor(Color.BLACK);
            canvasObject.draw(g);
        }
    }

    public void add(DrawingObject drawingObject) {
        this.canvasObjects.add(new CanvasObject(drawingObject));
        this.repaint();
    }

    public void add(CanvasObject canvasObject) {
        this.canvasObjects.add(canvasObject);
        this.repaint();
    }

    public void focus(CanvasObject canvasObject) {
        if (this.focusedCanvasObject != null) {
            this.focusedCanvasObject.setFocused(false);
        }
        canvasObject.setFocused(true);
        this.focusedCanvasObject = canvasObject;
    }

    public void unfocus() {
        if (this.focusedCanvasObject != null) {
            this.focusedCanvasObject.setFocused(false);
        }
        this.focusedCanvasObject = null;
    }

    public CanvasObject getFocusedCanvasObject() {
        return this.focusedCanvasObject;
    }
}
