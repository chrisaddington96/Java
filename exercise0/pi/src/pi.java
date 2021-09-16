import java.io.*;

public class pi{
    public static boolean check_8_digits(double pi_approx){
        boolean is_precise = false;
        // If the approximation is precise enough, return true
        if(pi_approx >= 3.14159265 && pi_approx < 3.14159266){
            is_precise = true;
        }
        return is_precise;
    }

    public static void main( String args[] ) throws IOException {
        int num_terms = 1; // Number of terms checked
        double series = 0; // Current series value for pi approximation
        double pi_approx = 0; // Current pi approximation
        int i = 1; // Increment variable

        // Continue while pi approximation is too imprecise
        while(!(check_8_digits(pi_approx))){
            // If i is even, the term is negative
            if(i%2 == 0){
                series -= 1.0 / (2 * i - 1);
            }
            // Otherwise, the term is positive
            else{
                series += 1.0 / (2 * i - 1);
            }

            // Re-calculate pi approximation
            pi_approx = series * 4;
            i++; // Increment i
            num_terms++; // Increment num_terms
        }

        // Print out the number of terms required and the approximation
        System.out.println("Terms needed: " + num_terms);
        System.out.println("Pi approximation: " + pi_approx);
    }
}
