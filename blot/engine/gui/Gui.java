package blot.engine.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import blot.engine.input.blotLibrary.DrawingObject;
import blot.engine.processing.Canvas;
import blot.engine.processing.CanvasObject;

public class Gui extends JFrame {
    private Canvas canvas = new Canvas(this);
    public static final int CANVAS_SIZE = 600;

    private JPanel sidebar = new JPanel();

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

    private void processCanvas() {
        DrawingObject output = canvas.process();

        

        canvas.clear();
        if (output != null) {
            canvas.add(output);
        }
    }

    public void refreshCanvasObjectList() {
        sidebar.removeAll();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.PAGE_AXIS));
        for (CanvasObject canvasObject : this.canvas.getCanvasObjects()) {
            SidebarObject sidebarObject = new SidebarObject(canvasObject, canvas);
            sidebar.add(sidebarObject);
        }
        add(sidebar, BorderLayout.EAST);
        sidebar.repaint();
        pack();
    }
}
