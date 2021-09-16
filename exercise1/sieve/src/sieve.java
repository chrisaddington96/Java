import java.io.*;
import java.util.Arrays;

public class sieve {
    public static void main( String args[] ) throws IOException{
        int num_terms = 10000; // Number of terms to check
        boolean[] primes = new boolean[num_terms]; // Array to hold whether it's index is prime or not
        int num_primes = 0; // Number of primes
        Arrays.fill(primes, true); // Initialize every element to true

        // Set index 0 and 1 to false
        primes[0] = false;
        primes[1] = false;

        // Start checking for primes
        for(int i = 2; i < num_terms; i++){
            // If the number is prime, change its multiples to false
            if(primes[i]){
                for(int j = 2; j*i < num_terms; j++){
                    primes[j*i] = false;
                }
            }
        }
        // Print all primes
        for(int i = 2; i < num_terms; i++){
            if(primes[i]) {
                System.out.print(i + ", ");
                num_primes++;
            }
        }
        // Print the number of primes
        System.out.println("\nThere are " + num_primes + " primes less than " + num_terms);
    }
}
