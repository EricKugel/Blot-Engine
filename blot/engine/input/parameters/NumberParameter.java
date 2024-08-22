package blot.engine.input.parameters;

import javax.swing.*;

/**
 * Parameter for typing in decimals. If a range is needed, use SliderParameter instead.
 */
public class NumberParameter extends Parameter<Double> {
    private JTextField textField = new JTextField();
    private boolean required = false;

    public NumberParameter(String title, boolean required) {
        super(title);
        this.required = required;

        add(new JLabel(title + ":"));
        add(textField);
        if (required) {
            add(new Asterisk());
        }
    }

    public Double getValue() throws ParameterValidationException {
        String stringValue = textField.getText();
        if (required && stringValue.equals("")) {
            throw new ParameterValidationException(getTitle() + " is a required field");
        }

        try {
            double value = Double.parseDouble(stringValue);
            return value;
        } catch(Exception e) {
            throw new ParameterValidationException(getTitle() + " must be a decimal value");
        }
    }
}
