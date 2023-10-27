import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class Main {
    static int n = 10; // Number of approximations to calculate
    public static void main(String[] args) {
        ArrayList<BigDecimal> fractions = continuedFraction();

        BigDecimal num1 = BigDecimal.ZERO;
        BigDecimal num2 = BigDecimal.ONE;
        BigDecimal den1 = BigDecimal.ONE;
        BigDecimal den2 = BigDecimal.ZERO;

        for (int i = 0; i < n; i++) {
            BigDecimal a = fractions.get(i);
            BigDecimal num = a.multiply(num1).add(num2);
            BigDecimal den = a.multiply(den1).add(den2);

            System.out.printf("Approximation %d: %s/%s \n", i + 1, den, num);

            num2 = num1;
            num1 = num;
            den2 = den1;
            den1 = den;
        }
        for (int j = n-1; j >= 0; j--) {
            BigDecimal alpha = BigDecimal.ZERO;
            for (int i = j; i >= 0; i--) {
                if (i == 0) {
                    alpha = alpha.add(fractions.get(i));
                    break;
                }
                alpha = BigDecimal.ONE.divide(alpha.add(fractions.get(i)), 50, BigDecimal.ROUND_HALF_EVEN);
            }
            System.out.printf("Approximation %d: %s \n", j + 1, alpha);
            System.out.printf("Abs value %d: %s \n", j + 1, Math.abs(alpha.subtract(new BigDecimal(Math.PI, MathContext.DECIMAL128)).doubleValue()));
        }
    }

    public static ArrayList<BigDecimal> continuedFraction() {
        BigDecimal pi = new BigDecimal("3.1415926", MathContext.DECIMAL128);
        BigDecimal a0 = pi.setScale(0, BigDecimal.ROUND_DOWN);
        ArrayList<BigDecimal> fractions = new ArrayList<>();
        fractions.add(a0);
        a0 = pi.subtract(a0);
        for (int i = 1; i < n; i++) {
            if (a0.equals(BigDecimal.ZERO)) {
                n = i;
                break;
            }
            BigDecimal ai = BigDecimal.ONE.divide(a0, MathContext.DECIMAL128);
            BigDecimal temp = ai.setScale(0, BigDecimal.ROUND_DOWN);
            if (temp.equals(BigDecimal.ZERO)) {
                n = i;
                break;
            }
            fractions.add(temp);
            a0 = ai.subtract(temp);
        }

        return fractions;
    }
}