package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends FileReader {

    private static final int DAY = 13;
    private final String[] lines;

    List<int[]> aOffsets, bOffsets, targets;

    final int INFINITY = Integer.MAX_VALUE;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day13(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        aOffsets = new ArrayList<>();
        bOffsets = new ArrayList<>();
        targets = new ArrayList<>();

        Pattern p1 = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
        Pattern p2 = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
        Pattern p3 = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
        for (int i = 0; i < lines.length; i += 4) {
            Matcher m1 = p1.matcher(lines[i]);
            Matcher m2 = p2.matcher(lines[i + 1]);
            Matcher m3 = p3.matcher(lines[i + 2]);
            // i + 3 is empty line

            if (!m1.matches() || !m2.matches() || !m3.matches()) {   // For some reason it crashes if we don't do this, even though we know it will match
                System.out.printf("Skipped following lines:%n%s%n%s%n%s%n", lines[i], lines[i + 1], lines[i + 2]);
                continue;
            }
            aOffsets.add(new int[] {Integer.parseInt(m1.group(1)), Integer.parseInt(m1.group(2))});
            bOffsets.add(new int[] {Integer.parseInt(m2.group(1)), Integer.parseInt(m2.group(2))});
            targets.add(new int[] {Integer.parseInt(m3.group(1)), Integer.parseInt(m3.group(2))});
        }

        cost_a_cache[0] = 0;
        cost_b_cache[0] = 0;
        for (int k = 1; k <= 100; k++) {
            cost_a_cache[k] = k * COST_A;
            cost_b_cache[k] = k * COST_B;
        }
    }

    int[] cost_a_cache = new int[101];
    int[] cost_b_cache = new int[101];

    final int COST_A = 3;
    final int COST_B = 1;

    int[] calculateMinimum(int i) {
        int a_x = aOffsets.get(i)[0];
        int a_y = aOffsets.get(i)[1];
        int b_x = bOffsets.get(i)[0];
        int b_y = bOffsets.get(i)[1];
        int t_x = targets.get(i)[0];
        int t_y = targets.get(i)[1];
        int x, y;

        int[] a_x_cache = new int[101];
        int[] a_y_cache = new int[101];
        int[] b_x_cache = new int[101];
        int[] b_y_cache = new int[101];
        a_x_cache[0] = 0;
        a_y_cache[0] = 0;
        b_x_cache[0] = 0;
        b_y_cache[0] = 0;
        for (int k = 1; k <= 100; k++) {
            a_x_cache[k] = a_x_cache[k - 1] + a_x;
            a_y_cache[k] = a_y_cache[k - 1] + a_y;
            b_x_cache[k] = b_x_cache[k - 1] + b_x;
            b_y_cache[k] = b_y_cache[k - 1] + b_y;
        }

        int minTokens = Integer.MAX_VALUE, tokens;
        int ways = 0;
        for (int aPressed = 0; aPressed <= 100; aPressed++) {
            for (int bPressed = 0; bPressed <= 100; bPressed++) {
                x = a_x_cache[aPressed] + b_x_cache[bPressed];
                y = a_y_cache[aPressed] + b_y_cache[bPressed];
                if (x > t_x || y > t_y) break;
                if (x == t_x && y == t_y) {
                    tokens = cost_a_cache[aPressed] + cost_b_cache[bPressed];
                    if (minTokens == tokens) ways++;
                    else if (tokens < minTokens) {
                        minTokens = tokens;
                        ways = 1;
                    }
                }
            }
        }

        // Result at target
        return new int[] { minTokens, ways };
    }

    long calculateMinimum2(int i) {
        // Retrieve inputs
        long a_x = aOffsets.get(i)[0];
        long a_y = aOffsets.get(i)[1];
        long b_x = bOffsets.get(i)[0];
        long b_y = bOffsets.get(i)[1];

        long t_x = targets.get(i)[0] + 10_000_000_000_000L;
        long t_y = targets.get(i)[1] + 10_000_000_000_000L;

        long det = a_x * b_y - a_y * b_x;
        if (det != 0) {
            // Unique solution
            long X = t_x * b_y - b_x * t_y;
            long Y = -a_y * t_x + a_x * t_y;

            // Check if X%det==0 and Y%det==0 for perfect integral solution
            if (X % det == 0 && Y % det == 0) {
                long a = X / det;
                long b = Y / det;
                return a * COST_A + b * COST_B;
            } else {
                // No integral solution here
                return 0;
            }

        } else {
            // det(A)=0 => infinite solutions or no solution
            boolean consistent;
            consistent = (t_x * b_y == t_y * b_x) && (t_x * a_y == t_y * a_x);

            if (!consistent) {
                // No solutions
                return 0;
            }

            long d = gcd(a_x, b_x);
            if (t_x % d != 0) {
                // No integral solution
                return 0;
            }

            // Reduce equation
            long a_xp = a_x / d;
            long b_xp = b_x / d;
            long t_xp = t_x / d;

            long[] eg = extendedGCD(a_xp, b_xp);
            long a0 = eg[1] * t_xp;
            long b0 = eg[2] * t_xp;

            // General solution:
            // a = a0 + k*(b_xp)
            // b = b0 - k*(a_xp)

            // Objective: Minimize 3a + b
            // = 3(a0 + k*b_xp) + (b0 - k*a_xp)
            // = (3b_xp - a_xp)*k + (3a0 + b0)

            long coeff_k = 3*b_xp - a_xp;

            if (coeff_k == 0) {
                // constant for all solutions
                return a0 * COST_A + b0 * COST_B;
            } else {
                // no finite minimum exists.
                return 0; // Indicate no finite minimum
            }
        }
    }

    // Extended Euclidean Algorithm to find x,y for ax+by=gcd(a,b)
    private long[] extendedGCD(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] result = extendedGCD(b, a % b);
        long g = result[0];
        long x = result[1];
        long y = result[2];
        return new long[]{g, y, x - (a / b) * y};
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return Math.abs(a);
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        for (int i = 0; i < aOffsets.size(); i++) {
            int minTokens = calculateMinimum(i)[0];
            //System.out.printf("(%d/%d): %s%n", i+1, aOffsets.size(), minTokens != INFINITY ? "" + minTokens : "not possible");
            if (minTokens != INFINITY) result += minTokens;
        }

        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < aOffsets.size(); i++) {
            long minTokens = Math.max(0, calculateMinimum2(i));
            //System.out.printf("(%d/%d): %s%n", i+1, aOffsets.size(), minTokens != 0 ? "" + minTokens : "not possible");
            if (minTokens != INFINITY) result = result.add(BigInteger.valueOf(minTokens));
        }

        out += result;
        System.out.println(out);
    }
}
