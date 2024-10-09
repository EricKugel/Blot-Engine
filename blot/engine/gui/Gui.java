package blot.engine.gui;

import java.awt.*;

import javax.swing.*;

import blot.engine.processing.Canvas;

public class Gui extends JFrame {
    private Canvas canvas = new Canvas(this);

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
        JPanel main = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                canvas.draw(g);
            };
        };
        main.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
        setContentPane(main);
    }
}
