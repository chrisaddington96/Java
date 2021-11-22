import java.awt.*;
import java.awt.geom.*;


public class Triangle {

private Point2D.Double tri[];

    public Triangle (int x1, int y1, int x2, int y2, int x3, int y3) {
	tri = new Point2D.Double[3];
	tri[0] = new Point2D.Double ((double)x1, (double)y1);
	tri[1] = new Point2D.Double ((double)x2, (double)y2);
	tri[2] = new Point2D.Double ((double)x3, (double)y3);
    }

    public Triangle (double [] coords) {
	if (coords.length < 6) {
		return;
	}

	tri = new Point2D.Double[3];
	int j=0;
	for (int i=0; i<3; i++)
	   tri[i] = new Point2D.Double (coords[j++], coords[j++]);
    }

    public double getX (int index) {
	if ((index >= 0) && (index < 6))
	   return (tri[index].getX()); 
	System.out.println("Index out of bounds in getX()");
	return(0.0);
    }

    public double getY (int index) {
	if ((index >= 0) && (index < 6))
	   return (tri[index].getY()); 
	System.out.println("Index out of bounds in getY()");
	return(0.0);
    }

    public void setX (int index, double x) {
	if ((index >= 0) && (index < 6)) {
	   double tmpy = tri[index].getY();
	   tri[index] = new Point2D.Double (x, tmpy);
        }
    }

    public void setY (int index, double y) {
	if ((index >= 0) && (index < 6)) {
	   double tmpx = tri[index].getX();
	   tri[index] = new Point2D.Double (tmpx, y);
        }
    }

    public double [] getAllCoords () {
	double[] allcoords = new double [6];
	allcoords[0] = tri[0].getX();
	allcoords[1] = tri[0].getY();
	allcoords[2] = tri[1].getX();
	allcoords[3] = tri[1].getY();
	allcoords[4] = tri[2].getX();
	allcoords[5] = tri[2].getY();
	return allcoords;
    }

    public double [] getXCoords () {
	double [] Xcoords = new double [3];
	Xcoords[0] = tri[0].getX();
	Xcoords[1] = tri[1].getX();
	Xcoords[2] = tri[2].getX();
	return Xcoords;
    }

    public double [] getYCoords () {
	double [] Ycoords = new double [3];
	Ycoords[0] = tri[0].getY();
	Ycoords[1] = tri[1].getY();
	Ycoords[2] = tri[2].getY();
	return Ycoords;
    }

	public void drawTriangle(Graphics2D g){
		// Get all coordinates for the triangle
		double[] allcoords = getAllCoords();

		// Make the lines coresponding to the triangle
		Shape line1 = new Line2D.Double(allcoords[0], allcoords[1], allcoords[2], allcoords[3]);
		Shape line2 = new Line2D.Double(allcoords[2], allcoords[3], allcoords[4], allcoords[5]);
		//Shape line3 = new Line2D.Double(allcoords[0], allcoords[1], allcoords[4], allcoords[5]);

		// Draw the lines
		g.draw(line1);
		g.draw(line2);
		//g.draw(line3);

	}

}

