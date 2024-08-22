package blot.engine.input;

import java.util.HashMap;

import blot.engine.input.blotLibrary.DrawingObject;

// All engines should implement Engine.
public interface Engine {
    public ParameterUi getParameterUi();
    public String getName();
    public DrawingObject run(HashMap<String, Object> parameters);
}
