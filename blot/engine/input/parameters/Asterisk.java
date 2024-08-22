package blot.engine.input.parameters;

import javax.swing.*;
import java.awt.Color;

/**
 * Shortcut to create a red asterisk in a JLabel.
 */
public class Asterisk extends JLabel {
    public Asterisk() {
        setText("*");
        setForeground(Color.RED);
    }
}
