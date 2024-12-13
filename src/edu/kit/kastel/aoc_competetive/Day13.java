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

    final int NEG_INF = Integer.MIN_VALUE;

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

    int[] calculateMinimum(int i) {
        int[] A = aOffsets.get(i);
        int[] B = bOffsets.get(i);
        int[] target = targets.get(i);
        dp = new int[target[0] + 1][target[1] + 1][2];
        for (int[][] a : dp) {
            for (int[] b : a) {
                Arrays.fill(b, NEG_INF);
            }
        }
        dp[0][0] = coord(0, 1);

        int tokensA, tokensB, tokenTotal, waysA, waysB;

        int[] current = coord(0, 0);
        int[] prev = coord(0, 0);



        for (; current[1] <= target[1]; current[1]++, prev[1]++) {
            for (current[0] = 0, prev[0] = 0; current[0] <= target[0]; current[0]++, prev[0]++) {
                /*if (current[0] == target[0] && current[1] == target[1]) {
                    System.out.println("Finished");
                }
                if (current[1] == current[0]) {
                    System.out.println("Diagonal");
                }*/

                subtract(prev, A);
                var dpResultRef = get(prev);
                tokensA = (dpResultRef[0] == NEG_INF) ? NEG_INF : dpResultRef[0] + COST_A;
                waysA = Math.max(0, dpResultRef[1]);
                add(prev, A);

                subtract(prev, B);
                dpResultRef = get(prev);
                tokensB = (dpResultRef[0] == NEG_INF) ? NEG_INF : dpResultRef[0] + COST_B;
                waysB = Math.max(0, dpResultRef[1]);
                add(prev, B);

                tokenTotal = tokensA != NEG_INF && tokensB != NEG_INF ? Math.min(tokensA, tokensB) : Math.max(tokensA, tokensB);    // One of them is negative infinity, so find the one that isn't instead
                if (tokenTotal > NEG_INF) {
                    if (tokenTotal < dp[current[0]][current[1]][0] || dp[current[0]][current[1]][0] == NEG_INF) {
                        dp[current[0]][current[1]][0] = tokenTotal;
                        dp[current[0]][current[1]][1] = waysA;  // Todo not yet, on part 2 maybe
                    } else if (tokenTotal == dp[current[0]][current[1]][0]) {
                        dp[current[0]][current[1]][1] += waysA + waysB;
                    }
                }   // Both were negative infinity
            }
        }
        return dp[target[0]][target[1]];
    }

    final int[] NULL_COORD = coord(NEG_INF, NEG_INF);
    int[] get(int[] pos) {
        if (pos[0] < 0 || pos[1] < 0 || pos[1] >= dp.length || pos[0] >= dp[0].length) { return NULL_COORD; }
        return dp[pos[0]][pos[1]];
    }

    void subtract(int[] coord, int[] coord2) {
        coord[0] -= coord2[0];
        coord[1] -= coord2[1];
    }
    void add(int[] coord, int[] coord2) {
        coord[0] += coord2[0];
        coord[1] += coord2[1];
    }
    int[] coord(int x, int y) {
        return new int[] {x, y};
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        for (int i = 0; i < aOffsets.size(); i++) {
            int minTokens = calculateMinimum(i)[0];
            System.out.println(minTokens);
            result += minTokens;
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
