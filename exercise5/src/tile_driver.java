import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.Timer;
import java.util.Stack;

public class tile_driver extends JFrame implements ActionListener {
    // Class variables
    // Integers to define the number of tiles in a row and column
    private int num_row;
    private int num_col;

    // Integer to hold the number of thermal traps in a game, the number of tiles flipped,
    // and the number of tiles required to win
    private int num_traps;
    private int disp_traps;
    private int num_flipped;
    private int win_num;

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

    // Help pop up to give game rules
    private JPopupMenu help_popup;
    private JLabel help_info;

    // Button to allow the user to flag for mines
    private JButton flag;
    private boolean flagging;
    private JButton display_traps;

    // Settings menu elements
    private JMenuBar menu_bar;
    private JMenu settings;
    private JMenu settings_sub;
    private JMenuItem default_settings;
    private JMenu preset;
    //private JMenu custom;
    private JMenuItem set_custom;
    private JMenuItem easy;
    private JMenuItem medium;
    private JMenuItem hard;
    /*
    // Custom game sliders
    private JPopupMenu custom_popup;
    private JSlider col_slider;
    private JSlider row_slider;
    private JSlider trap_slider;
    */
    // Boolean for if the game is running
    private boolean running;
    // Boolean for if you won or not
    private boolean won;

    // Custom game settings
    // Easy
    private int easy_row = 4;
    private int easy_col = 4;
    private int easy_trap = 5;

    // Medium
    private int med_row = 8;
    private int med_col = 8;
    private int med_trap = 14;

    // Hard
    private int hard_row = 15;
    private int hard_col = 15;
    private int hard_trap = 60;

    // Default
    private int def_row = 5;
    private int def_col = 5;
    private int def_trap = 8;

    // Constructor
    tile_driver(){
        // By default, the game will be 5x5 with 8 traps
        num_row = 5;
        num_col = 5;
        num_traps = 8;
        disp_traps = num_traps;
        num_flipped = 0;
        win_num = (num_col * num_row) - num_traps;

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
        game.setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        // Make flag button and set flagging to false
        flag = new JButton("Flag");
        flag.addActionListener(this);
        flagging = false;
        display_traps = new JButton("Traps left: " + disp_traps);

        // Make pop up menu and label
        help_popup = new JPopupMenu();
        help_info = new JLabel();
        help_popup.add("Click start to start a game.");
        help_popup.add("Click restart to start a new game.");
        help_popup.add("Click quit to quit the game.");
        help_popup.add("To flag a tile, click flag then click a tile. Click flag again to stop flagging tiles.");
        help_popup.add("When changing settings, the game will automatically restart.");
        help_popup.add("Easy : 4x4 with 5 traps.");
        help_popup.add("Medium : 8x8 with 14 traps.");
        help_popup.add("Hard : 15x15 with 60 traps.");
        help_popup.add("Default : 5x5 with 8 traps.");
        help_popup.add(help_info);
        help_popup.setPopupSize(600,600);

        // Create the menu bar for settings
        menu_bar = new JMenuBar();
        settings = new JMenu("Settings");
        //settings_sub = new JMenu();
        default_settings = new JMenuItem("Default game");
        default_settings.addActionListener(this);
        preset = new JMenu("Preset Games");
        /*
        custom = new JMenu("Custom Settings");
        row_slider = new JSlider(4,15);
        col_slider = new JSlider(4,15);
        trap_slider = new JSlider(5, 75);
        set_custom = new JMenuItem("Set Custom Settings");
        set_custom.addActionListener(this);
        */
        easy = new JMenuItem("Easy");
        easy.addActionListener(this);
        medium = new JMenuItem("Medium");
        medium.addActionListener(this);
        hard = new JMenuItem("Hard");
        hard.addActionListener(this);

        // Add menu items to menus
        preset.add(default_settings);
        preset.add(easy);
        preset.add(medium);
        preset.add(hard);
        /*
        custom.add(row_slider);
        custom.add(col_slider);
        custom.add(trap_slider);
        custom.add(set_custom);
        settings.add(custom);
        // Custom game sliders
        col_slider = new JSlider();
        row_slider = new JSlider();
        trap_slider = new JSlider();
        */
        settings.add(preset);
        menu_bar.add(settings);
        game.setJMenuBar(menu_bar);

        // Add regular menu elements to menu panel
        menu_panel.add(start);
        menu_panel.add(help);
        menu_panel.add(quit);
        menu_panel.add(restart);
        menu_panel.add(flag);
        menu_panel.add(display_traps);

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

    // Function to check neighboring tiles to see how many are traps. Returns
    // the number so it can be assigned to the current tile.
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

    // Function to clear neighboring tiles. Is called when the user clicks a tile
    // with 0 neighboring traps, and cascades through neighboring tiles that also
    // have 0 neighboring traps.
    public void clearTiles(tile start_tile){
        // Make a stack of tiles with 0 trap neighbors
        Stack<tile> tile_stack = new Stack<tile>();
        tile_stack.push(start_tile);

        // Make an arraylist of visitied tiles
        ArrayList<tile> visited = new ArrayList<tile>();

        // While to stack isn't empty, check the tile and it's neighbors
        while(tile_stack.size() > 0){
            // Check the last added tile
            tile init_tile = tile_stack.pop();

            // Flip the tile
            int x = init_tile.getX();
            int y = init_tile.getY();
            // If the tile wasn't already flipped, increment flipped counter
            if(!grid[x][y].getFlipped()){
                num_flipped++;
            }
            grid[x][y].setFlipped();

            // Get the array list of the init_tile's neighbors
            ArrayList<ArrayList<Integer>> neighbors = init_tile.getNeighbor();

            // Check every neighbor
            for(int i = 0; i < neighbors.size(); i++){
                x = neighbors.get(i).get(0);
                y = neighbors.get(i).get(1);

                // If the neighbor is in bounds
                if((x >= 0 && x < num_col) && (y >= 0 && y < num_row)){
                    tile curr_tile = grid[x][y];
                    // If the neighbor hasn't been visited and isn't a trap or bombed
                    if(!visited.contains(curr_tile) && !curr_tile.getFlagged() && !curr_tile.getTrap()){
                        // Flip the tile and add it to the stack
                        // If the tile wasn't already flipped, increment flipped counter
                        if(!grid[x][y].getFlipped()){
                            num_flipped++;
                        }
                        curr_tile.setFlipped();
                        visited.add(curr_tile);
                        if(curr_tile.getNumNeighborTraps() == 0) {
                            tile_stack.push(curr_tile);
                        }
                    }
                }
            }
        }
    }

    // Function to start the game by assigning traps randomly.
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

    // Function to reset game board
    public void reset(){
        // For each row
        for(int i = 0; i < num_row; i++){
            // For each column
            for(int j = 0; j< num_col; j++){
                // Remove old button
                game_panel.remove(grid[i][j].getButton());

                // Reset tile
                grid[i][j] = new tile(i,j);

                // Add new button the game panel
                JButton curr_button = grid[i][j].getButton();
                curr_button.setName(i + ":" + j);
                curr_button.addActionListener(this);
                game_panel.add(curr_button);
            }
        }
    }

    public void deleteGrid(){
        // For each row
        for(int i = 0; i < num_row; i++) {
            // For each column
            for (int j = 0; j < num_col; j++) {
                // Remove old button
                game_panel.remove(grid[i][j].getButton());
            }
        }
    }

    public void newGame(){
        // Set new win number
        win_num = (num_col * num_row) - num_traps;

        // Format game panel
        game_panel.setLayout(new GridLayout(num_row, num_col));

        // Initialize new grid
        grid = new tile[num_row][num_col];

        // For each row
        for(int i = 0; i < num_row; i++){
            // For each column
            for(int j = 0; j< num_col; j++){
                // Reset tile
                grid[i][j] = new tile(i,j);

                // Add new button the game panel
                JButton curr_button = grid[i][j].getButton();
                curr_button.setName(i + ":" + j);
                curr_button.addActionListener(this);
                game_panel.add(curr_button);

                c.remove(game_panel);
                c.add(game_panel);
                game.pack();
            }
        }
    }

    // Check if the game was won
    public boolean checkWin(){
        // If the user has clicked all of the non trap tiles, they win
        if(num_flipped >= win_num){
            won = true;
            running = false;
        }
        return won;
    }

    // Actions performed
    public void actionPerformed(ActionEvent e){
        // Get text of the button clicked
        String input_text = e.getActionCommand();

        if(input_text.equals("Start")){
            // Game only starts if not currently running
            if(!running) {
                // If the user hits start, start the timer and set running to true
                timer.start();
                running = true;

                // Set the number of buttons clicked to 0
                clicked = 0;

                // Reset flag button
                flagging = false;
                flag.setBackground(null);

                // Start game
                startGame();
            }
        }

        else if(input_text.equals("Help")){
            // Display a help popup
            help_popup.show(c, 400, 100);
        }
        else if(input_text.equals("Quit")){
            // Clean up memory and quit
            System.gc();
            System.exit(0);
        }
        else if(input_text.equals("Restart")){
            // If the user clicks restart, reset the game
            timer.stop();
            seconds = 0;
            minutes = 0;
            running = false;
            clicked = 0;
            num_flipped = 0;
            won = false;
            disp_traps = num_traps;
            display_traps.setText("Traps left: " + disp_traps);

            // Reset the grid
            reset();
            c.revalidate();
            c.repaint();
        }
        else if(input_text.equals("Flag")){
            // If flagging is off, turn it on and make button red
            if(!flagging){
                flagging = true;
                flag.setBackground(Color.RED);
            }

            // If flagging is on, turn it off and make button default color
            else{
                flagging = false;
                flag.setBackground(null);
            }

        }
        else if(input_text.equals("Default game")){
            // Delete the current grid
            deleteGrid();

            // Restart the game with the default settings
            num_row = def_row;
            num_col = def_col;
            num_traps = def_trap;

            // If the user clicks restart, reset the game
            timer.stop();
            seconds = 0;
            minutes = 0;
            running = false;
            clicked = 0;
            num_flipped = 0;
            won = false;
            disp_traps = num_traps;
            display_traps.setText("Traps left: " + disp_traps);

            // Reset the grid
            newGame();

            c.revalidate();
            c.repaint();

        }
        else if(input_text.equals("Easy")){
            // Delete the grid
            deleteGrid();
            // Restart the game with the default settings
            num_row = easy_row;
            num_col = easy_col;
            num_traps = easy_trap;

            // reset the game
            timer.stop();
            seconds = 0;
            minutes = 0;
            running = false;
            clicked = 0;
            num_flipped = 0;
            won = false;
            disp_traps = num_traps;
            display_traps.setText("Traps left: " + disp_traps);

            // Reset the grid
            newGame();

            c.revalidate();
            c.repaint();
        }
        else if(input_text.equals("Medium")){
            // Delete the grid
            deleteGrid();
            // Restart the game with the default settings
            num_row = med_row;
            num_col = med_col;
            num_traps = med_trap;

            // reset the game
            timer.stop();
            seconds = 0;
            minutes = 0;
            running = false;
            clicked = 0;
            num_flipped = 0;
            won = false;
            disp_traps = num_traps;
            display_traps.setText("Traps left: " + disp_traps);

            // Reset the grid
            newGame();

            c.revalidate();
            c.repaint();
        }
        else if(input_text.equals("Hard")){
            // Delete the grid
            deleteGrid();
            // Restart the game with the default settings
            num_row = hard_row;
            num_col = hard_col;
            num_traps = hard_trap;

            // reset the game
            timer.stop();
            seconds = 0;
            minutes = 0;
            running = false;
            clicked = 0;
            num_flipped = 0;
            won = false;
            disp_traps = num_traps;
            display_traps.setText("Traps left: " + disp_traps);

            // Reset the grid
            newGame();

            c.revalidate();
            c.repaint();
        }
        /*
        else if(input_text.equals("Set Custom Settings")){
            // Delete the grid
            deleteGrid();
            // Restart the game with the default settings
            num_row = row_slider.getValue();
            num_col = col_slider.getValue();
            num_traps = trap_slider.getValue();

            // reset the game
            timer.stop();
            seconds = 0;
            minutes = 0;
            running = false;
            clicked = 0;
            num_flipped = 0;
            won = false;
            disp_traps = num_traps;
            display_traps.setText("Traps left: " + disp_traps);

            // Reset the grid
            newGame();

            c.revalidate();
            c.repaint();
        }

        */
        else if(running){
            // Get name of the tile clicked
            JButton input = (JButton) e.getSource();
            String tile_name = input.getName();

            // Convert the tile name to coordinates
            String[] split_name = tile_name.split(":");
            int x = Integer.parseInt(split_name[0]);
            int y = Integer.parseInt(split_name[1]);

            // Toggle the flag, and if it is now flagged, decrement disp_trap. If
            // it's not flagged now, increment disp_trap
            if(flagging){
                grid[x][y].setFlagged();
                if(grid[x][y].getFlagged()){
                    if (disp_traps > 0){
                        disp_traps--;
                    }
                }
                else{
                    disp_traps++;
                }
                display_traps.setText("Traps left: " + disp_traps);
            }
            else{
                if(!grid[x][y].getFlagged()){
                    // If this is the first click and it's a trap, change the
                    // trap location
                    if(clicked == 0 && grid[x][y].getTrap()){
                        num_flipped++;
                        grid[x][y].unsetTrap();
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
                        // Flip this tile
                        grid[x][y].setFlipped();
                    }

                    // Check if the tile is a thermal trap
                    else if(grid[x][y].getTrap()){
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
                    else if(!grid[x][y].getFlipped()){
                        num_flipped++;
                        int curr_traps = grid[x][y].getNumNeighborTraps();
                        // If the number of trap neighbors is greater than 0, flip the tile
                        if(curr_traps > 0) {
                            grid[x][y].setFlipped();
                        }
                        // Visit neighbors if number of traps is 0
                        else{
                            // Clear neighbor tiles that have 0 trap neighbors
                            clearTiles(grid[x][y]);
                        }
                    }
                    clicked++;
                }
            }
        }
        // Redraw everything
        c.revalidate();
        c.repaint();

        // Check if the user won
        checkWin();
        System.out.print(win_num + " : " + num_flipped + "\n");

        // If the user won, stop the timer and display the win message
        if(won){
            timer.stop();
            JOptionPane.showMessageDialog(c, "You won! Press restart to play again!");
        }

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
