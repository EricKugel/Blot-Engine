package blot.engine.input.parameters;

import javax.swing.*;

public class DropdownParameter extends Parameter<String> {
    private JComboBox<String> comboBox = null;
    
    public DropdownParameter(String title, String[] choices, boolean required) {
        super(title);
        add(new JLabel(title + ":"));

        comboBox = new JComboBox<String>(choices);
        add(comboBox);

        // required doesn't do anything here either...
    }

    public String getValue() {
        return (String) comboBox.getSelectedItem();
    }
}
