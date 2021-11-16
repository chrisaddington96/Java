import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Mesh extends JFrame implements ActionListener {


private MeshCanvas mymesh;

private JPanel controlP;
private JButton clearB;
private JButton warpB;

private JPanel positionP;
private JLabel xposL;
private JLabel yposL;

// constructor: creates GUI
public Mesh() {
	super("Simple Mesh");

	setSize(getPreferredSize());

	Container c = getContentPane();         // create container
	c.setBackground(new Color(232, 232, 232));
	c.setForeground(new Color(0, 0, 0));
	c.setLayout(new BorderLayout());

	// new canvas
	mymesh = new MeshCanvas();
	mymesh.setImage("test.jpg");
	c.add(mymesh, BorderLayout.CENTER);

	// add control buttons
	clearB = new JButton("Clear");
	clearB.addActionListener(this);
	warpB = new JButton("Warp");
	warpB.addActionListener(this);

	controlP = new JPanel(new FlowLayout());
	controlP.setForeground(new Color(0, 0, 0));
	controlP.add(clearB);
	controlP.add(warpB);
	c.add(controlP, BorderLayout.SOUTH);

	// add position labels
	positionP = new JPanel(new FlowLayout());

	xposL = new JLabel("X: ");		// xposL used temporarily
	xposL.setForeground(new Color(0, 0, 0));
	positionP.add(xposL);
	xposL = new JLabel("000");		// real xposL
	xposL.setForeground(new Color(0, 0, 0));
	positionP.add(xposL);

	yposL = new JLabel("  Y: ");	// yposL used temporarily
	yposL.setForeground(new Color(0, 0, 0));
	positionP.add(yposL);
	yposL = new JLabel("000");		// real yposL
	yposL.setForeground(new Color(0, 0, 0));
	positionP.add(yposL);

	c.add(positionP, BorderLayout.NORTH);

	// keep track of mouse position
	mymesh.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));
		}
		public void mouseDragged(MouseEvent mevt) {
			xposL.setText(Integer.toString(mevt.getX()));
			yposL.setText(Integer.toString(mevt.getY()));
		}
	} );

	// allow use of "X" button to exit
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent winEvt) {
			setVisible(false);
			System.exit(0);
		}
	} );

} // Mesh ()


// capture button actions
public void actionPerformed(ActionEvent evt) {
	Object src = evt.getSource();

	// reset the original image and show the mesh
	if(src == clearB) {
		mymesh.clear();
	}
	if(src == warpB) {
		mymesh.makeWarp();
	}

} // actionPerformed()


// main: creates new instance of Mesh object
public static void main(String args[]) {
	Mesh pl;

	pl = new Mesh();
	pl.setSize(pl.getPreferredSize().width, pl.getPreferredSize().height);
	pl.setVisible(true);
} // main()


}
