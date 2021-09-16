import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class textmessage extends JFrame implements ActionListener{
    // Create array list of buttons
    // private ArrayList <JButton> button;

    // Create panel and container within panel
    private JPanel entry;
    private JPanel keyboard;
    private JPanel top;
    private JPanel mid;
    private JPanel bottom;
    private JPanel extra;
    private Container c;
    private BorderLayout layout;

    // Create rows keyboard
    private char top_row[] = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'};
    private char mid_row[] = {'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'};
    private char bottom_row[] = {'z', 'x', 'c', 'v', 'b', 'n', 'm'};

    // Constructor
    public textmessage(){
        super("Text message screen");

        // Instantiate textPanels
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
        entry.add(new JTextField("Type a message..."));
        entry.add(new JButton("Send"));

        // Add top row chars
        for(int i = 0; i < top_row.length; i++){
            top.add(new JButton(String.valueOf(top_row[i])));
        }

        // Add mid row chars
        for(int i = 0; i < mid_row.length; i++){
            mid.add(new JButton(String.valueOf(mid_row[i])));
        }

        // Add caps button
        bottom.add(new JButton("CAPS"));
        // Add bottom row chars
        for(int i = 0; i < bottom_row.length; i++){
            bottom.add(new JButton(String.valueOf(bottom_row[i])));
        }
        // Add delete button
        bottom.add(new JButton("DEL"));

        // Add extra buttons
        extra.add(new JButton("123"));
        extra.add(new JButton(":)"));
        extra.add(new JButton("mic"));
        extra.add(new JButton("               SPACE               "));
        extra.add(new JButton("Return"));

        // Draw
        keyboard.add(entry);
        keyboard.add(top);
        keyboard.add(mid);
        keyboard.add(bottom);
        keyboard.add(extra);
        c.add(keyboard, BorderLayout.CENTER);

        //setSize(400,400);
        pack();
        setVisible(true);
    }

    // Action Performed
    public void actionPerformed (ActionEvent e){
        System.exit(0);
    }

    public static void main(String args[]){
        textmessage keyboard = new textmessage();
        keyboard.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                });
    }
}