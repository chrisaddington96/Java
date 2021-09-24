import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import javax.swing.*;

public class memorygame extends JFrame implements ActionListener{

    // Class variables
    // Panels to hold buttons and container to hold panels
    private JPanel gamemenu;
    private JPanel gameboard;
    private JPanel winboard;
    private Container c;

    // Key buttons for game function
    private JButton start;
    private JButton restart;
    private JButton guesses;
    private JButton correct;
    private JButton quit;
    private JButton win;

    // Global variables for maintainability
    private int num_col = 4;
    private int num_row = 4;
    private int num_images = 8;
    private String cardback = "cardback.jpeg";

    // Store the number of guesses and correct guesses
    private int total_guess = 0;
    private int correct_guess = 0;

    // ArrayLists to hold all images and buttons for each image
    private ArrayList<ImageIcon> pics;
    private ArrayList<JButton> buttons;

    // Variable to show whether the game is being played or not
    private boolean running = false;

    // Store the previous picture index and description, and the card number (1,2)
    private int card_num = 1;
    private String prev_desc = "";
    private int prev_index;

    // Store the current index and pic description
    private int curr_index;
    private String curr_desc = "";

    // Create timer for wrong guesses
    private Timer wait_time;

    // Constructor
    public memorygame(){
        super("memorygame");
        // Initialize class variables
        gamemenu = new JPanel(); // Stores start, restart, and number of guesses and correct matches
        gameboard = new JPanel(); // Stores each card
        winboard = new JPanel(); // Stores win message
        win = new JButton();
        c = new Container();
        guesses = new JButton("Guesses made: " + total_guess);
        correct = new JButton("Matches made: " + correct_guess);
        start = new JButton("Start");
        start.addActionListener(this);
        restart = new JButton("Restart");
        restart.addActionListener(this);
        quit = new JButton("Quit");
        quit.addActionListener(this);

        // Set up content pane
        gameboard.setLayout(new GridLayout(num_col, num_row));
        c = getContentPane();

        // Set up gamemenu
        gamemenu.add(start);
        gamemenu.add(restart);
        gamemenu.add(guesses);
        gamemenu.add(correct);
        gamemenu.add(quit);

        // Initialize pics and buttons
        pics = new ArrayList <ImageIcon>();
        buttons = new ArrayList <JButton>();

        // Set up timer
        wait_time = new Timer();

        // Populate pics
        for(int i = 0; i < num_images; i++) {
            // Make new image icon with current jpeg
            ImageIcon curr_image = new ImageIcon("arsenal" + (int) (i + 1) + ".jpeg");
            curr_image.setDescription("" + i);

            // Add picture twice
            pics.add(curr_image);
            pics.add(curr_image);
        }

        // Add images to buttons, add an action listener, and add to gameboard
        for(int i = 0; i < (num_col * num_row); i++){
            buttons.add(new JButton());
            (buttons.get(i)).setIcon(new ImageIcon(cardback));
            buttons.get(i).addActionListener(this);
            gameboard.add(buttons.get(i));
        }

        // Draw panels
        c.add(gamemenu, BorderLayout.NORTH);
        c.add(gameboard, BorderLayout.CENTER);

        // Make everything visible
        pack();
        setVisible(true);

        }
        // Define scheduleTask class to flip wrong cards back
        class scheduleTask extends TimerTask{
            @Override
            public void run(){
                buttons.get(prev_index).setIcon(new ImageIcon(cardback));
                buttons.get(curr_index).setIcon(new ImageIcon(cardback));
            }
        }


    public void actionPerformed(ActionEvent e){
        // Get text of the button clicked
        JButton input = (JButton) e.getSource();
        String input_text = input.getText();

        // NOTE: YOU MUST PRESS START TO START THE GAME
        // If the start button is clicked
        if(input_text.equals("Start")){
            // If the game is not running, start the game
            if(!running){
                // Set running to true and shuffle the pictures in pics
                running = true;
                Collections.shuffle(pics);
            }
            // If the game is running, do nothing
        }
        // If the restart button is clicked
        else if(input_text.equals("Restart")){
            // Try to remove winboard
            try {
                c.remove(winboard);
            }
            catch(Exception ex){
                System.out.print("Win not added\n");
            }

            // If the game is running, restart it
            if(running){
                running = false;
                for(int i = 0; i < buttons.size(); i++){
                    (buttons.get(i)).setIcon(new ImageIcon(cardback));
                }
                // reset counters
                total_guess = 0;
                correct_guess = 0;
                card_num = 1;
            }
            // If the game isn't running, do nothing
        }
        // If the user selects quit
        else if(input_text.equals("Quit")){
            // Close the game
            System.gc();
            System.exit(0);
        }
        // For any other button click, if the game is running check against the other card
        else if(running) {
            // Store the temporary description and index
            String temp_desc = ((ImageIcon) input.getIcon()).getDescription();
            int temp_index = buttons.indexOf(input);

            // If the temporary description is the cardback, flip the card
            if(temp_desc.equals(cardback)){
                buttons.get(temp_index).setIcon(pics.get(temp_index));
                // Reassign temp_desc to actual picture
                temp_desc = ((ImageIcon) input.getIcon()).getDescription();
                // Redraw GUI with changes
                c.revalidate();
                c.repaint();

                // If card number = 1, assign card info to previous card info
                if(card_num == 1){
                    prev_index = temp_index;
                    prev_desc = temp_desc;
                    //  Chnage card number to 2
                    card_num = 2;
                }
                // Else (card number is 2) assign card info to current card info
                else{
                    curr_index = temp_index;
                    curr_desc = temp_desc;

                    // If the current and previous description are equal, increment correct guess counter
                    if(curr_desc.equals(prev_desc)){
                        correct_guess++;
                    }
                    // Else (not equal) wait 3 seconds and flip cards back over
                    else{
                        wait_time.schedule(new scheduleTask(), 3000);
                    }
                    // Incrememnt total number of guesses
                    total_guess++;

                    // Change card number to 1
                    card_num = 1;
                }
            }
        }
        // Reset number of guesses
        guesses.setText("Guesses made: " + total_guess);
        correct.setText("Correct guesses: " + correct_guess);

        // Detect win state
        if(correct_guess == num_images){
            // Display how many guesses it took, and how to restart
            win.setText("Congratulations! It took you " + total_guess + " guesses, click restart to play again");
            winboard.add(win);
            c.add(winboard, BorderLayout.SOUTH);
        }

        // Redraw GUI with changes
        c.revalidate();
        c.repaint();
    }

    // Main driver
    public static void main(String args[]){
        memorygame curr_game = new memorygame();
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
