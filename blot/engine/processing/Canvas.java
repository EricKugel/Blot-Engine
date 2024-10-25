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

/**
 * Draws the drawingObjects on the screen as canvasObjects. Allows for these canvasObjects
 * to be transformed/rearranged before processing to one drawingObject.
 */
public class Canvas extends JPanel {
    public static final double WIDTH = 100;
    public static final double HEIGHT = 100;

    private ArrayList<CanvasObject> canvasObjects = new ArrayList<CanvasObject>();
    private CanvasObject focusedCanvasObject = null;

    private boolean isShiftPressed = false;

    private Gui gui;

    /**
     * Constructs a Canvas and its extended JPanel. Adds the mouse/keyboard listeners
     * 
     * @param gui The parent Gui, to allow for commands to be sent back up.
     */
    public Canvas(Gui gui) {
        this.gui = gui;
        Canvas canvas = this;
        addMouseListener(new MouseAdapter() {
            /**
             * Check if we should be worried about the knob logic
             */
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
            /**
             * Let go!
             */
            public void mouseReleased(MouseEvent e) {
                if (focusedCanvasObject != null) {
                    focusedCanvasObject.unpress();
                }
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            /**
             * Here we go...........
             * 
             * @param e The mouse event
             */
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (focusedCanvasObject != null && focusedCanvasObject.getPressedKnob() != null) {
                    focusedCanvasObject.getPressedKnob().drag(new Point(x, y), isShiftPressed);
                }
                repaint();
            }
            /**
             * All the professionals do this, I've been told
             * 
             * @param e
             */
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
        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = true;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftPressed = false;
                    if (focusedCanvasObject != null && focusedCanvasObject.getPressedKnob() != null) {
                        // Snap!
                        java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                        SwingUtilities.convertPointFromScreen(mouseLocation, canvas);
                        focusedCanvasObject.getPressedKnob().drag(new Point(mouseLocation.getX(), mouseLocation.getY()), isShiftPressed);
                        repaint();
                    }
                }
            }
            public void keyTyped(KeyEvent e) {}
        });
    }

    /**
     * Invert the y-component, scale to screen size
     * 
     * @param point The point in question
     * @return A new point, transformed to the Canvas
     */
    public static Point transformToCanvas(Point point) {
        return new Point(point.getX() * (Gui.CANVAS_SIZE / WIDTH), Gui.CANVAS_SIZE - point.getY() * (Gui.CANVAS_SIZE / HEIGHT));
    }

    /**
     * ezis neercs ot elacs ,tnenopmoc-y eht trevnI
     * 
     * @param point The point in question
     * @return A new point, transformed back to our little imaginary world where everything makes sense
     */
    public static Point transformFromCanvas(Point point) {
        return new Point(point.getX() / (Gui.CANVAS_SIZE / WIDTH), (Gui.CANVAS_SIZE - point.getY()) / (Gui.CANVAS_SIZE / HEIGHT));
    }

    /**
     * All the professionals do this, I've been told
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Gui.CANVAS_SIZE, Gui.CANVAS_SIZE);
    }

    /**
     * Draw all the stuff!
     */
    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, Gui.CANVAS_SIZE, Gui.CANVAS_SIZE);
        for (CanvasObject canvasObject : canvasObjects) {
            if (!canvasObject.isFocused()) {
                canvasObject.draw(g);
            }
        }
        if (focusedCanvasObject != null) {
            focusedCanvasObject.draw(g);
        }
    }

    /**
     * Adds the drawingObject as a canvasObject and its button
     * to the side bar.
     * 
     * @param drawingObject
     */
    public void add(DrawingObject drawingObject) {
        add(new CanvasObject(drawingObject));
    }

    /**
     * Adds the canvasObject and its button to the sidebar.
     * 
     * @param canvasObject The canvasObject to add
     */
    public void add(CanvasObject canvasObject) {
        canvasObjects.add(canvasObject);
        gui.refreshCanvasObjectList();
        repaint();
    }

    /**
     * Which canvasObject should have its knobs drawn?
     * 
     * @param canvasObject
     */
    public void focus(CanvasObject canvasObject) {
        if (focusedCanvasObject != null) {
            focusedCanvasObject.setFocused(false);
        }
        canvasObject.setFocused(true);
        focusedCanvasObject = canvasObject;
    }

    /**
     * No canvasObject should have its knobs drawn.
     */
    public void unfocus() {
        if (focusedCanvasObject != null) {
            focusedCanvasObject.setFocused(false);
        }
        focusedCanvasObject = null;
    }

    /**
     * Which canvasObject has its knobs drawn?
     * 
     * @return The canvasObject which has its knobs drawn
     */
    public CanvasObject getFocusedCanvasObject() {
        return focusedCanvasObject;
    }

    /**
     * All the canvasObjects
     * 
     * @return All the canvasObjects
     */
    public ArrayList<CanvasObject> getCanvasObjects() {
        return canvasObjects;
    }

    /**
     * Take a canvasObject off the canvas, update the sidebar.
     * 
     * @param canvasObject The canvasObject to be deleted.
     */
    public void delete(CanvasObject canvasObject) {
        canvasObjects.remove(canvasObject);
        if (focusedCanvasObject == canvasObject) {
            unfocus();
        }
        gui.refreshCanvasObjectList();
        repaint();
    }

    /**
     * Conglomerate all canvasObjects, including their transformations, into
     * one big drawingObject. For now it's just being added right back, but should
     * be sent to an output engine (svg, gcode, or blot, mostly).
     * 
     * @return The conglomeration of canvasObjects as a drawingObject.
     */
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

        // TODO: change origin HERE
        return output;
    }

    /**
     * Delete every canvasObject!!!
     */
    public void clear() {
        Object[] canvasObjectsArray = canvasObjects.toArray();
        for (Object canvasObject : canvasObjectsArray) {
            delete((CanvasObject) canvasObject);
        }
    }
}
