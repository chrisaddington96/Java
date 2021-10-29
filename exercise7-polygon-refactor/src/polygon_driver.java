import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public class polygon_driver extends JFrame implements ActionListener, MouseListener, MouseMotionListener, ItemListener {
    // GUI elements
    private JFrame frame;
    private Container c;
    private polyPanel polygon_panel;
    private JPanel draw_panel;
    private JPanel controls_panel;

    // Buttons to control the application
    private JButton build_polygon;
    private JButton adjust_vertex;
    private JButton rotate_polygon;
    private JButton stop_button;
    private JButton reset_button;
    private JButton quit_button;

    // Menu for attributes
    private JMenuBar polygon_attributes;
    private JMenu menu;
    private JRadioButtonMenuItem white, red, orange, pink, green;
    private JCheckBoxMenuItem polygon_mode, bspline_mode;
    private JCheckBoxMenuItem filled;

    // Booleans to tell if the polygon has been built, if the user
    // is currently dragging, and if the user can drag a vertex
    private boolean polygon_built;
    private boolean adjusting_vertex;
    private boolean dragging;

    // Array of possible colors
    Color colors[];

    // Polygon that will be built
    private Polygon user_poly;
    private Polygon drag_poly;

    // Timer elements
    Timer rotate_time;
    private int angle;
    private boolean timer_running;

    // Booleans for bspline and filed polygon modes
    private boolean bspline;
    private boolean filled_mode;

    // Index of dragging vertex
    private int drag_index;

    // Constructor
    polygon_driver(){
        super("Polygon");
        // Build the GUI
        buildGUI();

        // Initialize variables
        init();

        // Make menu bar
        make_menu();
    }

    private void buildGUI(){
        // Set up the container
        frame = new JFrame();
        c = new Container();
        c = frame.getContentPane();

        // Set up the panels
        draw_panel = new JPanel();
        draw_panel.setBackground(Color.BLACK);
        controls_panel = new JPanel();

        // Set up buttons
        build_polygon = new JButton("Build");
        build_polygon.addActionListener(this);
        adjust_vertex = new JButton("Adjust Vertex");
        adjust_vertex.addActionListener(this);
        rotate_polygon = new JButton("Rotate");
        rotate_polygon.addActionListener(this);
        stop_button = new JButton("Stop");
        stop_button.addActionListener(this);
        reset_button = new JButton("Reset");
        reset_button.addActionListener(this);
        quit_button = new JButton("Quit");
        quit_button.addActionListener(this);

        // Add buttons to control panel
        controls_panel.add(build_polygon);
        controls_panel.add(adjust_vertex);
        controls_panel.add(rotate_polygon);
        controls_panel.add(stop_button);
        controls_panel.add(reset_button);
        controls_panel.add(quit_button);

        // Add mouse listener
        draw_panel.addMouseListener(this);

        // Add panels to the container
        c.add(controls_panel, BorderLayout.NORTH);
        c.add(draw_panel, BorderLayout.CENTER);

        // Make everything visible
        frame.setSize(800,800);
        frame.setVisible(true);
    }

    private void init(){
        // Set animating, polygon built, and adjusting a vertex to false
        polygon_built = false;
        adjusting_vertex = false;
        dragging = false;

        // Make polygon class
        user_poly = new Polygon();

        // Set colors
        colors = new Color[]{Color.white, Color.RED, Color.orange, Color.pink, Color.green};

        // Index of the vertex that is being drawn
        drag_index = 0;

        // Set the inital angle of rotation
        angle = 45;

        // The timer is not running by default
        timer_running = false;

        // Set filled and bspline to false
        bspline = false;
        filled_mode = false;
    }

    // Make menu
    public void make_menu(){
        // Create menu bar
        polygon_attributes = new JMenuBar();

        // Build the menu
        menu = new JMenu("Attributes");
        polygon_attributes.add(menu);

        // Radio buttons for color options
        ButtonGroup color_group = new ButtonGroup();
        white = new JRadioButtonMenuItem("White");
        red = new JRadioButtonMenuItem("Red");
        orange = new JRadioButtonMenuItem("Orange");
        pink = new JRadioButtonMenuItem("Pink");
        green = new JRadioButtonMenuItem("Green");

        // Set white to default and add buttons to color group
        white.setSelected(true);
        color_group.add(white);
        color_group.add(red);
        color_group.add(orange);
        color_group.add(pink);
        color_group.add(green);

        // Add radio buttons to menu and add seperator
        menu.add(white);
        menu.add(red);
        menu.add(orange);
        menu.add(pink);
        menu.add(green);
        menu.addSeparator();

        // Make check boxes
        polygon_mode = new JCheckBoxMenuItem("Polygon Mode");
        polygon_mode.setSelected(true);
        bspline_mode = new JCheckBoxMenuItem("B-spline Mode");
        menu.add(polygon_mode);
        menu.add(bspline_mode);
        menu.addSeparator();
        filled = new JCheckBoxMenuItem("Fill polygon?");
        menu.add(filled);

        // Add item listeners
        white.addItemListener(this);
        red.addItemListener(this);
        orange.addItemListener( this);
        pink.addItemListener(this);
        green.addItemListener( this);
        polygon_mode.addItemListener( this);
        bspline_mode.addItemListener(this);
        filled.addItemListener( this);

        // Add menu to frame
        frame.setJMenuBar(polygon_attributes);

    }

    public void itemStateChanged(ItemEvent e){
        // Check the item event
        if(e.getSource() == white){
            polygon_panel.set_color(colors[0]);
        }
        if(e.getSource() == red){
            polygon_panel.set_color(colors[1]);
        }
        if(e.getSource() == orange){
            polygon_panel.set_color(colors[2]);
        }
        if(e.getSource() == pink){
            polygon_panel.set_color(colors[3]);
        }
        if(e.getSource() == green){
            polygon_panel.set_color(colors[4]);
        }
        if(e.getSource() == filled){
            polygon_panel.set_filled();
            filled_mode = !filled_mode;
        }
        if(e.getSource() == bspline_mode){
            polygon_panel.set_bspline();
            bspline = !bspline;
        }
        if(e.getSource() == polygon_mode){
            polygon_panel.set_polygon_mode();
        }
        c.revalidate();
        c.repaint();
    }

    private void displayVerticesPopup(){
        // Make popup
        JPopupMenu popup = new JPopupMenu();
        popup.add("You must make at least 4 vertices, you have made: " + user_poly.npoints);
        popup.setPopupSize(400,200);

        // Show the popup
        popup.show(c, 400, 400);

    }

    // Handle different actions
    public void actionPerformed(ActionEvent e){
        // Get text of the button clicked
        String input_text = e.getActionCommand();

        // Quit the app and clear up memory
        if(input_text.equals("Quit")){
            System.gc();
            System.exit(0);
        }

        // If the polygon has been built, allow for buttons to be clicked
        if(polygon_built) {
            // Adjust vertex
            if (input_text.equals("Adjust Vertex")) {
                // If not adjusting, begin adjusting vertex
                if(!adjusting_vertex) {
                    adjusting_vertex = true;
                    adjust_vertex.setBackground(Color.red);
                }
                else{
                    adjusting_vertex = false;
                    adjust_vertex.setBackground(null);
                }
                c.repaint();
            }
            // Rotate polygon
            else if(input_text.equals("Rotate")){
                // Change button color to indicate the polygon is rotating
                rotate_polygon.setBackground(Color.red);
                c.repaint();

                // Create timer, runs every 2 seconds
                rotate_time = new Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        // Set the angle and increment by 45 degrees
                        polygon_panel.set_angle(angle);
                        angle += 45;
                        polygon_panel.set_drag_box(false);

                        // Once the polygon has rotated 360 degrees, stop the timer
                        if(angle > 360){
                            Timer stop = (Timer)actionEvent.getSource();
                            stop.stop();
                            angle = 0;
                            timer_running = false;
                            rotate_polygon.setBackground(null);
                            polygon_panel.new_polygon(user_poly, bspline, filled_mode, true);
                            c.repaint();
                        }
                    }
                });
                rotate_time.start();
                timer_running = true;
                c.repaint();
            }
            // Change polygon attributes
            else if(input_text.equals("Attributes")){

            }
            // Stop polygon rotation
            else if(input_text.equals("Stop")){
                // If the timer is running, stop the timer
                if(timer_running) {
                    rotate_time.stop();
                    rotate_polygon.setBackground(null);
                }
            }
            // Reset polygon to allow for user to build new one
            else if(input_text.equals("Reset")){
                // Set polygon built to false
                polygon_built = false;
                bspline = false;
                filled_mode = false;

                // Reset attributes menu
                make_menu();

                // Reset the panel and polygon
                c.remove(polygon_panel);
                c.add(draw_panel);
                c.repaint();
                user_poly.reset();
            }
        }
        else{
            // Build the polygon
            if(input_text.equals("Build")){
                // If the number of vertices is greater than 3, build the polygon
                if(user_poly.npoints > 3) {
                    // Set polygon built to true
                    polygon_built = true;

                    // Render the polygon panel
                    polygon_panel = new polyPanel(user_poly);

                    // Set the polygon panels background to black and add mouse listener
                    polygon_panel.setBackground(Color.black);
                    polygon_panel.addMouseListener(this);
                    polygon_panel.addMouseMotionListener(this);

                    // Replace the panel with the polygon panel
                    c.remove(draw_panel);
                    c.add(polygon_panel, BorderLayout.CENTER);

                    // Redraw screen
                    c.revalidate();
                    c.repaint();

                }
                // If the user hasn't clicked enough, make a popup telling them to click 3 times
                else{
                    displayVerticesPopup();
                }
            }
        }
    }


    // Handle mouse clicks
    public void mousePressed(MouseEvent e){
         // Get the current point clicked
        Point curr_point = e.getPoint();

        // Create new vertex if polygon hasn't been built yet
        if(!polygon_built){
            // Get the clicks coordinates
            int x = curr_point.x;
            int y = curr_point.y;

            // Add the point to the polygon
            user_poly.addPoint(x, y);
        }

        // If polygon is built and user is dragging
        else if(adjusting_vertex && !dragging){
                if (polygon_panel.clickInRect(curr_point) != -1) {
                    dragging = true;
                    drag_index = polygon_panel.clickInRect(curr_point);
                }
        }
    }

    public void mouseDragged(MouseEvent e){
        if(adjusting_vertex){
            // Redraw the vertex that is being drawn
            if(dragging){
                int[] x = user_poly.xpoints;
                int[] y = user_poly.ypoints;
                x[drag_index] = e.getX();
                y[drag_index] = e.getY();
                drag_poly = new Polygon(x,y,x.length);
                polygon_panel.new_polygon2(drag_poly);
                c.repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    public void mouseReleased(MouseEvent e)
    {
        // If the polygon is being built
        if(dragging) {
            dragging = false;
            polygon_panel.new_polygon2(drag_poly);
            c.repaint();
        }
    }
    public void mouseExited(MouseEvent e)
    {

    }
    public void mouseEntered(MouseEvent e)
    {

    }
    public void mouseClicked(MouseEvent e)
    {

    }

    public static void main(String args[]){
        // Make application
        polygon_driver app = new polygon_driver();

        // Add window listener
        app.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e)
                    {
                        System.exit(0);
                    }
                }
        );
    }
}
