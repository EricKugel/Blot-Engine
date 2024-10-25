package blot.engine.input.parameters;

import javax.swing.*;

/**
 * A parameter for typing less text.
 */
public class TextParameter extends Parameter<String> {
    private JTextField textField;
    private boolean required = false;

    public TextParameter(String title, int cols, boolean required) {
        super(title);
        this.required = required;
        add(new JLabel(title + ":"));

        textField = new JTextField(cols);
        add(textField);

        if (required) {
            add(new Asterisk());
        }
    }

    public String getValue() throws ParameterValidationException {
        if (required && textField.getText().isEmpty()) {
            throw new ParameterValidationException(getTitle() + " is a required field");
        }
        return textField.getText();
    }
}
