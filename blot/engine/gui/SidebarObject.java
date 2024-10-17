package blot.engine.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import blot.engine.processing.CanvasObject;
import blot.engine.processing.Canvas;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SidebarObject extends JPanel {
    private CanvasObject canvasObject;
    private Canvas canvas;
    
    private static Image deleteImage = null;

    public SidebarObject(CanvasObject canvasObject, Canvas canvas) {
        this.canvasObject = canvasObject;
        this.canvas = canvas;

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JButton focus = new JButton(canvasObject.getName());
        focus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.focus(canvasObject);
                canvas.repaint();
            }
        });

        if (deleteImage == null) {
            try {
                deleteImage = ImageIO.read(SidebarObject.class.getResource("delete.png"));
                deleteImage = deleteImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ImageIcon deleteIcon = new ImageIcon(deleteImage);
        JButton delete = new JButton(deleteIcon);
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                canvas.delete(canvasObject);
            }
        });

        add(focus);
        add(delete);
    }
}
