package blot.engine.input.parameters;

import javax.swing.*;
import java.util.Hashtable;

public class SliderParameter extends Parameter<Double> {
    private JSlider slider = null;
    private static final int SCALAR = 100;

    public SliderParameter(String title, double min, double max, double majorTickSpacing, boolean required) {
        super(title);
        add(new JLabel(title + ":"));

        slider = new JSlider((int) (min * SCALAR), (int) (max * SCALAR));
        slider.setMajorTickSpacing((int) (majorTickSpacing * SCALAR));
        Hashtable<Integer, String> labelTable = new Hashtable<Integer, String>();
        int ticks = (int) ((max - min) / majorTickSpacing);
        for (int i = 0; i < ticks; i++) {
            double value = min + i * majorTickSpacing;
            value = Math.round(value * 100) / 100;
            labelTable.put((int) (value * SCALAR), "" + value);
        }
        slider.setLabelTable(labelTable);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        add(slider);
    }

    public Double getValue() {
        return (slider.getValue() / (double) SCALAR);
    }
}
