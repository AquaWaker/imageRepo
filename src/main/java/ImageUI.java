import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.io.File;
import javax.imageio.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;


public class ImageUI extends JFrame implements Serializable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 450;
    // Screen buffer dimensions are different than terminal dimensions
    public static final int COLS = 80;
    public static final int ROWS = 24;
    private final char startCol = 0;
    private final char msgRow = 1;
    private final char roomRow = 3;
    private Container contentPane;
    private JPanel labelNamePanel;
    private JTextArea messageLabel;
    private JTextArea imageField;
    private JMenu fileMenu;
    private JFrame frame;

    private transient HashMap<String, BufferedImage> images;

    /**
     * Constructor.
     */
    public ImageUI() {
        super("my awesome repository");
        images = new HashMap<String, BufferedImage>();
        contentPane = getContentPane();
        setWindowDefaults();
        setUpPanels();
        pack();
        setSize(WIDTH, HEIGHT);
        frame = new JFrame("Error Message");
        loadImages();
    }

    private void setWindowDefaults() {
        setTitle("Image Repository");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane.setLayout(new BorderLayout());

    }

    private void setUpPanels() {
        labelNamePanel = new JPanel(new GridLayout(1, 2));
        JPanel labelPanel = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel imagePanel = new JPanel();
        setUpMessageLabelPanel(labelPanel);
        setUpImagePanel(imagePanel);
        contentPane.add(labelNamePanel, BorderLayout.NORTH);
        makeMenuBar();
    }

    private void setUpImagePanel(JPanel thePanel) {
        Border prettyLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        thePanel.setBorder(prettyLine);
        imageField = new JTextArea("Images: \n");
        imageField.setEditable(false);
        thePanel.add(imageField);
        contentPane.add(thePanel, BorderLayout.CENTER);
    }

    private void setUpMessageLabelPanel(JPanel thePanel) {
        Border prettyLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        thePanel.setBorder(prettyLine);
        thePanel.setBackground(Color.CYAN);
        messageLabel = new JTextArea("Select the Add option under File to add images to be saved \n Then select Save to save your current repository");
        messageLabel.setBackground(Color.CYAN);
        messageLabel.setEditable(false);
        thePanel.add(messageLabel);
        labelNamePanel.add(thePanel);

    }

    // Make menu bar and the buttons
    private void makeMenuBar() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        JMenuItem loadItem = new JMenuItem("Add");
        loadItem.addActionListener(ev -> addImage());
        fileMenu.add(loadItem);

        JMenuItem searchItem = new JMenuItem("Save");
        searchItem.addActionListener(ev -> saveImages());
        fileMenu.add(searchItem);
    }

    // Loads images that are already in the repository
    private void loadImages() {

        File folder = new File(System.getProperty("user.dir") + "\\images\\");
        File[] listOfImages = folder.listFiles();
        String message = "Couldn't Old Images, please try again";

        if (!folder.exists()) {
            folder.mkdir();
        } else {
            try {
                for (File file : listOfImages) {
                    if (file.isFile()) {
                        BufferedImage img = ImageIO.read(file);
                        images.put(fileComponent(file.getName()), img);

                        imageField.setText(imageField.getText() + fileComponent(file.getName()) + "\n");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, message, "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }




    }

    // Add an image to repository
    private void addImage() {
        JFileChooser j = new JFileChooser();
        int result = j.showOpenDialog(null);
        String message = "Couldn't load, please try again";
        BufferedImage img = null;

        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = (j.getSelectedFile().toString());
            try {
                img = ImageIO.read(new File(filename));
                images.put(fileComponent(filename), img);

                imageField.setText(imageField.getText() + fileComponent(filename) + "\n");

            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(frame, message, "Error Message", JOptionPane.ERROR_MESSAGE);
            } 
        } else {
            JOptionPane.showMessageDialog(frame, message, "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Removes the path from the file name
     * Credit to:
     * https://www.tutorialspoint.com/java-program-to-remove-path-information-from-a-filename-returning-only-its-file-component#:~:text=The%20method%20fileCompinent()%20is,only%20of%20the%20file%20name.
     * @param fname the full file path
     */
    public static String fileComponent(String fname) {
        int pos = fname.lastIndexOf(File.separator);
        if(pos > -1)
            return fname.substring(pos + 1);
        else
            return fname;
    }

    // Searches an image
    // TODO
    private void saveImages() {

        String message = "Couldn't save, please try again";

        try {

            for(Map.Entry<String, BufferedImage> entry : images.entrySet()) {
                String fname = entry.getKey();
                BufferedImage image = entry.getValue();

                File file = new File (System.getProperty("user.dir") + "\\images\\" + fname);
                ImageIO.write(image, "png", file);

            }

        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(frame, message, "Error Message", JOptionPane.ERROR_MESSAGE);
        }
    }


}
