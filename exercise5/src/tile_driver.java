import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.Timer;

public class tile_driver extends JFrame implements ActionListener {
    // Class variables
    // Integers to define the number of tiles in a row and column
    private int num_row;
    private int num_col;

    // Integer to hold the number of thermal traps in a game
    private int num_traps;

    // Integer to store the number of tiles clicked
    private int clicked;

    // Integers to hold the time elapsed for a game
    private int minutes;
    private int seconds;

    // 2D array to hold each tile
    private tile grid[][];

    // Container and JPanels to house the game and game menu
    private JFrame game;
    private Container c;
    private JPanel game_panel;
    private JPanel menu_panel;

    // Elements to display the elapsed time
    private JButton disp_time;
    private String format_time;
    private Timer timer;

    // Menu elements to start, restart, and quit the game and popup help
    private JButton start;
    private JButton help;
    private JButton quit;
    private JButton restart;

    // Settings menu elements
    private JMenuBar menu_bar;
    private JMenu settings;
    private JMenu settings_sub;
    private JMenuItem default_settings;
    private JMenu custom;
    private JMenuItem easy;
    private JMenuItem medium;
    private JMenuItem hard;

    // Boolean for if the game is running
    private boolean running;
    // Boolean for if you won or not
    private boolean won;

    // Constructor
    tile_driver(){
        // By default, the game will be 5x5 with 8 traps
        num_row = 5;
        num_col = 5;
        num_traps = 8;

        // Initiate timer to 00:00
        seconds = 0;
        minutes = 0;

        // Initiate number of clicks
        clicked = 0;

        // Set up initial grid
        grid = new tile[num_row][num_col];
        for(int i = 0; i < num_row; i++){
            for(int j = 0; j < num_col; j++){
                grid[i][j] = new tile(i,j);

            }
        }

        // Set up container and panels
        game = new JFrame();
        c = new Container();
        game_panel = new JPanel();
        menu_panel = new JPanel();
        c = game.getContentPane();

        // Format game panel
        game_panel.setLayout(new GridLayout(num_row, num_col));

        // Set up buttons
        for(int i = 0; i < num_row; i++){
            for(int j = 0; j < num_col; j++){
                JButton curr_button = grid[i][j].getButton();
                curr_button.setName(i + ":" + j);
                curr_button.addActionListener(this);
                game_panel.add(curr_button);
            }
        }

        // Create timer
        disp_time = new JButton("Timer");
        format_time = minutes + ":" + String.format("%02d", seconds);
        disp_time.setText(format_time);
        menu_panel.add(disp_time);
        ActionListener time = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // If seconds is less than 59, incrememnt minutes
                if(seconds < 59){
                    seconds++;
                }
                // If seconds will be 60, increment minutes and reset seconds
                else{
                    seconds = 0;
                    minutes++;
                }
                // Set new time
                format_time = minutes + ":" + String.format("%02d", seconds);
                disp_time.setText(format_time);
                // Redraw timer
                c.revalidate();
                c.repaint();
            }
        };
        timer = new Timer(1000, time);

        // Make menu buttons
        start = new JButton("Start");
        start.addActionListener(this);
        help = new JButton("Help");
        help.addActionListener(this);
        quit = new JButton("Quit");
        quit.addActionListener(this);
        restart = new JButton("Restart");
        restart.addActionListener(this);

        // Create the menu bar for settings
        menu_bar = new JMenuBar();
        settings = new JMenu("Settings");
        //settings_sub = new JMenu();
        default_settings = new JMenuItem("Default game");
        custom = new JMenu("Custom Settings");
        easy = new JMenuItem("Easy");
        medium = new JMenuItem("Medium");
        hard = new JMenuItem("Hard");


        // Add menu items to menus
        settings.add(default_settings);
        custom.add(easy);
        custom.add(medium);
        custom.add(hard);
        settings.add(custom);
        menu_bar.add(settings);
        game.setJMenuBar(menu_bar);

        // Add regular menu elements to menu panel
        menu_panel.add(start);
        menu_panel.add(help);
        menu_panel.add(quit);
        menu_panel.add(restart);

        // Add menus to container
        c.add(menu_panel, BorderLayout.NORTH);
        c.add(game_panel, BorderLayout.CENTER);

        // Make everything visible
        game.pack();
        game.setVisible(true);

        // Set running to false
        running = false;
        won = false;

    }

    public int checkTrapNeighbors(tile curr_tile){
        // Accumulator for the number of neighbors that are traps
        int curr_num_traps = 0;
        // Array list of coordinates of all neighbors
        ArrayList<ArrayList<Integer>> neighbor = curr_tile.getNeighbor();

        // Loop through each neighbor
        for(int i = 0; i < neighbor.size();i++){
            // If either value is out of bounds, ignore the neighbor
            if((neighbor.get(i).get(0) < 0 || neighbor.get(i).get(0) >= num_row) ||
                    (neighbor.get(i).get(1) < 0 || neighbor.get(i).get(1) >= num_col)){

            }
            // Otherwise, check if they are a trap and accumulate the total number of traps
            else{
                // If the tile is a trap, accumulate
                if(grid[neighbor.get(i).get(0)][neighbor.get(i).get(1)].getTrap()){
                    curr_num_traps++;
                }
            }
        }

        return curr_num_traps;
    }

    public void startGame(){
        // Randomly set the traps
        int traps_made = 0;
        while(traps_made < num_traps){
            int random_x = (int)Math.floor(Math.random()*(num_col));
            int random_y = (int)Math.floor(Math.random()*(num_row));

            // Check if the tile is already a tile
            if(!grid[random_x][random_y].getTrap()) {
                grid[random_x][random_y].setTrap();
                traps_made++;
            }

            // Assign each tiles number of neighbors that are traps
            for(int i = 0; i < num_row; i++){
                for(int j = 0; j < num_col; j++){
                    int curr_traps = checkTrapNeighbors(grid[i][j]);
                    grid[i][j].setNeighborTraps(curr_traps);
                }
            }
        }
    }

    // Actions performed
    public void actionPerformed(ActionEvent e){
        // Get text of the button clicked
        JButton input = (JButton) e.getSource();
        String input_text = input.getText();

        if(input_text.equals("Start")){
            // If the user hits start, start the timer and set running to true
            timer.start();
            running = true;

            // Set the number of buttons clicked to 0
            clicked = 0;

            // Start game
            startGame();
        }

        else if(input_text.equals("Help")){
            // Display a help popup
        }
        else if(input_text.equals("Quit")){
            // Clean up memory and quit
            System.gc();
            System.exit(0);
        }
        else if(input_text.equals("Restart")){
            // If the user clicks restart, reset the game
        }
        else if(running){
            // Get name of the tile clicked
            JButton curr_tile = (JButton) e.getSource();
            String tile_name = input.getName();

            // Convert the tile name to coordinates
            String[] split_name = tile_name.split(":");
            int x = Integer.parseInt(split_name[0]);
            int y = Integer.parseInt(split_name[1]);

            // If this is the first click and it's a trap, change the
            // trap location
            if(clicked == 0 && grid[x][y].getTrap()){
                grid[x][y].setTrap();
                boolean new_trap = false;
                while(!new_trap){
                    int random_x = (int)Math.floor(Math.random()*(num_col));
                    int random_y = (int)Math.floor(Math.random()*(num_row));

                    // Check if the tile is already a tile
                    if(!grid[random_x][random_y].getTrap() && (random_x != x || random_y != y)) {
                        grid[random_x][random_y].setTrap();
                        new_trap = true;
                    }

                    // Reset number of neighbor traps
                    for(int i = 0; i < num_row; i++){
                        for(int j = 0; j < num_col; j++){
                            int curr_traps = checkTrapNeighbors(grid[i][j]);
                            grid[i][j].setNeighborTraps(curr_traps);
                        }
                    }
                }
            }

            // Check if the tile is a thermal trap
            if(grid[x][y].getTrap()){
                // Lose the game
                running = false;
                timer.stop();
                // Show all bombs
                for(int i = 0; i < num_row; i++){
                    for(int j = 0; j < num_col; j++) {
                        if(grid[i][j].getTrap()){
                            grid[i][j].showTrap();
                        }
                    }
                }
            }
            // If it isn't a trap, flip the tile
            else{
                int curr_traps = grid[x][y].getNumNeighborTraps();
                String curr_num = String.valueOf(curr_traps);
                grid[x][y].getButton().setText(curr_num);
                grid[x][y].getButton().setBackground(Color.white);

                // Visit neighbors if number of traps is 0
            }
        }
        // Redraw everything
        c.revalidate();
        c.repaint();
    }

    public static void main(String args[]){
        tile_driver curr_game = new tile_driver();
        curr_game.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e)
                    {
                        System.exit(0);
                    }
                }
        );
    }
}
