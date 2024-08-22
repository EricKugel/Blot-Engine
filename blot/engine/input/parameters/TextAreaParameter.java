package blot.engine.input.parameters;

import javax.swing.*;

/**
 * A parameter for typing large amounts of text, or text with new lines.
 * Also consider uploading txt files using FileParameter.
 */
public class TextAreaParameter extends Parameter<String> {
    private JTextArea textArea;
    private boolean required;

    public TextAreaParameter(String title, int rows, int cols, boolean required) {
        super(title);
        this.required = required;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(title));
        
        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.LINE_AXIS));
        textArea = new JTextArea(rows, cols);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textAreaPanel.add(scrollPane);
        if (required) {
            textAreaPanel.add(new Asterisk());
        }
        add(textAreaPanel);
    }

    public String getValue() throws ParameterValidationException {
        if (required && textArea.getText().equals("")) {
            throw new ParameterValidationException(getTitle() + " is a required field");
        }

        return textArea.getText();
    }
}
