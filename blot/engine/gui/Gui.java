package blot.engine.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.processing.Canvas;
import blot.engine.processing.CanvasObject;

/**
 * Class to hold all the GUI logic.
 */
public class Gui extends JFrame {
    private Canvas canvas = new Canvas(this);
    public static final int CANVAS_SIZE = 600;

    private JPanel sidebar = new JPanel();

    /**
     * Constructs the GUI and its extended JFrame.
     */
    public Gui() {
        setTitle("Blot Engine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        initGUI();
        pack();
    }

    /**
     * Getter method for the Canvas.
     * 
     * @return The Canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Initializes the GUI. Adds the canvas and sidebar.
     */
    private void initGUI() {
        add(canvas, BorderLayout.CENTER);

        add(sidebar, BorderLayout.EAST);
        
        JButton process = new JButton("Process");
        process.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processCanvas();
            }
        });
        add(process, BorderLayout.SOUTH);

        canvas.requestFocus();
    }

    /**
     * Gets the processed drawingObject from the canvas, clears the canvas,
     * and adds the conglomeration back on.
     */
    private void processCanvas() {
        DrawingObject output = canvas.process();
        canvas.clear();
        if (output != null) {
            canvas.add(output);
        }
    }

    /**
     * Updates the sidebar with the current canvasObjects. Automatically called
     * when a canvasObject is added/removed from the canvas.
     */
    public void refreshCanvasObjectList() {
        sidebar.removeAll();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.PAGE_AXIS));
        for (CanvasObject canvasObject : canvas.getCanvasObjects()) {
            SidebarObject sidebarObject = new SidebarObject(canvasObject, canvas);
            sidebar.add(sidebarObject);
        }
        add(sidebar, BorderLayout.EAST);
        sidebar.repaint();
        pack();
    }
}
