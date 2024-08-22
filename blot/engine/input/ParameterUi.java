package blot.engine.input;

import blot.engine.input.parameters.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.HashMap;
import java.util.ArrayList;

public class ParameterUi extends JFrame {
    private Engine engine = null;
    private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

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

    public void setParameters() {
        JButton submitButton = new JButton("Run " + engine.getName());
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });
        add(submitButton);
        setVisible(true);
        pack();
    }

    private void submit() {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        for (Parameter parameter : parameters) {
            try {
                dataMap.put(parameter.getTitle(), parameter.getValue());
            } catch (ParameterValidationException e) {
                String message = e.getMessage();
                JOptionPane.showMessageDialog(this, message);
                return;
            }
        }
        this.dispose();
        engine.run(dataMap);
    }
}
