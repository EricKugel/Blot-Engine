package blot.engine.controller;

import blot.engine.input.imageEngines.ExampleImageEngine;

public class Main {
    public static void main(String[] arg0) {
        ExampleImageEngine engine = new ExampleImageEngine();
        engine.getParameterUi().setParameters();
    }
}
