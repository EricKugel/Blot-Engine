package blot.engine.processing;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import blot.engine.gui.Gui;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Point;

public class Canvas extends JPanel {
    public static final double WIDTH = 100;
    public static final double HEIGHT = 100;

    private ArrayList<CanvasObject> canvasObjects = new ArrayList<CanvasObject>();
    private CanvasObject focusedCanvasObject = null;

    private boolean isShiftPressed = false;

    private Gui gui;

    public Canvas(Gui gui) {
        this.gui = gui;
        Canvas canvas = this;
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (focusedCanvasObject != null) {
                    for (Knob knob : focusedCanvasObject.getKnobs()) {
                        if (knob.contains(new Point(x, y))) {
                            focusedCanvasObject.press(knob);
                        }
                    }
                    repaint();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (focusedCanvasObject != null) {
                    focusedCanvasObject.unpress();
                }
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (focusedCanvasObject != null && focusedCanvasObject.getPressedKnob() != null) {
                    focusedCanvasObject.getPressedKnob().drag(new Point(x, y), isShiftPressed);
                }
                repaint();
            }
            public void mouseMoved(MouseEvent e) {
                // it kept randomly losing focus so I'm just gonna do this...
                requestFocus();

                int x = e.getX();
                int y = e.getY();

                boolean hovering = false;
                if (focusedCanvasObject != null) {
                    for (Knob knob : focusedCanvasObject.getKnobs()) {
                        if (knob.contains(new Point(x, y))) {
                            hovering = true;
                        }
                    }
                }

                if (hovering) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        this.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = true;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = false;
                    if (focusedCanvasObject != null && focusedCanvasObject.getPressedKnob() != null) {
                        java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                        SwingUtilities.convertPointFromScreen(mouseLocation, canvas);
                        focusedCanvasObject.getPressedKnob().drag(new Point(mouseLocation.getX(), mouseLocation.getY()), isShiftPressed);
                        repaint();
                    }
                }
            }
            public void keyTyped(KeyEvent e) {
                
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
            if (!canvasObject.isFocused()) {
                canvasObject.draw(g);
            }
        }
        if (focusedCanvasObject != null) {
            focusedCanvasObject.draw(g);
        }
    }

    public void add(DrawingObject drawingObject) {
        this.add(new CanvasObject(drawingObject));
    }

    public void add(CanvasObject canvasObject) {
        this.canvasObjects.add(canvasObject);
        gui.refreshCanvasObjectList();
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

    public ArrayList<CanvasObject> getCanvasObjects() {
        return canvasObjects;
    }

    public void delete(CanvasObject canvasObject) {
        canvasObjects.remove(canvasObject);
        if (this.focusedCanvasObject == canvasObject) {
            unfocus();
        }
        gui.refreshCanvasObjectList();
        repaint();
    }

    public DrawingObject process() {
        if (canvasObjects.size() == 0)
            return null;
        
        DrawingObject output = new DrawingObject();
        for (CanvasObject canvasObject : canvasObjects) {
            DrawingObject drawingObject = canvasObject.getDrawingObject();
            Point position = canvasObject.getPosition();
            double scaleX = canvasObject.getScaleX();
            double scaleY = canvasObject.getScaleY();
            double rotation = canvasObject.getRotation();

            for (ArrayList<Point> path : drawingObject.clone().getPaths()) {
                for (Point point : path) {
                    point.translate(position.getX(), position.getY(), 50, 50);
                    point.scale(scaleX, scaleY, position.getX(), position.getY());
                    point.rotate(rotation, position.getX(), position.getY());
                }
                output.getPaths().add(path);
            }
        }
        return output;
    }

    public void clear() {
        Object[] canvasObjectsArray = canvasObjects.toArray();
        for (Object canvasObject : canvasObjectsArray) {
            delete((CanvasObject) canvasObject);
        }
    }
}
