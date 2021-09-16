import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class tweetbot extends JFrame implements ActionListener{
    // Create panel, text field, and container within panel
    private JPanel entry;
    private JPanel keyboard;
    private JPanel top;
    private JPanel mid;
    private JPanel bottom;
    private JPanel extra;
    private Container c;
    private BorderLayout layout;
    private JTextField text_entry;

    // Create rows keyboard
    private char top_row[] = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'};
    private char mid_row[] = {'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'};
    private char bottom_row[] = {'z', 'x', 'c', 'v', 'b', 'n', 'm'};

    // Constructor
    public tweetbot(){
        super("tweetbot");

        // Instantiate JPanels
        entry = new JPanel();
        keyboard = new JPanel();
        top = new JPanel();
        mid = new JPanel();
        bottom = new JPanel();
        extra = new JPanel();

        // Get content pane
        keyboard.setLayout(new GridLayout(5,1));
        c = getContentPane();

        // Add entry bar button
        entry.add(new JButton("+"));
        text_entry = new JTextField("Type a message...");
        entry.add(text_entry);
        JButton send = new JButton("Send");
        entry.add(send);
        send.addActionListener(this);

        // Add top row chars
        for(int i = 0; i < top_row.length; i++){
            JButton curr_button = new JButton(String.valueOf(top_row[i]));
            curr_button.addActionListener(this);
            top.add(curr_button);
        }

        // Add mid row chars
        for(int i = 0; i < mid_row.length; i++){
            JButton curr_button = new JButton(String.valueOf(mid_row[i]));
            curr_button.addActionListener(this);
            mid.add(curr_button);
        }

        // Add caps button
        bottom.add(new JButton("CAPS"));

        // Add bottom row chars
        for(int i = 0; i < bottom_row.length; i++){
            JButton curr_button = new JButton(String.valueOf(bottom_row[i]));
            curr_button.addActionListener(this);
            bottom.add(curr_button);
        }

        // Add delete button
        JButton delete = new JButton("DEL");
        bottom.add(delete);
        delete.addActionListener(this);

        // Add extra buttons
        extra.add(new JButton("123"));
        JButton emoji = new JButton(":)");
        extra.add(emoji);
        emoji.addActionListener(this);
        extra.add(new JButton("mic"));
        JButton space = new JButton("               SPACE               ");
        extra.add(space);
        space.addActionListener(this);
        extra.add(new JButton("Return"));

        // Draw panels
        keyboard.add(entry);
        keyboard.add(top);
        keyboard.add(mid);
        keyboard.add(bottom);
        keyboard.add(extra);
        c.add(keyboard, BorderLayout.CENTER);

        // Format window and make visible
        pack();
        setVisible(true);
    }

    // Action Performed
    public void actionPerformed (ActionEvent e){
        // Get text of the button clicked
        JButton input = (JButton) e.getSource();
        String input_text = input.getText();

        // If the initial message is displayed, clear it
        if(text_entry.getText().equals("Type a message...")){
            text_entry.setText("");
        }

        // Deal with button text
        // If the input text is one char long, is a number input
        if(input_text.length() == 1){
            // Append the input to the current string
            String curr_string = text_entry.getText();
            curr_string += input_text;
            text_entry.setText(curr_string);
        }
        // If the input is send, write the text to the console and clear the text entry
        else if(input_text.equals("Send")){
            System.out.print(text_entry.getText() + "\n");
            text_entry.setText("");
        }
        // If the input is DEL, delete the last character in the string
        else if(input_text.equals("DEL")){
            String curr_string = text_entry.getText();
            // If the text entry is not NULL or blank, delete the last character
            if(curr_string != null && curr_string.length() > 0) {
                curr_string = curr_string.substring(0, curr_string.length() - 1);
                text_entry.setText(curr_string);
            }
        }
        // If the input is space, add a space to the string
        else if(input_text.equals("               SPACE               ")){
            String curr_string = text_entry.getText();
            curr_string += " ";
            text_entry.setText(curr_string);
        }
        // If the input is :), add :) to the entry field
        else if(input_text.equals(":)")){
            // Append the input to the current string
            String curr_string = text_entry.getText();
            curr_string += ":-)";
            text_entry.setText(curr_string);
        }
        // Redraw the text_entry field
        text_entry.revalidate();
        text_entry.repaint();
    }

    // Driver for the tweetbot
    public static void main(String args[]){
        tweetbot keyboard = new tweetbot();
        keyboard.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                });
    }
}