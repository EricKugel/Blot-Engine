package blot.engine.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import blot.engine.processing.CanvasObject;
import blot.engine.processing.Canvas;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * An extension of JPanel that allows for control over a single canvasObject.
 * Contains buttons to focus and delete canvasObjects. Intended to be added
 * to a sideBar.
 */
public class SidebarObject extends JPanel {
    /**
     * Who am I kidding, optimizing the way this one icon is loaded when the
     * rest of my code is like it is...
     */
    private static Image deleteImage = null;

    /**
     * Constructs the SidebarObject, adds the actionListeners to the two buttons.
     * 
     * @param canvasObject The canvasObject to be given control over
     * @param canvas The Canvas instance, to be able to send commands back up
     */
    public SidebarObject(CanvasObject canvasObject, Canvas canvas) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JButton focus = new JButton(canvasObject.getName());
        focus.addActionListener(new ActionListener() {
            /**
             * Look at me! Focus on me! Looooooooooove me
             */
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
            /**
             * Delete me 
             */
            public void actionPerformed(ActionEvent e) {
                canvas.delete(canvasObject);
            }
        });

        add(focus);
        add(delete);
    }
}
