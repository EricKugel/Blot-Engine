package blot.engine.input.parameters;

import javax.swing.*;

public class NumberParameter extends Parameter<Double> {
    private JTextField textField = new JTextField();
    private String title = "";
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
            throw new ParameterValidationException(title + " is a required field");
        }

        try {
            double value = Double.parseDouble(stringValue);
            return value;
        } catch(Exception e) {
            throw new ParameterValidationException(title + " must be a decimal value");
        }
    }
}
