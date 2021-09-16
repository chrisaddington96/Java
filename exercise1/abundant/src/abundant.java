import java.io.*;

public class abundant {
    public static int abundant(int in_num){
        int abundance = 1; // Variable to store the abundance of the input number

        // Check every number from 2 to (in_num-1)/2
        for(int i = 2; i <= in_num/2; i++){
            // Check if i is a proper divisor of the input number
            if(in_num % i == 0){
                // If the number is a proper divisor, add it to the abundance
                abundance += i;
            }
        }

        return abundance;
    }

    public static void main( String args[] ) throws IOException{
        int num_limit = 10000; // Store the number limit for portability
        int num_abundant = 0; // Store the number of abundant numbers
        int smallest_num = 2; // Store the smallest number with the highest abundance
        int highest_abundance = 2; // Store the highest abundance of the smallest number

        // Check every number between 3 and num_limit-1
        for(int i = 3; i < num_limit; i++){

            // Store the abundance of the current number
            int curr_abundance = abundant(i);

            // Check if the number is actually abundant
            if(curr_abundance > i){
                // If the number is abundant, increment num_abundant and check if the abundance
                // is higher than the current highest
                num_abundant++;
                if(curr_abundance > highest_abundance){
                    // If the current abundance is higher than previous largest, reassign it
                    smallest_num = i;
                    highest_abundance = curr_abundance;
                }
            }
        }
        // Print out the number of abundant numbers and the smallest number with the highest abundance
        System.out.println("There are " + num_abundant + " abundant numbers less than " + num_limit);
        System.out.println("The smallest abundant number (less than " + num_limit + ") with highest abundance is " + smallest_num);

    }
}
