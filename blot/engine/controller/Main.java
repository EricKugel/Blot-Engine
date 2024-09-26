package blot.engine.controller;

import java.util.HashMap;

import blot.engine.gui.Gui;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.imageEngines.ExampleImageEngine;
import blot.engine.processing.CanvasObject;

public class Main {
    public static void main(String[] arg0) {
        ExampleImageEngine engine = new ExampleImageEngine();
        HashMap<String, Object> parameterValues = engine.getParameterUi().getParameters();
        DrawingObject drawingObject = engine.run(parameterValues);
        CanvasObject canvasObject = new CanvasObject(drawingObject);
        canvasObject.setScaleX(.75);
        canvasObject.setScaleY(.75);
        canvasObject.setRotation(15);

        Gui gui = new Gui();
        gui.getCanvas().add(canvasObject);
    }
}
