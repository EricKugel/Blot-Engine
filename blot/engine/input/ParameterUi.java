package blot.engine.input;

import blot.engine.input.parameters.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * An easy way to give engines their arguments. Uses Parameters.
 * 
 * I'm actually really proud of this...
 * I'm also not sure if I'm using generics correctly
 */
public class ParameterUi extends JFrame implements ActionListener {
    private Engine engine = null;
    private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
    private HashMap<String, Object> parameterValues = new HashMap<String, Object>();

    public ParameterUi(Engine engine) {
        this.engine = engine;
        setTitle(engine.getName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
    }

    public void addParameter(Parameter parameter) {
        add(parameter);
        parameters.add(parameter);
    }

    public synchronized HashMap<String, Object> getParameters() {
        JButton submitButton = new JButton("Run " + engine.getName());
        submitButton.addActionListener(this);
        add(submitButton);
        setVisible(true);
        pack();

        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return parameterValues;
    }

    private HashMap<String, Object> submit() {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        for (Parameter parameter : parameters) {
            try {
                dataMap.put(parameter.getTitle(), parameter.getValue());
            } catch (ParameterValidationException e) {
                String message = e.getMessage();
                JOptionPane.showMessageDialog(this, message);
                return null;
            }
        }
        dispose();
        return dataMap;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        HashMap<String, Object> values = submit();
        if (values != null) {
            parameterValues = values;
            notifyAll();
        }
    }
}
