import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    static int n = 7; // Number of approximations to calculate
    static int m = 53; // Number of approximations to calculate
    static BigDecimal temp = null;
    static int smaller = -1;
    static boolean stop = false;
    static int i = 0;
    static int k = 0;
    static BigDecimal alpha = new BigDecimal(Math.log(3)/Math.log(2) - 1);
    static BigDecimal ab = null;
    static int t;
    static HashMap<Integer, Integer> approx = calculateSth(alpha);
    static HashMap<Integer, BigDecimal> pq = new HashMap<>();
    static HashMap<Integer, Integer> p = new HashMap<>();
    static HashMap<Integer, BigDecimal> abs = new HashMap<>();
    static HashMap<Integer, Boolean> s = new HashMap<>();
    static HashMap<Integer, ArrayList<BigDecimal>> ver = new HashMap<>();
    public static void main(String[] args) {
//        System.out.println(alpha);
        for (k = n; k < m + 1; k++) {
            i = k;
            temp = alpha.multiply(BigDecimal.valueOf(i));
            BigDecimal tmp = temp;
            t = temp.setScale(0, RoundingMode.HALF_EVEN).intValue();
            int g = getGCDByModulus(t, i);
            t = t / g;
            i = i / g;
            tmp = tmp.divide(BigDecimal.valueOf(g), MathContext.DECIMAL128);
            while (g != 1) {
                g = getGCDByModulus(t, i);
                t = t / g;
                i = i / g;
                tmp = tmp.divide(BigDecimal.valueOf(g), MathContext.DECIMAL64);
            }
            if (i < n) continue;
            if (pq.containsKey(i)) {
                BigDecimal ptemp = pq.get(i).round(MathContext.DECIMAL64);
                if (ptemp.compareTo(tmp) == 0) continue;
            }
//            System.out.println("α=" + temp);
            pq.put(i, temp);
            p.put(i, t);
            temp = new BigDecimal((double)t/i);
//            System.out.println("p=" + t);
//            System.out.println("q=" + i);
//            System.out.println("p/q=" + temp);
            int sign = temp.subtract(alpha).compareTo(BigDecimal.ZERO);
            if (sign >= 0) {
                s.put(i, true);
            } else {
                s.put(i, false);
            }
            ab = temp.subtract(alpha).abs();
            abs.put(i, ab);
//            System.out.println("|p/q-α|=" + ab);
            approx.forEach((key, value) -> {
                if (key <= i) {
                    if (!stop) {
                        int cmp = temp.subtract(alpha, MathContext.DECIMAL32).abs().subtract(BigDecimal.valueOf((double)key/value).subtract(alpha, MathContext.DECIMAL32).abs()).compareTo(BigDecimal.ZERO);
                        if (cmp == 0) {
                            System.out.println("II vrsta");
                        } else if (cmp > 0) {
                            smaller++;
                        }
                    }
                }
            });
            abs.forEach((key, value) -> {
                int cmp = temp.subtract(alpha).abs().subtract(value.abs()).compareTo(BigDecimal.ZERO);
                if (cmp > 0) {
                    smaller++;
                }
            });
            if (!stop && smaller > 0) {

            }
            else if (!stop) {
                System.out.println("I vrsta");
                System.out.println("p=" + t);
                System.out.println("q=" + i);
                System.out.println("------------");
            }
            smaller = -1;
            stop = false;
            ArrayList<BigDecimal> array = calculate(temp);
//            System.out.print("[");
            int j = 0;
            for (BigDecimal b : array) {
//                System.out.print(b);
                if (j != array.size() - 1) {
//                    System.out.print(", ");
                }
                j++;
            }
//            System.out.println("]");
            ver.put(i, array);
        }
        print(abs);
    }

    public static void print(HashMap<Integer, BigDecimal> map) {
        ArrayList<String> list = new ArrayList<>();
        LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
            list.add(String.valueOf(entry.getValue()));
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String str, String str1) {
                return (str).compareTo(str1);
            }
        });
        for (String str : list) {
            for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
                if (entry.getValue().subtract(new BigDecimal(str)).compareTo(BigDecimal.ZERO) == 0) {
                    sortedMap.put(String.valueOf(entry.getKey()), str);
                }
            }
        }
        System.out.println();
        System.out.println();
        System.out.println("Razlomak\tVerizni zapis\t\t\tOdstupanje");

        sortedMap.forEach((key, value) -> {
            System.out.print(p.get(new Integer(key)) + "/" + key + "\t\t[");
            int j = 0;
            for (BigDecimal b : ver.get(new Integer(key))) {
                System.out.print(b);
                if (j != ver.get(new Integer(key)).size() - 1) {
                    System.out.print(", ");
                }
                j++;
            }
            System.out.print("]\t\t");
            System.out.print(s.get(new Integer(key)) ? "+" : "-");
            System.out.println(abs.get(new Integer(key)).setScale(18, RoundingMode.HALF_EVEN));
        });
//        System.out.println(sortedMap);
    }

    public static ArrayList<BigDecimal> calculate(BigDecimal alpha) {
        ArrayList<BigDecimal> x = new ArrayList<>();
        ArrayList<BigDecimal> a = new ArrayList<>();
        ArrayList<BigDecimal> d = new ArrayList<>();
        int i = 0;
        x.add(alpha);
        a.add(alpha.setScale(0, RoundingMode.FLOOR));
        d.add(x.get(i).subtract(a.get(i)));
        while (d.get(i).setScale(10, BigDecimal.ROUND_HALF_EVEN).compareTo(BigDecimal.ZERO) != 0) {
            i++;
            x.add(BigDecimal.ONE.divide(d.get(i-1), MathContext.DECIMAL128));
            a.add(x.get(i).setScale(0, RoundingMode.FLOOR));
            d.add(x.get(i).subtract(a.get(i)).abs());
        }
        return a;
    }

    public static int getGCDByModulus(int value1, int value2)
    {
        int v1 = value1;
        int v2 = value2;
        while (v1 != 0 && v2 != 0)
        {
            if (v1 > v2)
                v1 %= v2;
            else
                v2 %= v1;
        }
        return Math.max(v1, v2);
    }

    public static HashMap<Integer, Integer> calculateSth(BigDecimal b) {
        ArrayList<BigDecimal> fractions = continuedFraction(b);
        HashMap<Integer, Integer> res = new HashMap<>();
        BigDecimal num1 = BigDecimal.ZERO;
        BigDecimal num2 = BigDecimal.ONE;
        BigDecimal den1 = BigDecimal.ONE;
        BigDecimal den2 = BigDecimal.ZERO;

        for (int i = 0; i < n; i++) {

            BigDecimal a = fractions.get(i);
            BigDecimal num = a.multiply(num1).add(num2);
            BigDecimal den = a.multiply(den1).add(den2);


            num2 = num1;
            num1 = num;
            den2 = den1;
            den1 = den;
            if (den.compareTo(BigDecimal.ZERO) == 0) continue;
            res.put(den.intValue(), num.intValue());
//            System.out.printf("Approximation %d: %s/%s \n", i + 1, den, num);
//            System.out.println("Difference=" + den.divide(num, MathContext.DECIMAL128).subtract(alpha).abs());
        }
//        System.out.println("----------");
        return res;
    }
    public static ArrayList<BigDecimal> continuedFraction(BigDecimal b) {
        BigDecimal pi = b;
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