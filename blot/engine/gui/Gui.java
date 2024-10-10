package blot.engine.gui;

import java.awt.*;

import javax.swing.*;

import blot.engine.processing.Canvas;

public class Gui extends JFrame {
    private Canvas canvas = new Canvas();
    public static final int CANVAS_SIZE = 600;

    public Gui() {
        setTitle("Blot Engine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        initGUI();
        pack();
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    private void initGUI() {
        setContentPane(canvas);
    }
}
