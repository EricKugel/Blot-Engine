package blot.engine.input.imageEngines;

import java.util.HashMap;

import blot.engine.input.Engine;
import blot.engine.input.ParameterUi;
import blot.engine.input.parameters.*;
import blot.engine.input.blotLibrary.DrawingObject;

public class ExampleImageEngine implements Engine {
    private String engineName = "Example Image Engine";

    public ParameterUi getParameterUi() {
        ParameterUi parameterUi = new ParameterUi(this);
        parameterUi.addParameter(new NumberParameter("Favorite number", true));
        parameterUi.addParameter(new FileParameter("Favorite picture", FileParameter.IMAGE, false));
        return parameterUi;
    }

    public String getName() {
        return engineName;
    }

    public DrawingObject run(HashMap<String, Object> parameters) {
        for (String key : parameters.keySet()) {
            System.out.println(key + "...");
            System.out.println("\t" + parameters.get(key));
        }
        System.out.println("Thank you.");
        return new DrawingObject();
    }
}
