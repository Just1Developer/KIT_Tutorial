package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day25 extends FileReader {

    private static final int DAY = 25;
    private String[] lines;

    private static final int PINS = 5;
    private static final int MAX_HEIGHT = 6;    // struct - top and bottom row + bottom key row
    private static final int STRUCT_HEIGHT = 7;

    Set<int[]> locks;  // List<int> for possibly checking equality
    Set<int[]> keys;  // List<int> for possibly checking equality

    /**
     * Initializes code for day {@code DAY}.
     * @param exampleNr The example number for the filepath.
     */
    public Day25(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        locks = new HashSet<>();
        keys = new HashSet<>();

        Set<int[]> current = null;
        for (int i = 0; i < lines.length; i++) {    // i++ for skipping empty line
            int[] struct = new int[5];

            for (int line = 0; line < STRUCT_HEIGHT; line++, i++) {
                if (line == 0) {
                    if (lines[i].charAt(0) == '#') current = locks;
                    else current = keys;
                    continue;
                }
                String l = lines[i];
                for (int j = 0; j < PINS; j++) {
                    if (l.charAt(j) == '#') struct[j]++;
                }
            }

            current.add(struct);
        }
    }

    boolean fits(List<Integer> lock, List<Integer> key) {
        for (int i = 0; i < lock.size(); i++) {
            if (lock.get(i) + key.get(i) > MAX_HEIGHT) return false;
        }
        return true;
    }

    boolean fits(int[] lock, int[] key) {
        for (int i = 0; i < lock.length; i++) {
            if (lock[i] + key[i] > MAX_HEIGHT) return false;
        }
        return true;
    }

    public void part1() {
        String out = "part1 >> ";
        long result = 0;
        long start = System.currentTimeMillis();

        for (var lock : locks) {
            for (var key : keys) {
                if (fits(lock, key)) result++;
            }
        }

        long end = System.currentTimeMillis();
        out += result;
        out += ", Time: " + (end - start) + "ms";
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        long start = System.currentTimeMillis();

        out += "AoC Chronicle 2024 completed :)";

        long end = System.currentTimeMillis();
        out += " - Time: %dms".formatted(end - start);
        System.out.println(out);
    }
}
