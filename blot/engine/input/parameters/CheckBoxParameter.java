package blot.engine.input.parameters;

import javax.swing.*;

/**
 * Parameter for yes-no questions.
 */
public class CheckBoxParameter extends Parameter<Boolean> {
    private JCheckBox checkBox = null;

    public CheckBoxParameter(String title, boolean required) {
        super(title);

        checkBox = new JCheckBox(title);
        add(checkBox);

        // In this case required doesn't do anything...
    }

    public Boolean getValue() {
        return checkBox.isSelected();
    }
}