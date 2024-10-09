// Transformation Television, Eric Kugel
// https://github.com/hackclub/blot/blob/main/art/transformationTelevision-Eric/index.js

package blot.engine.input.codeEngines;

import java.util.HashMap;

import blot.engine.input.Engine;
import blot.engine.input.ParameterUi;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Turtle;

public class TransformationTelevision implements Engine {
    @Override
    public ParameterUi getParameterUi() {
        return null;
    }

    @Override
    public String getName() {
        return "Transformation Television";
    }

    @Override
    public DrawingObject run(HashMap<String, Object> parameters) {
        return transformationTelevision();
    }

    private void rect(double left, double bottom, double width, double height, double radius, double bend, Turtle t) {
        t.jump(left, bottom);
        t.setAngle(0);
        for (int i = 0; i < 4; i++) {
            t.forward(i % 2 == 0 ? width : height);
            if (radius != 0) {
                t.arc(90 + bend * (i % 2 == 0 ? -1 : 1), radius);
            } else {
                t.left(90 + bend * (i % 2 == 0 ? -1 : 1));
            }
        }
    }
    
    private DrawingObject transformationTelevision() {
        Turtle t = new Turtle();
        rect(-30, -27, 63, 36, 2, 0, t);
        rect(-27, -24, 39, 31, 1, 0, t);
        rect(-26, -22, 36, 29, 0, 0, t);
        for (int i = 0; i < 28; i++) {
            rect(-26, -21 + i, 36, 0, 0, 0, t);
        }
        for (int i = 0; i < 37; i++) {
            rect(-26 + i, -21, 0, 29, 0, 0, t);
        }
        rect(16, -24, 14, 33, 0, 0, t);
        
        for (int i = 0; i < 7; i++) {
            rect(17.5, -21 + i * 2, 11, -1, 0, 0, t);
        }
        rect(23, -7, 0, 0, 3, 0, t);
        rect(23, 1, 0, 0, 3, 0, t);
        
        rect(4, 13, 0, 40, 1, 28, t);
        rect(2, 13, 0, 36, 1, -37, t);
        rect(-23, -37, 4, 9, 1, 28, t);
        rect(22, -37, 4, 9, 1, -28, t);

        DrawingObject drawingObject = t.getDrawingObject();
        drawingObject.confine(0, 0, 100, 100);

        return drawingObject;
    }
}
