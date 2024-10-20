package blot.engine.controller;


import blot.engine.gui.Gui;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Point;
import blot.engine.input.codeEngines.TransformationTelevision;
import blot.engine.processing.CanvasObject;

/**
 * Run this class!.
 */
public class Main {
    /**
     * Main method of the main class.
     * 
     * @param arg0 Command line arguments
     */
    public static void main(String[] arg0) {
        // ExampleImageEngine engine = new ExampleImageEngine();
        // HashMap<String, Object> parameterValues = engine.getParameterUi().getParameters();
        // DrawingObject drawingObject = engine.run(parameterValues);
        // CanvasObject canvasObject = new CanvasObject(drawingObject);
        TransformationTelevision engine = new TransformationTelevision();
        DrawingObject drawingObject = engine.run(null);
        DrawingObject drawingObject1 = engine.run(null);
        CanvasObject canvasObject = new CanvasObject(drawingObject, engine.getName());
        CanvasObject canvasObject1 = new CanvasObject(drawingObject1);
        CanvasObject canvasObject2 = new CanvasObject(drawingObject1.clone(), "The big guy");

        canvasObject.setScaleX(0.5);
        canvasObject.setScaleY(0.5);
        canvasObject.setRotation(15);
        canvasObject1.setScaleY(0.5);
        canvasObject.setPosition(new Point(50, 25));
        canvasObject1.setPosition(new Point(50, 75));

        Gui gui = new Gui();
        gui.getCanvas().add(canvasObject);
        gui.getCanvas().add(canvasObject1);
        gui.getCanvas().add(canvasObject2);
        gui.getCanvas().focus(canvasObject);
        gui.repaint();
    }
}
