// Mesh and Warp Canvas


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
//import com.sun.image.codec.jpeg.*;
import java.util.ArrayList;


public class MeshCanvas extends Canvas
             implements MouseListener, MouseMotionListener {

private int x;
private int y;
private boolean selected;		
private boolean firsttime;
private boolean showwarp=false;
private BufferedImage bim=null;
private BufferedImage bimwarp=null;
private Triangle S, T;

private Grid grid;
private ArrayList<Triangle> triangles;
private Point[][] controlPoints;

private int controlX, controlY;
private int numBlocks = 6;

private int xsize, ysize;

private final int THRESHOLD_DISTANCE=10;

// constructor: creates an empty mesh point
public MeshCanvas () {
	//setSize(getPreferredSize());
	setSize(500,500);
	grid = new Grid(numBlocks, numBlocks, 500, 500);
	addMouseListener(this);
	addMouseMotionListener(this);
	selected = false;
	firsttime=true;
}

// resets mesh point to center
public void clear() {
	selected = false;
	firsttime=true;
	showwarp=false;
	grid = new Grid(numBlocks, numBlocks, bim.getWidth(), bim.getHeight());
	controlPoints = grid.getCpoints();
	drawMesh();
	this.repaint();

}

public void mouseClicked(MouseEvent mevt) {
}
public void mouseEntered(MouseEvent mevt) {
}
public void mouseExited(MouseEvent mevt) {
}

// checks for user selecting the mesh control point within a threshold
// distance of the point

public void mousePressed(MouseEvent E) {
	int curx, cury;

	curx = E.getX();
	cury = E.getY();
	for(int i = 0; i < numBlocks-1; i++) {
		for(int j = 0; j < numBlocks-1; j++) {
			double distance = Math.sqrt((curx - controlPoints[i][j].getX()) * (curx - controlPoints[i][j].getX()) + (cury - controlPoints[i][j].getY()) * (cury - controlPoints[i][j].getY()));
			if (distance < THRESHOLD_DISTANCE) {
				selected = true;
				firsttime = false;
				controlX = i;
				controlY = j;
			}
		}
	}
}


// if a point is being dragged, the point is released
// otherwise, adds/removes a point at current position
public void mouseReleased(MouseEvent E) {

	// if a point was selected, it was just released
	if(selected) {
		selected = false;
	}
	//makeWarp();
}


// if a point is selected, drag it
public void mouseDragged(MouseEvent E) {

	// if a point is selected, it's being moved
	// redraw (rubberbanding)
	if(selected) {
		x=E.getX();
		y=E.getY();
		controlPoints[controlX][controlY].x = x;
		controlPoints[controlX][controlY].y = y;
		grid.updateGrid(controlPoints);
                // Can show warp dynamically by unstubbing here
                // This will apply the warp during rubberbanding
	        //makeWarp();
		drawMesh();
	}
}

public void mouseMoved(MouseEvent mevt) {
}


// draws the mesh on top of the background image
public void drawMesh() {
	repaint();
}

private BufferedImage readImage (String file) {

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

public void setImage(String file) {
   bim = readImage(file);
   setSize(new Dimension(bim.getWidth(), bim.getHeight()));
   grid = new Grid(numBlocks, numBlocks, bim.getWidth(), bim.getHeight());
   controlPoints = grid.getCpoints();
   triangles = grid.getTriangles();
   this.repaint();
}

public void setImage(BufferedImage bufIm){
	bim = bufIm;
	setSize(new Dimension(bim.getWidth(), bim.getHeight()));
	grid = new Grid(numBlocks, numBlocks, bim.getWidth(), bim.getHeight());
	controlPoints = grid.getCpoints();
	triangles = grid.getTriangles();
	this.repaint();
}

public void makeWarp() {

	if (bimwarp == null)
           bimwarp = new BufferedImage (bim.getWidth(this), 
                         bim.getHeight(this), 
                         BufferedImage.TYPE_INT_RGB);


// Divide the image into 4 triangles defined by the one point
// out in the image somewhere.
// MorphTools has to be set up NOT to clear the destination image
// each time it is called.
	S = new Triangle (0, 0, xsize/2, ysize/2, xsize, 0);
	T = new Triangle (0, 0, x, y, xsize, 0);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (0, 0, 0, ysize, xsize/2, ysize/2);
	T = new Triangle (0, 0, 0, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (0, ysize, xsize, ysize, xsize/2, ysize/2);
	T = new Triangle (0, ysize, xsize, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);

	S = new Triangle (xsize, 0, xsize, ysize, xsize/2, ysize/2);
	T = new Triangle (xsize, 0, xsize, ysize, x, y);
	MorphTools.warpTriangle(bim, bimwarp, S, T, null, null, false);



	showwarp=true;
	this.repaint();
}

// Over-ride update method 
public void update(Graphics g) {
	paint(g);
}

// paints the polyline
public void paint (Graphics g) {

	// draw lines from each corner of canvas to the mesh point
	// with a circle at the mesh point

	xsize=getWidth();
	ysize=getHeight();
	triangles = grid.getTriangles();

	if (firsttime) { x = xsize/2;  y = ysize/2; firsttime=false;}

	Graphics2D big = (Graphics2D) g;
	if (showwarp)
           big.drawImage(bimwarp, 0, 0, this);
	else {
           big.drawImage(bim, 0, 0, this);
		   // Draw the triangles
		   for(int i = 0; i < triangles.size(); i++){
			   triangles.get(i).drawTriangle(big);
		   }

		   // Draw the control points
			for(int i = 0; i < numBlocks-1; i++){
				for(int j = 0; j < numBlocks-1; j++){
					int x = controlPoints[i][j].x;
					int y = controlPoints[i][j].y;
					g.fillOval(x-6, y-6, 12, 12);
				}
			}

			// Draw red oval over current control point
			int x = controlPoints[controlX][controlY].x;
			int y = controlPoints[controlX][controlY].y;
			g.setColor(Color.red);
			g.fillOval(x-6, y-6, 12, 12);
	}

} // paint()

	public void setControlPoint(int x, int y){
		controlX = x;
		controlY = y;
		repaint();
	}

	public int returnControlX(){ return controlX; }
	public int returnControlY(){ return controlY; }
	public Grid returnGrid(){ return grid; }

	public void newGrid(Point[][] newCPoints){
		controlPoints = newCPoints;
		grid.updateGrid(controlPoints);
		repaint();
	}

}
