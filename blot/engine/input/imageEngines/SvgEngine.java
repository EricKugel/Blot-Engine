package blot.engine.input.imageEngines;

import java.awt.Canvas;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

import blot.engine.input.Engine;
import blot.engine.input.ParameterUi;
import blot.engine.input.blotLibrary.ConfinedDrawingObject;
import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.input.blotLibrary.Point;
import blot.engine.input.blotLibrary.Turtle;
import blot.engine.input.parameters.FileParameter;
import blot.engine.input.parameters.SliderParameter;

public class SvgEngine implements Engine {
    private String name = "SVG Engine Output";
    private File file;
    private int resolution = 64;

    private Point[] cubicSmoothReference = null;
    private Point[] quadraticSmoothReference = null;

    public ParameterUi getParameterUi() {
        ParameterUi parameterUi = new ParameterUi(this);
        parameterUi.addParameter(new FileParameter("SVG File", FileParameter.OTHER, true));
        parameterUi.addParameter(new SliderParameter("Curve Resolution", 1, 1000, 100, false));
        return parameterUi;
    }

    public String getName() {
        return name;
    }

    public ConfinedDrawingObject parseSvg() {
        Turtle t = new Turtle();
        t.setResolution(resolution);
        
        DrawingObject drawingObject = t.getDrawingObject();
        drawingObject.confine(0, 0, Canvas.WIDTH, Canvas.HEIGHT);
        return new ConfinedDrawingObject(drawingObject);
    }

    private boolean isStringDigit(String string) {
        if (string.isEmpty()) return false;
        for (char c : string.toCharArray()) {
            if (!"-0123456789.".contains("" + c)) {
                return false;
            }
        }
        return true;
    }

    public void path(Turtle t, String path) {
        t.down();

        String command = "";
        ArrayList<Double> buffer = new ArrayList<Double>();
        path = path.replace("\n", "");
        path = path.replace(",", " ");
        while(path.contains("  ")) {
            path = path.replace("  ", " ");
        }

        for (String token : path.split(" ")) {
            while (("MmLlHhVvCcSsQqTtAaZz").contains("" + token.charAt(0))) {
                executeAndFlushBuffer(t, command, buffer);

                command = "" + token.charAt(0);
                token = token.substring(1);
            }

            if (isStringDigit(token)) {
                buffer.add(Double.parseDouble(token));
            }
        }
        executeAndFlushBuffer(t, command, buffer);
    }

    /**
     * Executes command command on t with data buffer, clears the buffer.
     * Specifically for path modules.
     * 
     * @param t The turtle to execute on
     * @param command The command to run
     * @param buffer The data to run the command with
     */
    private void executeAndFlushBuffer(Turtle t, String command, ArrayList<Double> buffer) {
        if ("MmLl".contains(command)) {
            for (int i = 0; i < buffer.size(); i += 2) {
                Point point = new Point(buffer.get(i), buffer.get(i + 1));
                Point addend = "ml".contains(command) ? t.getPosition() : Point.ZERO;
                point = Point.add(point, addend);
                if ("Mm".contains(command)) {
                    t.jump(point.getX(), point.getY());
                } else {
                    t.goTo(point.getX(), point.getY());
                }
            }
        } else if ("HhVv".contains(command)) {
            ArrayList<Double> newBuffer = new ArrayList<Double>();
            for (int i = 0; i < buffer.size(); i++) {
                if ("Hh".contains(command)) {
                    newBuffer.add(buffer.get(i));
                    newBuffer.add(command.equals("H") ? t.getPosition().getY() : 0);
                } else {
                    newBuffer.add(command.equals("V") ? t.getPosition().getX() : 0);
                    newBuffer.add(buffer.get(i));
                }
            }
            buffer = newBuffer;

            executeAndFlushBuffer(t, "HV".contains(command) ? "L" : "l", buffer);
        } else if ("CcSs".contains(command)) {
            double[] b = new double[6];
            if ("Cc".contains(command)) {
                for (int i = 0; i < buffer.size() / 6; i++) {
                    for (int j = 0; j < 6; j++) {
                        b[j] = buffer.get(i * 6 + j);
                    }
                }
            } else if ("Ss".contains(command)) {
                for (int i = 0; i < buffer.size() / 4; i++) {
                    Point reflection = t.getPosition();
                    if (cubicSmoothReference != null) {
                        reflection = Point.add(cubicSmoothReference[0], Point.mult(Point.sub(cubicSmoothReference[1], cubicSmoothReference[0]), 2));
                    }
                    b[0] = reflection.getX();
                    b[1] = reflection.getY();
                    for (int j = 0; j < 4; j++) {
                        b[2 + j] = buffer.get(i * 4  + j);
                    }
                }
            }
            
            cubicSmoothReference = new Point[] {new Point(b[2], b[3]), new Point(b[4], b[5])};
            if ("SC".contains(command)) {
                t.cubicBezier(b);
            } else {
                cubicSmoothReference[0] = Point.add(t.getPosition(), cubicSmoothReference[0]);
                cubicSmoothReference[1] = Point.add(t.getPosition(), cubicSmoothReference[1]);
                t.relativeCubicBezier(b);
            }
        } else if ("QqTt".contains(command)) {
            double[] b = new double[4];
            if ("Qq".contains(command)) {
                for (int i = 0; i < buffer.size() / 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        b[j] = buffer.get(i * 4 + j);
                    }
                }
            } else if ("Tt".contains(command)) {
                for (int i = 0; i < buffer.size() / 2; i++) {
                    Point reflection = t.getPosition();
                    if (quadraticSmoothReference != null) {
                        reflection = Point.add(quadraticSmoothReference[0], Point.mult(Point.sub(quadraticSmoothReference[1], quadraticSmoothReference[0]), 2));
                    }
                    b[0] = reflection.getX();
                    b[1] = reflection.getY();
                    for (int j = 0; j < 2; j++) {
                        b[2 + j] = buffer.get(i * 2  + j);
                    }
                }
            }
            
            quadraticSmoothReference = new Point[] {new Point(b[0], b[1]), new Point(b[2], b[3])};
            if ("QT".contains(command)) {
                t.quadraticBezier(b);
            } else {
                quadraticSmoothReference[0] = Point.add(t.getPosition(), quadraticSmoothReference[0]);
                quadraticSmoothReference[1] = Point.add(t.getPosition(), quadraticSmoothReference[1]);
                t.relativeQuadraticBezier(b);
            }
        } else if ("Aa".contains(command)) {
            for (int i = 0; i < buffer.size() / 7; i++) {
                double[] b = new double[7];
                for (int j = 0; j < 7; j++) {
                    b[j] = buffer.get(i * 7 + j);
                }
                t.svgArc(b[0], b[1], b[2], b[3], b[4], b[5], b[6]);
            }
        } else if ("Zz".contains(command)) {
            t.closePath();
        }

        if (!"CcSS".contains(command)) {
            cubicSmoothReference = null;
        } if (!"QqTt".contains(command)) {
            quadraticSmoothReference = null;
        }

        buffer.clear();
    }

    public ConfinedDrawingObject run(HashMap<String, Object> parameters) {
        for (String key : parameters.keySet()) {
            if (key.equals("SVG File")) {
                file = (File) parameters.get(key);
                name = file.getName().split(".")[0];
            } else if (key.equals("Curve Resolution")) {
                resolution = (int) parameters.get(key);
            } else {
                System.out.println("UNEXPECTED PARAMETER " + key);
            }
        }

        return parseSvg();
    }
}
