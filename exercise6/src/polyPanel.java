import java.awt.*;
import javax.swing.*;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Stack;

public class polyPanel extends JPanel{
    // User drawn polygon
    private Polygon curr_poly;

    // Stack of rectangles for rubberbanding
    Stack<Polygon> vertex_box;

    // Midpoint algorithm coordinates
    private int x_cord1;
    private int x_cord2;
    private int y_cord1;
    private int y_cord2;

    // Polygon centroid
    private int center_x;
    private int center_y;

    // Polygon attributes
    private Color poly_color;
    private int line_width;
    private boolean filled;
    private boolean polygon_mode;
    private boolean bspline_mode;

    // Stack to hold the original points
    private Stack<Point> original_points;

    // Angle of rotation
    private int angle;

    // Constructor
    public polyPanel(Polygon given_poly){
        init(given_poly);
    }

    public void new_polygon(Polygon given_poly){
        init(given_poly);
    }

    // Initialize the polygon panel
    public void init(Polygon given_poly){
        // Make polygon
        curr_poly = given_poly;

        // Calculate the centroid of the polygon
        calc_center();

        // Set default attributes
        poly_color = Color.white;
        line_width = 3;
        filled = false;
        polygon_mode = true;
        bspline_mode = false;

        // Initialize stack of points
        original_points = new Stack<Point>();

        // Populate the stack with each point
        buildStack();

        // Build drag boxes
        vertex_box = new Stack<Polygon>();

        // Set angle to 0
        angle = 0;
    }

    // Calculate the centroid of the polygon
    public void calc_center(){
        // Get the x and y coordinate lists
        int[] x = curr_poly.xpoints;
        int[] y = curr_poly.ypoints;

        // Accumulators for x and y
        int total_x = 0;
        int total_y = 0;

        // Accumulate each point
        for(int i = 0; i < curr_poly.npoints; i++){
            total_x += x[i];
            total_y += y[i];
        }

        // Create the center point
        center_x = total_x/curr_poly.npoints;
        center_y = total_y/curr_poly.npoints;

    }

    // Build the rectangles that allow a user to rubber band a given vertex
    public void buildDragBoxes(Graphics2D g2d){
        g2d.setColor(Color.YELLOW);
        g2d.setStroke(new BasicStroke(1));
        for(int i = 0; i < curr_poly.npoints; i++) {
            int x = curr_poly.xpoints[i];
            int y = curr_poly.ypoints[i];
            int x2[] = {x-5, x-5, x+5, x+5};
            int y2[] = {y+5, y-5, y-5, y+5};
            vertex_box.add(new Polygon(x2, y2, 4));
            g2d.drawPolygon(vertex_box.elementAt(i));
        }
        repaint();
    }

    // Check if a given vertex is clicked on
    public int clickInRect(Point click){
        int vertex_index = -1;
        if(vertex_box.size() > 0) {
            for (int i = 0; i < curr_poly.npoints; i++) {
                if (vertex_box.elementAt(i).contains(click)) {
                    vertex_index = i;
                }
            }
        }
        else{
            System.out.print(vertex_box.size() + "\n");
        }
        return vertex_index;
    }

    // Set operators
    public void set_color(Color new_color){
        poly_color = new_color;
        repaint();
    }
    public void set_width(int new_width){
        line_width = new_width;
        repaint();
    }
    public void set_angle(int new_angle){
        angle = new_angle;
        repaint();
    }
    public void set_filled(){
        if(!filled) {
            filled = true;
        }
        else{
            filled = false;
        }
    }
    public void set_bspline(){
        if(!bspline_mode){
            bspline_mode = true;
        }
        else{
            bspline_mode = false;
        }
    }
    public void set_polygon_mode(){
        if(!polygon_mode){
            polygon_mode = true;
        }
        else{
            polygon_mode = false;
        }
    }


    // Populate the stack with the original points
    public void buildStack(){
        // Get the x and y coordinates
        int[] x = curr_poly.xpoints;
        int[] y = curr_poly.ypoints;

        // For each point, create a Point object
        for(int i = 0; i < curr_poly.npoints; i++){
            Point curr_point = new Point(x[i], y[i]);

            // Add the current point to the stack
            original_points.push(curr_point);
        }
    }

    // Draw a vertex slightly larger for visibilty
    public void drawPoints(Graphics2D g){
        // Get the x and y coordinates
        int[] x = curr_poly.xpoints;
        int[] y = curr_poly.ypoints;

        // For each vertex
        for(int i = 0; i < curr_poly.npoints; i++){
            // Get circle center points
            int x_center = x[i] - 3;
            int y_center = y[i] - 3;

            // Draw an oval around the vertex
            g.setColor(Color.red);
            g.drawOval(x_center, y_center, 6, 6);
        }

        g.setColor(Color.blue);
        g.drawOval(center_x, center_y, 4, 4);
    }

    // draw all the lines of the poly using the Midpoint Alg
    public void draw_poly_by_bres (Graphics2D g) {
        for (int i=0; i<(curr_poly.npoints-1); i++) {
            x_cord1 = curr_poly.xpoints[i];
            x_cord2 = curr_poly.xpoints[i+1];
            y_cord1 = curr_poly.ypoints[i];
            y_cord2 = curr_poly.ypoints[i+1];
            draw_line_by_bres(g);
        }
        x_cord1 = curr_poly.xpoints[0];
        x_cord2 = curr_poly.xpoints[curr_poly.npoints-1];
        y_cord1 = curr_poly.ypoints[0];
        y_cord2 = curr_poly.ypoints[curr_poly.npoints-1];
        draw_line_by_bres(g);
    }

    // draw a single line segment using Midpoint alg
    public void draw_line_by_bres (Graphics2D g) {

        float m;

        int dy=Math.abs(y_cord2-y_cord1);
        int dx=Math.abs(x_cord2-x_cord1);
        m=(float)dy/(float)dx;

        if(m<=1)
            slope_less_1(g);
        else
            slope_great_1(g);
    }


    //    slope less than 1

    public void slope_less_1 (Graphics g) {
        int x = x_cord1, y = y_cord1 , p = 0,xEnd=0,yEnd=0;
        int dx,dy;
        float m;

        plotpoints(x,y, g);
        dx = (x_cord1-x_cord2);
        dy = (y_cord1-y_cord2);
        m=(float)dy/(float)dx;

        dx = Math.abs(x_cord1-x_cord2);
        dy = Math.abs(y_cord1-y_cord2);
        //p = 2 * dy - dx;
        p = dy - dx;
        if(x_cord1>x_cord2)
        {
            x=x_cord2;
            y=y_cord2;
            xEnd = x_cord1;
        }
        else
        {	x=x_cord1;
            y=y_cord1;
            xEnd= x_cord2;
        }
        plotpoints (x, y, g);

        // Set up the constant "increments"
        int incrE=2*dy;
        int incrNE=2*(dy-dx);

        while (x < xEnd) {
            x++;
            if (p < 0)
                p += incrE;
            else {
                p += incrNE;
                if (m < 0) y--;
                else y++;
            }
            plotpoints (x, y, g);
        }
    }

    public void slope_great_1 (Graphics g) {
        int x = x_cord1, y = y_cord1 , p = 0,xEnd=0,yEnd=0;
        int dx,dy;
        float m;

        plotpoints(x,y, g);
        dx = (x_cord1-x_cord2);
        dy = (y_cord1-y_cord2);
        m=(float)dy/(float)dx;

        dx = Math.abs(x_cord1-x_cord2);
        dy = Math.abs(y_cord1-y_cord2);
        p = 2 * dx - dy;
        if(y_cord1>y_cord2)
        {
            x=x_cord2;
            y=y_cord2;
            yEnd = y_cord1;
        }
        else
        {	x=x_cord1;
            y=y_cord1;
            yEnd= y_cord2;
        }
        plotpoints (x, y, g);
        int incrE=2*dx;
        int incrNE=2*(dx-dy);
        while (y < yEnd)
        {
            y++;
            if (p < 0)
                p += incrE;
            else {
                p += incrNE;
                if (m <0 ) x--;
                else x++;
            }
            plotpoints (x, y, g);
        }
    }

    // Helper method to draw a single point
    public void  plotpoints(int x, int y, Graphics g) {
        g.fillRect(x , y , 1,1);
    }

    public void bspline(Graphics2D g2d){
        double x, y, xold, yold, A, B, C;
        double t1, t2, t3;
        double deltax1, deltax2, deltax3;
        double deltay1, deltay2, deltay3;
        int count = curr_poly.npoints;
        int limit = 4;
        int intervals = 20;
        int[] xpoints = curr_poly.xpoints;
        int[] ypoints = curr_poly.ypoints;

        // must have at least 4 points
        if (count < limit)
            return;

        for(int i = 0; i < curr_poly.npoints - 3; i++) {
            //  This is forward differencing code to draw fast Bspline
            t1 = 1.0 / intervals;
            t2 = t1 * t1;
            t3 = t2 * t1;

            //  For B-spline curve, "D" is the starting x,y coord
            //  So the first x,y coord is the D term from the cubic equation
            x = (xpoints[i] + 4.0 * xpoints[i+1] + xpoints[i+2]) / 6.0;
            y = (ypoints[i] + 4.0 * ypoints[i+1] + ypoints[i+2]) / 6.0;
            xold = x;
            yold = y;

            // set up deltas for the x-coords of B-spline
            A = (-xpoints[i] + 3 * xpoints[i+1] - 3 * xpoints[i+2] + xpoints[i+3]) / 6.0;
            B = (3 * xpoints[i] - 6 * xpoints[i+1] + 3 * xpoints[i+2]) / 6.0;
            C = (-3 * xpoints[i] + 3 * xpoints[i+2]) / 6.0;

            deltax1 = A * t3 + B * t2 + C * t1;
            deltax2 = 6 * A * t3 + 2 * B * t2;
            deltax3 = 6 * A * t3;

            // set up deltas for the y-coords
            A = (-ypoints[i] + 3 * ypoints[i+1] - 3 * ypoints[i+2] + ypoints[i+3]) / 6.0;
            B = (3 * ypoints[i] - 6 * ypoints[i+1] + 3 * ypoints[i+2]) / 6.0;
            C = (-3 * ypoints[i] + 3 * ypoints[i+2]) / 6.0;

            deltay1 = A * t3 + B * t2 + C * t1;
            deltay2 = 6 * A * t3 + 2 * B * t2;
            deltay3 = 6 * A * t3;

            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.RED);

            for (int j = 0; j < intervals; j++) {
                x += deltax1;
                deltax1 += deltax2;
                deltax2 += deltax3;

                y += deltay1;
                deltay1 += deltay2;
                deltay2 += deltay3;

                g2d.drawLine((int) xold, (int) yold, (int) x, (int) y);
                xold = x;
                yold = y;
            }

            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(poly_color);
        }
    }

    // Paint component override
    public void paintComponent(Graphics g){
        // Clear drawing area
        super.paintComponent(g);

        // Set polygon color
        g.setColor(poly_color);

        // Type cast g to a Graphics2D object and set edge width
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(line_width));

        AffineTransform a = AffineTransform.getRotateInstance (Math.toRadians(angle),
                (double) center_x, (double) center_y);
        g2d.setTransform(a);
        //g2d.rotate(Math.toRadians(angle));

        // Draw the current polygon
        if(polygon_mode) {
            draw_poly_by_bres(g2d);
        }
        if(bspline_mode){
            bspline(g2d);
        }

        // If polygon should be filled, fill it
        if(filled) {
            g2d.fill(curr_poly);
        }

        // Draw the vertices larger for visibility
        drawPoints(g2d);

        // Build the drag boxes for rubberbanding
        buildDragBoxes(g2d);
    }
}
