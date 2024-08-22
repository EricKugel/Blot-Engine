package blot.engine.input.parameters;

import javax.swing.*;

/**
 * Base class for Parameters. A parameter extends JPanel, meaning it can be
 * added to a ParameterUi.
 */
public abstract class Parameter<T> extends JPanel {
    public abstract T getValue() throws ParameterValidationException;
    private String title;
    
    public Parameter() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    }

    public Parameter(String title) {
        this();
        setTitle(title);
    }
    
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
