package blot.engine.input.imageEngines;

import java.util.HashMap;

import blot.engine.input.Engine;
import blot.engine.input.ParameterUi;
import blot.engine.input.parameters.*;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Turtle;

public class ExampleImageEngine implements Engine {
    private String engineName = "Example Image Engine";

    public ParameterUi getParameterUi() {
        ParameterUi parameterUi = new ParameterUi(this);
        parameterUi.addParameter(new NumberParameter("Favorite number", true));
        parameterUi.addParameter(new FileParameter("Favorite picture", FileParameter.IMAGE, false));
        parameterUi.addParameter(new SliderParameter("Sigma skibidi rizz levels", 0, 10, 1, true));
        parameterUi.addParameter(new CheckBoxParameter("Include error messages", true));
        parameterUi.addParameter(new DropdownParameter("Best fruit", new String[] {"Banana", "Apple", "Grape", "no"}, true));
        parameterUi.addParameter(new TextParameter("Your name", 15, true));
        parameterUi.addParameter(new TextAreaParameter("Your story", 4, 15, true));
        return parameterUi;
    }

    public String getName() {
        return engineName;
    }

    public DrawingObject run(HashMap<String, Object> parameters) {
        // WARNING: A set, not a list. May return in an order different than the parameters
        // were added to the ParameterUi.
        for (String key : parameters.keySet()) {
            System.out.println(key + "...");
            System.out.println("\t" + parameters.get(key));
        }
        System.out.println("Thank you.");
        
        Turtle turtle = new Turtle();
        for (int i = 0; i < 4; i++) {
            turtle.forward(100);
            turtle.left(90);
        }


        return turtle.getDrawingObject();
    }
}
