import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Mesh extends JFrame implements ActionListener {

private MeshCanvas mymeshLeft;
private MeshCanvas mymeshRight;
private JPanel meshPanel;

private JPanel controlP;
private JButton clearB;
private JButton previewB;
private JSlider frameSlider;

private int controlX;
private int controlY;

private MeshCanvas animateMesh;
private Point[][] startCPoints;
private Point[][] endCPoints;
private int numRows = 6;
private int numCols = 6;

private Timer animateTimer;
private int numFrames = 5;
private boolean timerRunning;
private float time;

private BufferedImage imageLeft;
private BufferedImage imageRight;

// constructor: creates GUI
public Mesh() {
	super("Simple Mesh");

	//setSize(getPreferredSize());
	setExtendedState(JFrame.MAXIMIZED_BOTH);

	Container c = getContentPane();         // create container
	c.setBackground(new Color(232, 232, 232));
	c.setForeground(new Color(0, 0, 0));
	c.setLayout(new BorderLayout());

	// Make mesh panel
	meshPanel = new JPanel(new FlowLayout());

	// new canvas
	mymeshLeft = new MeshCanvas();
	mymeshLeft.setImage("default.jpg");
	meshPanel.add(mymeshLeft);

	// new canvas
	mymeshRight = new MeshCanvas();
	mymeshRight.setImage("default.jpg");
	meshPanel.add(mymeshRight);

	c.add(meshPanel, BorderLayout.CENTER);

	// add control buttons
	clearB = new JButton("Clear");
	clearB.addActionListener(this);
	previewB = new JButton("Preview Warp");
	previewB.addActionListener(this);

	// Add slider for number of tween frames
	frameSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
	frameSlider.setMajorTickSpacing(1);
	frameSlider.setPaintTicks(true);
	frameSlider.setPaintLabels(true);

	// Add change listener
	frameSlider.addChangeListener(new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			// Get the slider, and once it stops adjusting
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting()){
				numFrames = (int)source.getValue();
			}
		}
	});

	// Build control panel
	controlP = new JPanel(new FlowLayout());
	controlP.setForeground(new Color(0, 0, 0));
	controlP.add(clearB);
	controlP.add(previewB);
	controlP.add(frameSlider);
	c.add(controlP, BorderLayout.SOUTH);


	// keep track of mouse position
	mymeshLeft.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent mevt) {
		}
		public void mouseDragged(MouseEvent mevt) {
			controlX = mymeshLeft.returnControlX();
			controlY = mymeshLeft.returnControlY();

			mymeshRight.setControlPoint(controlX, controlY);
		}
	} );

	// keep track of mouse position
	mymeshRight.addMouseMotionListener(new MouseMotionAdapter() {
		public void mouseMoved(MouseEvent mevt) {
		}
		public void mouseDragged(MouseEvent mevt) {
			controlX = mymeshRight.returnControlX();
			controlY = mymeshRight.returnControlY();

			mymeshLeft.setControlPoint(controlX, controlY);
		}
	} );


	// allow use of "X" button to exit
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent winEvt) {
			setVisible(false);
			System.exit(0);
		}
	} );

	// Timer not running by default
	timerRunning = false;

	// Build menu for loading file
	buildMenu();

} // Mesh ()

	private void buildMenu(){
		// Make the file chooser for user to select a file from the current directory
		final JFileChooser fc = new JFileChooser(".");

		// Build the menu bar and add it to the JFrame
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// Make menu called file, and items for opening a file and exiting
		JMenu fileMenu = new JMenu ("File");
		JMenuItem fileopenLeft = new JMenuItem ("Open Left");
		JMenuItem fileopenRight = new JMenuItem("Open Right");
		JMenuItem fileexit = new JMenuItem ("Exit");

		// Add an action listener to the file open item
		fileopenLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make the open file dialog
				int returnVal = fc.showOpenDialog(Mesh.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					// If allowed, get the selected file
					File file = fc.getSelectedFile();
					try {
						// Read the image
						imageLeft = ImageIO.read(file);
					} catch (IOException e1){};

					// Show the image
					mymeshLeft.setImage(imageLeft);

				}
			}
		});
		fileopenRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make the open file dialog
				int returnVal = fc.showOpenDialog(Mesh.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					// If allowed, get the selected file
					File file = fc.getSelectedFile();
					try {
						// Read the image
						imageRight = ImageIO.read(file);
					} catch (IOException e1){};

					// Show the image
					mymeshRight.setImage(imageRight);

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

		// Add both menu items to the file menu, then add the file and settings menu to the menubar
		fileMenu.add(fileopenLeft);
		fileMenu.add(fileopenRight);
		fileMenu.add(fileexit);
		menuBar.add(fileMenu);
	}

	// Make the preview of the warp
	public void makePreview(){
		// Make popup
		animateMesh = new MeshCanvas();
		animateMesh.setImage("default.jpg");
		animateMesh.newGrid(startCPoints);
		meshPanel.add(animateMesh);

		// Create timer that runs every two seconds
		time = 0;
		animateTimer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Make new canvas mesh and 2d array of control points
				Point[][] animateCPoints = new Point[numCols-1][numRows-1];

				// Calculate next key frame
				animateCPoints = calculatePoints(animateCPoints);
				animateMesh.newGrid(animateCPoints);

				// Increment time
				time += (1.0/numFrames);
				if(time > 1){
					animateTimer.stop();
				}
			}
		});
		animateTimer.start();
	}

	public Point[][] calculatePoints(Point[][] animatedPoints){
		// Make a copy of the start and end points
		Point[][] startPoints = new Point[numCols-1][numRows-1];
		Point[][] endPoints = new Point[numCols-1][numRows-1];
		for(int i = 0; i < numCols-1; i++){
			for(int j = 0; j < numRows-1; j++){
				startPoints[i][j] = startCPoints[i][j];
				endPoints[i][j] = endCPoints[i][j];
			}
		}

		// Calculate the new points
		for(int i = 0; i < numCols-1; i++){
			for(int j = 0; j < numRows-1; j++){
				float animX = startPoints[i][j].x - (startPoints[i][j].x - endPoints[i][j].x) * time;
				float animY = startPoints[i][j].y - (startPoints[i][j].y - endPoints[i][j].y) * time;

				animatedPoints[i][j] = new Point((int) animX, (int) animY);
			}
		}

		return animatedPoints;
	}

// capture button actions
public void actionPerformed(ActionEvent evt) {
	Object src = evt.getSource();

	// reset the original image and show the mesh
	if(src == clearB) {
		mymeshLeft.clear();
		mymeshRight.clear();
	}
	if(src == previewB) {
		//mymeshLeft.makeWarp();
		//mymeshRight.makeWarp();
		startCPoints = mymeshLeft.returnGrid().getCpoints();
		endCPoints = mymeshRight.returnGrid().getCpoints();
		makePreview();
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
