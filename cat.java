import org.json.JSONObject;
import java.util.*;

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

    // Function to convert a number in a given base to decimal
    public static int convertToDecimal(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Function to simulate secret sharing
    public static void operation(int N, int K, Map<Integer, Map<String, String>> points) {
        System.out.println("Secret is divided into " + N + " Parts:");

        List<int[]> pointList = new ArrayList<>();

        // Parse points from the JSON-like input
        for (int i = 1; i <= N; i++) {
            Map<String, String> pointData = points.get(i);
            String baseStr = pointData.get("base");
            String valueStr = pointData.get("value");
            int base = Integer.parseInt(baseStr);
            int value = convertToDecimal(valueStr, base);
            pointList.add(new int[]{i, value});  // Store as (x, f(x))
            System.out.println(i + " -> " + value);  // Print (x, f(x))
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
            x[i] = pointList.get(i)[0];
            y[i] = pointList.get(i)[1];
        }

        // Reconstruct the secret (constant term) using Lagrange interpolation
        System.out.println("The secret (constant term) is: " + generateSecret(x, y, M));
    }

    public static void main(String[] args) {
        // Example JSON input as a string
        String jsonString = "{"
            + "\"keys\": {\"n\": 4, \"k\": 3},"
            + "\"1\": {\"base\": \"10\", \"value\": \"4\"},"
            + "\"2\": {\"base\": \"2\", \"value\": \"111\"},"
            + "\"3\": {\"base\": \"10\", \"value\": \"12\"},"
            + "\"6\": {\"base\": \"4\", \"value\": \"213\"}"
            + "}";

       
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<Integer, Map<String, String>> points = new HashMap<>();

        
        int N = jsonObject.getJSONObject("keys").getInt("n");
        int K = jsonObject.getJSONObject("keys").getInt("k");

       
        for (int i = 1; i <= N; i++) {
            points.put(i, jsonObject.getJSONObject(String.valueOf(i)).toMap());
        }

       
        operation(N, K, points);
    }
}
