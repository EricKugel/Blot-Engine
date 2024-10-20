package blot.engine.input;

import java.util.HashMap;

import blot.engine.input.blotLibrary.ConfinedDrawingObject;

/**
 * All plugins/engines MUST implement engine. An engine defines its parameterui,
 * gets the parameters, runs its process, and returns a drawingobject.
 */
public interface Engine {
    public ParameterUi getParameterUi();
    public String getName();
    /**
     * Remember to confine the drawingObject to the canvas after creation.
     * 
     * @param parameters
     * @return
     */
    public ConfinedDrawingObject run(HashMap<String, Object> parameters);
}
