package blot.engine.input.parameters;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;

import java.io.File;

import javax.imageio.ImageIO;

public class FileParameter extends Parameter<File> {
    public static final int IMAGE = 0;
    public static final int TEXT = 1;
    public static final int OTHER = 2;

    private JFileChooser fileChooser = null;
    private File selectedFile = null;
    private boolean required = false;
    private String title = "";

    public FileParameter(String title, int fileType, boolean required) {
        super(title);

        fileChooser = new JFileChooser();
        if (fileType == IMAGE) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        } else if (fileType == TEXT) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        } else if (fileType == OTHER) {
            System.out.println("WARNING: Please implement the desired file type in blot.engine.input.parameters.FileParameter");
        } else {
            System.err.println("File type must be specified in FileParameter");
            System.exit(1);
        }

        JButton chooseFileButton = new JButton("Pick File");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                }
            }
        });
        
        add(new JLabel(title + ":"));
        add(chooseFileButton);
        if (required) {
            add(new Asterisk());
        }

        this.required = required;
    }

    public File getValue() throws ParameterValidationException {
        if (required && selectedFile == null) {
            throw new ParameterValidationException(title + " is a required field");
        }
        return fileChooser.getSelectedFile();
    }
}
