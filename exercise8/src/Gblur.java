import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class Gblur extends JFrame implements ActionListener{
    // Instance variables
    private BufferedImage image;   // the image
    private ImageFrame view;       // a component in which to display an image
    private JLabel infoLabel;      // an informative label for the simple GUI
    private JButton BlurButton;    // Button to trigger blur operator
    private JButton resetButton;// Button to restore original image
    private JTextField filterfield[];
    private float customfiltervalues[];
    private int rotation=0;
    private int kernelSize = 3;

    private JRadioButtonMenuItem small;
    private JRadioButtonMenuItem med;
    private JRadioButtonMenuItem large;

    // Constructor
    public Gblur(){
        // JFrame constructor
        super();

        // Build the GUI
        buildGUI();

        // Build the display
    }

    // Build GUI
    private void buildGUI(){
        // Build the menu
        buildMenu();
    }

    // Build menus
    private void buildMenu(){
        // Make the file chooser for user to select a file from the current directory
        final JFileChooser fc = new JFileChooser(".");

        // Build the menu bar and add it to the JFrame
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        // Make menu called file, and items for opening a file and exiting
        JMenu fileMenu = new JMenu ("File");
        JMenuItem fileopen = new JMenuItem ("Open");
        JMenuItem fileexit = new JMenuItem ("Exit");

        // Add an action listener to the file open item
        fileopen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make the open file dialog
                int returnVal = fc.showOpenDialog(Gblur.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    // If allowed, get the selected file
                    File file = fc.getSelectedFile();
                    try {
                        // Read the image
                        image = ImageIO.read(file);
                    } catch (IOException e1){};

                    // Show the image
                    view.setImage(image);
                    view.showImage();
                }
            }
        });
        // Add an action listener to the file menu exit button
        fileexit.addActionListener(
                new ActionListener () {
                    // Close the program on exit
                    public void actionPerformed (ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        // Make a menu called settings, and items for the three kernel sizes
        JMenu settingsMenu = new JMenu("Settings");
        small = new JRadioButtonMenuItem("Radius 1", true);
        med = new JRadioButtonMenuItem("Radius 2");
        large = new JRadioButtonMenuItem("Radius 3");

        // Make a button group for the three radio buttons
        ButtonGroup bg = new ButtonGroup();
        bg.add(small);
        bg.add(med);
        bg.add(large);

        // Add buttons to settings menu
        settingsMenu.add(small);
        settingsMenu.add(med);
        settingsMenu.add(large);

        // Add action listeners
        small.addActionListener(this);
        med.addActionListener(this);
        large.addActionListener(this);

        // Add both menu items to the file menu, then add the file and settings menu to the menubar
        fileMenu.add(fileopen);
        fileMenu.add(fileexit);
        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check which radio button is clicked
        if(e.getSource() == small){
            kernelSize = 3;
        }
        else if(e.getSource() == med){
            kernelSize = 5;
        }
        else if(e.getSource() == large){
            kernelSize = 7;
        }
    }

    // Member function to build the display
    private void buildDisplay(){
        // Build the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(infoLabel);
        controlPanel.add(resetButton);
        controlPanel.add(BlurButton);

        // Add panel to the container
        Container c = getContentPane();
        c.add(view, BorderLayout.NORTH);
        c.add(controlPanel, BorderLayout.SOUTH);
    }

    // Main function
    public static void main(String[] args){
        JFrame frame = new Gblur();
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
}
