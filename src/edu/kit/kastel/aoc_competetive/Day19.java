package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Day19 extends FileReader {

    private static final int DAY = 19;
    private String[] lines;

    List<String> patterns;
    List<String> towels;
    Pattern towelPattern;

    /**
     * Initializes code for day {@code DAY}.
     * @param exampleNr The example number for the filepath.
     */
    public Day19(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        patterns = new ArrayList<>();
        towels = new ArrayList<>();

        patterns.addAll(Arrays.asList(lines[0].split(", ")));
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("^(");
        for (int i = 0; i < patterns.size(); i++) {
            patternBuilder.append(patterns.get(i));
            if (i < patterns.size() - 1) patternBuilder.append("|");
        }
        patternBuilder.append(")+$");
        towelPattern = Pattern.compile(patternBuilder.toString());

        towels.addAll(Arrays.asList(lines).subList(2, lines.length));
    }

    int maxLength = 0;
    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        for (String towel : towels) {
            if (towelPattern.matcher(towel).matches()) result++;
            if (towel.length() > maxLength) maxLength = towel.length();
        }
        System.out.println("Max: " + maxLength);

        out += result;
        System.out.println(out);
    }

    TrieNode root;
    HashMap<String, Long> cache = new HashMap<>();

    public void part2() {
        String out = "part2 >> ";
        long result = 0;

        root = new TrieNode((char)0);
        for (String towel : patterns) root.addPatternRec(towel);
        for (String towel : towels) {
            if (!towelPattern.matcher(towel).matches()) continue;
            result += root.matchDistinct(towel);
        }

        out += result;
        System.out.println(out);
    }

    private class TrieNode {
        static final String colors = "wubrg";

        private final TrieNode[] children;
        private final char color;
        private boolean possible;

        public TrieNode(char color) {
            this.children = new TrieNode[5];
            this.color = color;
            this.possible = false;
        }

        public void addPatternRec(String towelPattern) {
            if (towelPattern.isEmpty()) {
                possible = true;
                return;
            }
            char stripe = towelPattern.charAt(0);
            int col = colors.indexOf(stripe);
            if (col == -1) throw new OutOfMemoryError(" :) ");
            if (children[col] == null) children[col] = new TrieNode(stripe);
            children[col].addPatternRec(towelPattern.substring(1));
        }

        public long matchDistinct(String towelPattern) {
            if (color == 0 && cache.containsKey(towelPattern))
                return cache.get(towelPattern);
            long result = 0;

            // Finished
            if (towelPattern.isEmpty()) {
                return possible ? 1 : 0;
            }

            if (possible) result += root.matchDistinct(towelPattern);   // Works as long as root is not possible
            int col = colors.indexOf(towelPattern.charAt(0));
            // If sub-trie exists
            if (children[col] != null) {
                result += children[col].matchDistinct(towelPattern.substring(1));
            }
            // Cache if root
            if (color == 0) {
                cache.put(towelPattern, result);
            }
            return result;
        }
    }
}
