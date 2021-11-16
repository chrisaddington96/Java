import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Jmorph extends JFrame implements ActionListener {
    // Class vairables
    private MeshCanvas mesh;

    private JPanel controlPanel;
    private JButton clearButton;
    private JButton warpButton;

    private JPanel positionPanel;
    private JLabel xposLabel;
    private JLabel yposLabel;

    // Constructor
    public Jmorph(){
        super("Keyframe creator");

        // Set the size of the Jmorph
        setSize(getPreferredSize());

        // Create container and set colors
        Container c =getContentPane();
        c.setBackground(new Color(232, 232, 232));
        c.setForeground(new Color(0, 0, 0));
        c.setLayout(new BorderLayout());

        // Canvas
        mesh = new MeshCanvas();
        //mesh.setImage("test.jpg");
        c.add(mesh, BorderLayout.CENTER);

        // Add control buttons
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        warpButton = new JButton("Warp");
        warpButton.addActionListener(this);

        // Add control panel
        controlPanel = new JPanel(new FlowLayout());
        controlPanel.setForeground(new Color(0, 0, 0));
        controlPanel.add(clearButton);
        controlPanel.add(warpButton);
        c.add(controlPanel, BorderLayout.SOUTH);

        // Make position panel
        positionPanel = new JPanel(new FlowLayout());

        // Add x position label
        xposLabel = new JLabel("X: ");		// xposL used temporarily
        xposLabel.setForeground(new Color(0, 0, 0));
        positionPanel.add(xposLabel);
        xposLabel = new JLabel("000");		// real xposL
        xposLabel.setForeground(new Color(0, 0, 0));
        positionPanel.add(xposLabel);

        // Add y position label
        yposLabel = new JLabel("  Y: ");	// yposL used temporarily
        yposLabel.setForeground(new Color(0, 0, 0));
        positionPanel.add(yposLabel);
        yposLabel = new JLabel("000");		// real yposL
        yposLabel.setForeground(new Color(0, 0, 0));
        positionPanel.add(yposLabel);

        // Add the position panel to the container
        c.add(positionPanel, BorderLayout.NORTH);

        // keep track of mouse position
        mesh.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent mevt) {
                xposLabel.setText(Integer.toString(mevt.getX()));
                yposLabel.setText(Integer.toString(mevt.getY()));
            }
            public void mouseDragged(MouseEvent mevt) {
                xposLabel.setText(Integer.toString(mevt.getX()));
                yposLabel.setText(Integer.toString(mevt.getY()));
            }
        } );

        // allow use of "X" button to exit
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                setVisible(false);
                System.exit(0);
            }
        } );

    }


    // Capture button actions
    public void actionPerformed(ActionEvent e){
        Object src = e.getSource();

        // reset the original image and show the mesh
        if(src == clearButton) {
            mesh.clear();
        }
        if(src == warpButton) {
            mesh.makeWarp();
        }
    }

    // main: creates new instance of Mesh object
    public static void main(String args[]) {
        Jmorph pl;

        pl = new Jmorph();
        pl.setSize(pl.getPreferredSize().width, pl.getPreferredSize().height);
        pl.setVisible(true);
    } // main()
}
