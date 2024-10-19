package blot.engine.input;

import java.util.HashMap;

import blot.engine.input.blotLibrary.ConfinedDrawingObject;
import blot.engine.input.blotLibrary.DrawingObject;

/**
 * All plugins/engines MUST implement engine. An engine defines its parameterui,
 * gets the parameters, runs its process, and returns a drawingobject.
 */
public interface Engine {
    public ParameterUi getParameterUi();
    public String getName();
    public ConfinedDrawingObject run(HashMap<String, Object> parameters);
}
