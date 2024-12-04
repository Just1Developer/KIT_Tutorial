package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 extends FileReader {

    private static int DAY = 4;

    private String[] lines;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day4(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);
    }

    public void part1() {
        String out = "part1 >> ";

        int sum = 0;

        // Look for XMAS
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != 'X') continue;
                sum += look(0, 1, i, j);
                sum += look(0, -1, i, j);
                sum += look(1, 0, i, j);
                sum += look(-1, 0, i, j);
                sum += look(1, 1, i, j);
                sum += look(-1, 1, i, j);
                sum += look(1, -1, i, j);
                sum += look(-1, -1, i, j);
            }
        }

        out += sum;
        System.out.println(out);
    }

    private int look(int deltaX, int deltaY, int i, int j) {
        if (deltaY == 1 && i >= lines.length - 3) return 0;
        if (deltaY == -1 && i < 3) return 0;
        if (deltaX == 1 && j >= lines[i].length() - 3) return 0;
        if (deltaX == -1 && j < 3) return 0;

        return lines[i + (deltaY)].charAt(j + (deltaX)) == 'M'
                && lines[i + (2 * deltaY)].charAt(j + (2 * deltaX)) == 'A'
                && lines[i + (3 * deltaY)].charAt(j + (3 * deltaX)) == 'S'
                ? 1 : 0;
    }

    public void part2() {
        String out = "part2 >> ";

        int sum = 0;
        // Look for XMAS
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c != 'A') continue;
                sum += look2(i, j);
            }
        }

        out += sum;
        System.out.println(out);
    }

    private int look2(int i, int j) {
        if (i >= lines.length - 1) return 0;
        if (i < 1) return 0;
        if (j >= lines[i].length() - 1) return 0;
        if (j < 1) return 0;

        return ((lines[i - 1].charAt(j - 1) == 'M' && lines[i + 1].charAt(j + 1) == 'S') || (lines[i - 1].charAt(j - 1) == 'S' && lines[i + 1].charAt(j + 1) == 'M'))
                && ((lines[i - 1].charAt(j + 1) == 'M' && lines[i + 1].charAt(j - 1) == 'S') || (lines[i - 1].charAt(j + 1) == 'S' && lines[i + 1].charAt(j - 1) == 'M'))
                ? 1 : 0;
    }

}
