import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import javax.imageio.*;

public class Gblur extends JFrame implements ActionListener{
    // Instance variables
    private BufferedImage image;   // the image
    private ImageFrame view;       // a component in which to display an image
    private JLabel infoLabel;      // an informative label for the simple GUI
    private JButton BlurButton;    // Button to trigger blur operator
    private JButton resetButton; // Button to restore original image
    private int kernelSize = 1;

    private JRadioButtonMenuItem small;
    private JRadioButtonMenuItem med;
    private JRadioButtonMenuItem large;

    // Constructor
    public Gblur(){
        // JFrame constructor
        super();

        // Build the GUI
        buildGUI();
    }

    // Build GUI
    private void buildGUI(){
        // Build the menu
        buildMenu();

        // Build the components
        buildComponents();

        // Build the display
        buildDisplay();
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
            kernelSize = 1;
        }
        else if(e.getSource() == med){
            kernelSize = 2;
        }
        else if(e.getSource() == large){
            kernelSize = 3;
        }
        // Set the kernel size in the ImageFrame
        view.setKernelSize(kernelSize);
    }

    private void buildComponents() {
        // UI Components
        // Uncomment for no default image
        //view = new ImageFrame();
        view = new ImageFrame(readImage("joe-exotic.jpg"));
        infoLabel = new JLabel("Original Image");
        resetButton = new JButton("Original");
        BlurButton = new JButton("Blur");

        // Action listeners for each button
        // Button listeners activate the buffered image object in order
        // to display appropriate function
        resetButton.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.showImage();
                        infoLabel.setText("Original");
                    }
                }
        );
        BlurButton.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        view.BlurImage();
                        infoLabel.setText("Blur");
                    }
                }
        );
    }

    // Member function to build the display
    private void buildDisplay(){
        // Build the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(infoLabel);
        controlPanel.add(resetButton);
        controlPanel.add(BlurButton);

        // Add panel to the container
        Container c = this.getContentPane();
        c.add(view, BorderLayout.CENTER);
        c.add(controlPanel, BorderLayout.SOUTH);
    }

    // Function to normalize a given double nxn matrix, prints out the normalized matrix as a matrix of floats
    // For kernel creation
    private static void normalize(double[] arr){
        // Initialize variables
        double sum = 0;
        float[] newarr = new float[arr.length];

        // Sum array
        for(int i =0; i < arr.length; i++){
            sum += arr[i];
        }
        // Normalize matrix
        sum = 1/sum;
        for(int i = 0; i < arr.length; i++){
            newarr[i] = (float) (arr[i] * sum);
        }

        // Print out normalized array
        System.out.print(Arrays.toString(newarr));

        // Double check the sum of the normalized array
        double newsum = 0;
        for(int i = 0; i < newarr.length; i++){
            newsum += newarr[i];
        }
        System.out.print("\n" + newsum + "\n");
    }

    // This method reads an Image object from a file indicated by
    // the string provided as the parameter.  The image is converted
    // here to a BufferedImage object, and that new object is the returned
    // value of this method.
    // The mediatracker in this method can throw an exception

    public BufferedImage readImage (String file) {

        Image image = Toolkit.getDefaultToolkit().getImage(file);
        MediaTracker tracker = new MediaTracker (new Component () {});
        tracker.addImage(image, 0);
        try { tracker.waitForID (0); }
        catch (InterruptedException e) {}
        BufferedImage bim = new BufferedImage
                (image.getWidth(this), image.getHeight(this),
                        BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage (image, 0, 0, this);
        return bim;
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

        /*
        // 3x3, 5x5, and 7x7 matrix from the gaussian kernel calculator provided by Dr. Seales
        double[] arr1 = {31.313345797148152, 35.39115471252564, 31.313345797148152, 35.39115471252564, 40, 35.39115471252564, 31.313345797148152, 35.39115471252564, 31.313345797148152};
        double[] arr2 = {15.021707775220133, 21.688244051309848, 24.51261534412037, 21.688244051309848, 15.021707775220133, 21.688244051309848, 31.313345797148152, 35.39115471252565, 31.313345797148152, 21.688244051309848,
                24.51261534412037, 35.39115471252565, 40, 35.39115471252565, 24.51261534412037, 21.688244051309848, 31.313345797148152, 35.39115471252565, 31.313345797148152, 21.688244051309848,
                15.021707775220133, 21.688244051309848, 24.51261534412037, 21.688244051309848, 15.021707775220133};
        double[] arr3 = {4.4156577513526845, 8.144367401843168, 11.758784719942968, 13.290083146997514, 11.758784719942968, 8.144367401843168, 4.4156577513526845, 8.144367401843168, 15.021707775220126, 21.68824405130984, 24.512615344120363, 21.68824405130984, 15.021707775220126, 8.144367401843168,
                11.758784719942968, 21.68824405130984, 31.31334579714814, 35.39115471252564, 31.31334579714814, 21.68824405130984, 11.758784719942968, 13.290083146997514, 24.512615344120363, 35.39115471252564, 40, 35.39115471252564, 24.512615344120363, 13.290083146997514,
                11.758784719942968, 21.68824405130984, 31.31334579714814, 35.39115471252564, 31.31334579714814, 21.68824405130984, 11.758784719942968, 8.144367401843168, 15.021707775220126, 21.68824405130984, 24.512615344120363, 21.68824405130984, 15.021707775220126, 8.144367401843168,
                4.4156577513526845, 8.144367401843168, 11.758784719942968, 13.290083146997514, 11.758784719942968, 8.144367401843168, 4.4156577513526845};
        normalize(arr1);
        normalize(arr2);
        normalize(arr3);
        */
    }
}
