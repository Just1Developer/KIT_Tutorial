package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends FileReader {

    private static final int DAY = 13;
    private static String[] lines;

    List<int[]> aOffsets, bOffsets, targets;
    int[][][] dp;   // We store int[] as key-value pair for x = tokens and y = # of ways to get there

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

            m1.matches();
            m2.matches();
            m3.matches();
            // i + 3 is empty line
            //System.out.println(i + " + 0: \"" + lines[i] + "\" >> " + m1.matches());
            //System.out.println(i + " + 1: \"" + lines[i + 1] + "\"" + m2.matches());
            //System.out.println(i + " + 2: \"" + lines[i + 2] + "\"" + m3.matches());
            aOffsets.add(coord(Integer.parseInt(m1.group(1)), Integer.parseInt(m1.group(2))));
            bOffsets.add(coord(Integer.parseInt(m2.group(1)), Integer.parseInt(m2.group(2))));
            targets.add(coord(Integer.parseInt(m3.group(1)), Integer.parseInt(m3.group(2))));
        }
    }

    final int COST_A = 3;
    final int COST_B = 1;

    final int Y_INDEX = 0;
    final int X_INDEX = 1;

    int[] calculateMinimum(int i) {
        int[] A = aOffsets.get(i);
        int[] B = bOffsets.get(i);
        int[] target = targets.get(i);
        dp = new int[target[Y_INDEX] + 1][target[X_INDEX] + 1][4];
        for (int[][] a : dp) {
            for (int[] b : a) {
                b[0] = INFINITY;
                b[1] = 0;
                b[2] = 0;
                b[3] = 0;
            }
        }
        dp[0][0] = result(0, 1, 0, 0);

        int tokensA, tokensB, tokenTotal, waysA, waysB, waysTotal, chosen;

        int[] current = coord(0, 0);
        int[] prev = coord(0, 0);



        for (; current[X_INDEX] <= target[X_INDEX]; current[X_INDEX]++, prev[X_INDEX]++) {
            for (current[Y_INDEX] = 0, prev[Y_INDEX] = 0; current[Y_INDEX] <= target[Y_INDEX]; current[Y_INDEX]++, prev[Y_INDEX]++) {
                subtract(prev, A);
                var dpResultRef = get(prev);
                if (dpResultRef[2] >= 100) {        // Button pressed maximum times
                    tokensA = INFINITY;
                    waysA = 0;
                } else {
                    tokensA = (dpResultRef[0] == INFINITY) ? INFINITY : dpResultRef[0] + COST_A;
                    waysA = Math.max(0, dpResultRef[1]);
                }
                add(prev, A);

                subtract(prev, B);
                dpResultRef = get(prev);
                if (dpResultRef[3] >= 100) {        // Button pressed maximum times
                    tokensB = INFINITY;
                    waysB = 0;
                } else {
                    tokensB = (dpResultRef[0] == INFINITY) ? INFINITY : dpResultRef[0] + COST_B;
                    waysB = Math.max(0, dpResultRef[1]);
                }
                add(prev, B);

                if (tokensA < tokensB) {
                    chosen = 2; // A
                    tokenTotal = tokensA;
                    waysTotal = waysA;
                } else {
                    chosen = 3;
                    tokenTotal = tokensB;
                    waysTotal = tokensA == tokensB ? waysA + waysB : waysB;
                }
                if (tokenTotal != INFINITY) {
                    if (tokenTotal < dp[current[Y_INDEX]][current[X_INDEX]][0]) {
                        dp[current[Y_INDEX]][current[X_INDEX]][0] = tokenTotal;
                        dp[current[Y_INDEX]][current[X_INDEX]][1] = waysTotal;
                        dp[current[Y_INDEX]][current[X_INDEX]][chosen]++;
                    } else if (tokenTotal == dp[current[Y_INDEX]][current[X_INDEX]][0]) {
                        dp[current[Y_INDEX]][current[X_INDEX]][1] += waysA + waysB;
                        dp[current[Y_INDEX]][current[X_INDEX]][chosen]++;
                    }
                }   // Both were negative infinity
            }
        }
        return dp[target[Y_INDEX]][target[X_INDEX]];
    }

    final int[] NULL_RESULT = result(INFINITY, 0, 0, 0);
    int[] get(int[] pos) {
        if (pos[Y_INDEX] < 0 || pos[Y_INDEX] >= dp.length || pos[X_INDEX] < 0 || pos[X_INDEX] >= dp[pos[Y_INDEX]].length) {
            return NULL_RESULT;
        }
        return dp[pos[Y_INDEX]][pos[X_INDEX]];
    }

    void subtract(int[] coord, int[] coord2) {
        coord[Y_INDEX] -= coord2[Y_INDEX];
        coord[X_INDEX] -= coord2[X_INDEX];
    }
    void add(int[] coord, int[] coord2) {
        coord[Y_INDEX] += coord2[Y_INDEX];
        coord[X_INDEX] += coord2[X_INDEX];
    }
    int[] coord(int x, int y) {
        int[] coord = new int[2];
        coord[X_INDEX] = x;
        coord[Y_INDEX] = y;
        return coord;
    }
    int[] result(int tokens, int ways, int pressedA, int pressedB) {
        return new int[] {tokens, ways, pressedA, pressedB};
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        for (int i = 0; i < aOffsets.size(); i++) {
            int minTokens = calculateMinimum(i)[0];
            System.out.printf("(%d/%d): %s%n", i+1, aOffsets.size(), minTokens != INFINITY ? "" + minTokens : " (not possible)");
            if (minTokens != INFINITY) result += minTokens;
        }

        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        int result1 = 0, result2 = 0;



        out += result1 + " or " + result2;
        System.out.println(out);
    }
}
