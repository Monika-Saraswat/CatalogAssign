import java.util.ArrayList;
import java.util.List;

public class Main {

    // Class to represent a fraction
    static class Fraction {
        int num, den;

        public Fraction(int n, int d) {
            this.num = n;
            this.den = d;
        }

        // Function to reduce the fraction
        public void reduce() {
            int gcd = gcd(num, den);
            num /= gcd;
            den /= gcd;
        }

        // Multiply two fractions
        public Fraction multiply(Fraction f) {
            Fraction result = new Fraction(num * f.num, den * f.den);
            result.reduce();
            return result;
        }

        // Add two fractions
        public Fraction add(Fraction f) {
            Fraction result = new Fraction(num * f.den + den * f.num, den * f.den);
            result.reduce();
            return result;
        }
    }

    // Function to compute the Greatest Common Divisor (GCD)
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Function to perform Lagrange interpolation and find the constant term (secret)
    public static int generateSecret(int[] x, int[] y, int M) {
        Fraction ans = new Fraction(0, 1); // Initialize the result to zero (fraction)

        for (int i = 0; i < M; ++i) {
            Fraction l = new Fraction(y[i], 1); // Initialize Lagrange basis term with y[i]

            for (int j = 0; j < M; ++j) {
                if (i != j) {
                    Fraction temp = new Fraction(-x[j], x[i] - x[j]);  // (x - x_j) term
                    l = l.multiply(temp);  // Multiply with the Lagrange basis
                }
            }
            ans = ans.add(l);  // Add to the final answer
        }

        return ans.num;  // Return the constant term (secret)
    }

    // Function to simulate secret sharing
    public static void operation(int N, int K, List<int[]> points) {
        System.out.println("Secret is divided into " + N + " Parts:");

        // Display the points (x, f(x)) representing the secret parts
        for (int i = 0; i < N; ++i) {
            System.out.println(points.get(i)[0] + " " + points.get(i)[1]);
        }

        // We can reconstruct the secret from any K parts (minimum threshold)
        System.out.println("We can generate the Secret from any " + K + " Parts.");

        // Input M points (M >= K) to reconstruct the secret
        int M = K;  // M should be at least K to successfully decode the secret

        if (M < K) {
            System.out.println("Points are less than threshold " + K + " Points Required");
            return;
        }

        // Arrays to store the x and y values for Lagrange interpolation
        int[] x = new int[M];
        int[] y = new int[M];

        // Copy M points from the given list of points to arrays
        for (int i = 0; i < M; ++i) {
            x[i] = points.get(i)[0];
            y[i] = points.get(i)[1];
        }

        // Reconstruct the secret (constant term) using Lagrange interpolation
        System.out.println("The secret (constant term) is: " + generateSecret(x, y, M));
    }

    public static void main(String[] args) {
        // Example parameters: 4 parts, minimum 2 parts to reconstruct the secret
        int N = 4; // Number of parts to divide the secret
        int K = 3; // Minimum number of parts required to reconstruct the secret

        // List of points (x, f(x)) based on polynomial evaluations
        // Here, each pair represents (x, f(x)) i.e., (x, secret or polynomial value)
        List<int[]> points = new ArrayList<>();
        points.add(new int[]{1, 80});  // f(1) = 10
        points.add(new int[]{2, 95});  // f(2) = 20
        points.add(new int[]{3, 110});  // f(3) = 30
        points.add(new int[]{4, 125});  // f(4) = 40

        // Perform the secret sharing operation
        operation(N, K, points);
    }
}
