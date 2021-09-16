import java.io.*;
import java.lang.Math;

public class dice {
    public static int die_roll(int max_die_roll){
        // Randomly select a number between 1 and max_die_roll
        int my_number = (int)(Math.random() * max_die_roll) + 1;

        // Return the random number
        return my_number;
    }

    public static void main( String args[] ) throws IOException {
        int max_out = 0; // Number of max out rolls
        int D12; // D12 roll
        int D20; // D20 roll
        int start_num = 100; // Starting number of rolls
        int end_num = 100000; // Ending number of rolls
        int roll_inc = 10; // Increment value for number of rolls

        // Continue running up to and equal the number of total rolls desired
        while(start_num <= end_num) {
            // Roll each die i times
            for (int i = 0; i < start_num; i++) {
                // Roll each die
                D12 = die_roll(12);
                D20 = die_roll(20);

                // Check if both rolled max value
                if ((D12 == 12) & (D20 == 20)) {
                    max_out++;
                }
            }
            // Print out results table
            System.out.println(start_num + ": " + max_out + " max out");

            // Increment start_num by roll increment
            start_num = start_num * roll_inc;
        }
    }
}
