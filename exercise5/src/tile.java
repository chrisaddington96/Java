import javax.swing.*;
import java.awt.*;
import java.util.*;

public class tile extends JButton {
    // Class variables
    // Booleans for if the tile is a trap, has been flipped, and has been flagged
    private boolean trap;
    private boolean flipped;
    private boolean flagged;

    // Integers to store the coordinates of the tile in the greater grid
    private int x_coor;
    private int y_coor;

    // Neighbors of the tile
    // 0 1 2
    // 3 t 4
    // 5 6 7
    private ArrayList<ArrayList<Integer>> neighbor;

    // Each tile represents a JButton
    private JButton button;

    // Image for thermal traps if flipped and string of its name
    private ImageIcon fire;
    private String fire_image = "fire.jpeg";

    // Image for flagged tiles and string of its name
    private ImageIcon flag;
    private String flag_image = "flag.png";

    // Integer to store the number of neighbors that are thermal traps (between 0 and 8)
    private int neighbor_traps;

    // Constructor
    tile(int x, int y){
        // Set all flags to false
        trap = false;
        flipped = false;
        flagged = false;

        // Set coordinates to (-1,-1) to indicate game not initialized
        x_coor = x;
        y_coor = y;

        // Populate neighbors
        // Note: Neighbor coordinates may contain an out of range
        // number, that will be handled by the driver
        // 0 1 2
        // 3 t 4
        // 5 6 7
        ArrayList<Integer> n0 = new ArrayList<Integer>();
        n0.add(x-1);
        n0.add(y-1);
        ArrayList<Integer> n1 = new ArrayList<Integer>();
        n1.add(x);
        n1.add(y-1);
        ArrayList<Integer> n2 = new ArrayList<Integer>();
        n2.add(x+1);
        n2.add(y-1);
        ArrayList<Integer> n3 = new ArrayList<Integer>();
        n3.add(x-1);
        n3.add(y);
        ArrayList<Integer> n4 = new ArrayList<Integer>();
        n4.add(x+1);
        n4.add(y);
        ArrayList<Integer> n5 = new ArrayList<Integer>();
        n5.add(x-1);
        n5.add(y+1);
        ArrayList<Integer> n6 = new ArrayList<Integer>();
        n6.add(x);
        n6.add(y+1);
        ArrayList<Integer> n7 = new ArrayList<Integer>();
        n7.add(x+1);
        n7.add(y+1);
        neighbor = new ArrayList<ArrayList<Integer>>();
        neighbor.add(n0);
        neighbor.add(n1);
        neighbor.add(n2);
        neighbor.add(n3);
        neighbor.add(n4);
        neighbor.add(n5);
        neighbor.add(n6);
        neighbor.add(n7);

        // Build JButton and set initial color
        button = new JButton();
        button.setBackground(Color.YELLOW);
        button.setName(x_coor + ":" + y_coor);

        // Build ImageIcon for thermal trap
        fire = new ImageIcon(fire_image);
        fire.setDescription("thermal trap");

        // Build ImageIcon for flag
        flag = new ImageIcon(flag_image);
        flag.setDescription("flag");
    }

    // Get operators
    public ArrayList<ArrayList<Integer>> getNeighbor(){
        return neighbor;
    }
    public boolean getTrap(){
        return trap;
    }
    public boolean getFlipped(){
        return flipped;
    }
    public boolean getFlagged(){
        return flagged;
    }
    public int getX(){
        return x_coor;
    }
    public int getY(){
        return y_coor;
    }
    public JButton getButton(){
        return button;
    }

    public int getNumNeighborTraps(){
        return neighbor_traps;
    }

    // Set operators
    public void setTrap(){
        trap = true;
    }
    public void unsetTrap(){
        trap = false;
    }
    public void setFlipped(){
        // Check if flagged, can only flip if not flagged or already flipped
        if(!flagged && !flipped){
            flipped = true;
            if(neighbor_traps > 0) {
                button.setText(String.valueOf(neighbor_traps));
            }
            button.setBackground(Color.white);
            //System.out.print(x_coor + ":" + y_coor + " flipped\n");
        }
    }
    public void setFlagged(){
        // Can only set or unset a flag if not flipped already
        if(!flipped){
            // Toggle the current value of flipped
            if(flagged){
                // Remove flag image
                button.setIcon(null);
                // Set flagged to false
                flagged = false;
            }
            else{
                // Add flag image
                // FIGURE OUT HOW TO SCALE IMAGE
                button.setIcon(flag);
                // Change flagged to true
                flagged = true;
            }
        }
    }
    public void setCoor(int x, int y){
        x_coor = x;
        y_coor = y;
        this.setText(x + ":" + y);
    }
    public void setNeighborTraps(int trap){
        neighbor_traps = trap;
    }

    // Show image operations
    public void showTrap(){
        button.setIcon(fire);
    }
    public void showFlag(){
        button.setIcon(flag);
    }
}
