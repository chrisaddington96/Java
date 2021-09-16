import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class christmas extends JFrame implements ActionListener {
    // Store the current date
    private String month;
    private String day;
    private static String christmas_month = "12";
    private static String christmas_day = "25";

    // GUI elements
    private JPanel panel;
    private Container c;
    private JButton button;
    private JLabel text;

    // Constructor
    public christmas(){
        super("Christmas checker");
        // Get current date
       MonthDay curr_date = MonthDay.now();

       // Convert date to string and split string into month and day
       String month_day = curr_date.toString();
       String[] substring = month_day.split("-");
       month = substring[2];
       day = substring[3];

       // Check if it is christmas day
       boolean is_christmas = check_christmas(month, day);

       // Build GUI
       panel = new JPanel();
       c = getContentPane();
       button = new JButton("Check if today is Christmas");
       text = new JLabel();

       // If it is christmas, display yes
       if (is_christmas){
           text.setText("Yes");
       }
       // If not, display no
       else{
           text.setText("No");
       }

       // Add elements to the panel and container
       panel.add(button);
       c.add(panel);

       // Format container and make visible
       pack();
       setVisible(true);

       // Wait for the user to click the button
       button.addActionListener(this);
    }

    // Check if the provided date matches christmas (12-25)
    public static boolean check_christmas(String month, String day){
        // Boolean to store if it is actually christmas
        boolean is_christmas = false;

        // Check if the date and month match up, if they do, it's christmas!
        if(day == christmas_day && month == christmas_month){
            is_christmas = true;
        }
        return is_christmas;
    }
    public static void main(String args[]){
        christmas check_day = new christmas();
        check_day.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent w){
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e){
        // Remove the button and add the text
        panel.remove(button);
        panel.add(text);
        panel.revalidate();
        panel.repaint();
    }
}
