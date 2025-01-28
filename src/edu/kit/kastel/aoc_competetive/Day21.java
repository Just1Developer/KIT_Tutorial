package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Day21 extends FileReader {

    private static final int DAY = 19;
    private String[] lines;

    /**
     * Initializes code for day {@code DAY}.
     * @param exampleNr The example number for the filepath.
     */
    public Day21(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        
    }

    // We need Set<char> of what to press and order it
    // And then Map<char, int> for the amount

    enum NumpadType {
        NUMPAD, NAVIGATION;

        int lastRow(NumpadType type) {
            return switch (type) {
                case NUMPAD -> 3;
                case NAVIGATION -> 1;
            };
        }
        int lastCol(NumpadType type) {
            return 2;
        }
    }

    class KeypadNavigate {
        NumpadType type;
        int fromRow, fromCol, toRow, toCol;
        Set<Character> keys;
        Map<Character, Integer> amounts;


        List<Character> findOrdering(char current, Optional<KeypadNavigate> next) {
            return null;
        }

        boolean isValidOrdering(List<Character> ordering) {
            if (fromRow == 3) return true;
            return false;
        }
    }

    int rowOfNumpad(char c) {
        return switch (c) {
            case '7', '8', '9' -> 0;
            case '4', '5', '6' -> 1;
            case '1', '2', '3' -> 2;
            case '0', 'A' -> 3;
            default -> throw new IllegalArgumentException("Invalid character: " + c);
        };
    }

    int colOfNumpad(char c) {
        return switch (c) {
            case '7', '4', '1' -> 0;
            case '8', '5', '2', '0' -> 1;
            case '9', '6', '3', 'A' -> 2;
            default -> throw new IllegalArgumentException("Invalid character: " + c);
        };
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;



        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        long result = 0;



        out += result;
        System.out.println(out);
    }
}
