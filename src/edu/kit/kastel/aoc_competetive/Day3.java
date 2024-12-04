package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

        int sum = 0;
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");          //(?:(?:.*)?mul(\d+),(\d+)\)(?:.*)?)+
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            for (var result : matcher.results().toList()) {
                sum += Integer.parseInt(result.group(1)) * Integer.parseInt(result.group(2));
            }
        }
        out += sum;

        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";

        int sum = 0;
        Pattern pattern = Pattern.compile("(mul\\((\\d+),(\\d+)\\)|(do\\(\\))|(don't\\(\\)))");          //(?:(?:.*)?mul(\d+),(\d+)\)(?:.*)?)+
        boolean _do = true;
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            for (var result : matcher.results().toList()) {
                if (result.group(0).startsWith("do(")) { _do = true; continue; }
                if (result.group(0).startsWith("don't")) { _do = false; continue; }

                if(_do) sum += Integer.parseInt(result.group(2)) * Integer.parseInt(result.group(3));
            }
        }
        out += sum;

        System.out.println(out);
    }

}
