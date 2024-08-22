package blot.engine.input.parameters;

import javax.swing.*;

public class TextAreaParameter extends Parameter<String> {
    private JTextArea textArea;
    private String title;
    private boolean required;

    public TextAreaParameter(String title, int rows, int cols, boolean required) {
        super(title);
        this.required = required;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(title));
        
        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.LINE_AXIS));
        textArea = new JTextArea(rows, cols);
        textAreaPanel.add(textArea);
        if (required) {
            textAreaPanel.add(new Asterisk());
        }
        add(textAreaPanel);
    }

    public String getValue() throws ParameterValidationException {
        if (required && textArea.getText().equals("")) {
            throw new ParameterValidationException(title + " is a required field");
        }

        return textArea.getText();
    }
}
