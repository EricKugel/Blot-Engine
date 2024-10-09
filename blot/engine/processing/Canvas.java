package blot.engine.processing;

import java.awt.Graphics;
import java.util.ArrayList;

import blot.engine.gui.Gui;
import blot.engine.input.blotLibrary.DrawingObject;

public class Canvas extends ArrayList<CanvasObject> {
    public static final double WIDTH = 100;
    public static final double HEIGHT = 100;

    private Gui gui;

    public Canvas(Gui gui) {
        this.gui = gui;
    }

    public void draw(Graphics g) {
        for (CanvasObject canvasObject : this) {
            canvasObject.draw(g);
        }
    }

    public void add(DrawingObject drawingObject) {
        this.add(new CanvasObject(drawingObject));
        this.gui.repaint();
    }

    @Override
    public boolean add(CanvasObject canvasObject) {
        super.add(canvasObject);
        this.gui.repaint();
        return true;
    }
}
