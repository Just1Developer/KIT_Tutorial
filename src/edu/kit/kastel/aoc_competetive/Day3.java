package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

public class Day3 extends FileReader {

    private static int DAY = 3;

    private String[] lines;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day3(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);
    }

    public void part1() {
        String out = "part1 >> ";

        // Code...

        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";

        // Code...

        System.out.println(out);
    }

}
