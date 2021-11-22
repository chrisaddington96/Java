import java.awt.*;
import java.util.ArrayList;

public class Grid {
    private Point[][] points;
    private Point[][] cPoints;
    private int rows;
    private int cols;
    private ArrayList<Triangle> triangles;

    public Grid(int numRows, int numCols, int imgWidth, int imgHeight){
        super();

        // Assing rows and columns
        rows = numRows;
        cols = numCols;

        // Initialize points for each grid cell
        points = new Point[rows][cols];
        cPoints = new Point[rows-1][cols-1];

        // Make each cell correspond to a part of the image
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                // Make a point for the upper left corner of each cell
                points[i][j] = new Point(imgWidth * i / (rows-1), imgHeight * j / (cols-1));

                if(i < rows-1 && j < cols-1) {
                    // Make the central control point for each cell
                    int x = (imgWidth * i / (rows - 1) + imgWidth * (i + 1) / (rows - 1)) / 2;
                    int y = (imgHeight * j / (cols - 1) + imgHeight * (j + 1) / (cols - 1)) / 2;
                    cPoints[i][j] = new Point(x, y);
                }
            }
        }

        // Make the triangles for each cell
        makeTriangles();

    }

    public void makeTriangles(){
        // Get the total number of triangle for the grid
        int numTriangles = (rows * cols) * 4;

        // Make array of triangles to be returned latter
        triangles = new ArrayList<Triangle>();

        for(int i = 0; i < rows-1; i++){
            for(int j = 0; j < cols-1; j++){

                // Get the points of each cell
                Point upperLeft = points[i][j];
                Point upperRight = points[i+1][j];
                Point bottomLeft = points[i+1][j+1];
                Point bottomRight = points[i][j+1];
                Point control = cPoints[i][j];


                // Create each triangle
                Triangle left = new Triangle(upperLeft.x, upperLeft.y, control.x, control.y, bottomLeft.x, bottomLeft.y);
                Triangle top = new Triangle(upperLeft.x, upperLeft.y, control.x, control.y, upperRight.x, upperRight.y);
                Triangle right = new Triangle(upperRight.x, upperRight.y, control.x, control.y, bottomRight.x, bottomRight.y);
                Triangle bottom = new Triangle(bottomRight.x, bottomRight.y, control.x, control.y, bottomLeft.x, bottomLeft.y);

                // Add each triangle to the array list of triangles
                triangles.add(left);
                triangles.add(top);
                triangles.add(right);
                triangles.add(bottom);

            }
        }
    }

    public void updateGrid(Point[][] newCPoints){
        cPoints = newCPoints;
        makeTriangles();
    }

    // Accessor to get the array list of triangles
    public ArrayList<Triangle> getTriangles(){
        return triangles;
    }
    // Accessor to get control points
    public Point[][] getCpoints(){ return cPoints; }
    // Accessor to get the grid corner points for each cell
    public Point[][] getPoints(){ return points; }
}
