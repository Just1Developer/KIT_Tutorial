package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Day11 extends FileReader {

    private static final int DAY = 11;
    private final String[] lines;

    static final int ITERATIONS = 25;
    static final int ITERATIONS_PT2 = 75;

    List<Long> stones;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day11(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        stones = new ArrayList<>();

        for (String num : lines[0].split(" ")) {
            long val = Long.parseLong(num);
            stones.add(val);
        }

        stones.sort(Comparator.naturalOrder());
    }

    //https://www.reddit.com/r/adventofcode/comments/1hbm0al/comment/m1ltr3v/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
    private static List<Long> blink(long rock)
    {
        if (rock == 0) return List.of(1L);

        var digits = (long)Math.floor(Math.log10(rock)) + 1;

        if (digits % 2 != 0) return List.of(rock * 2024L);

        var halfDigits = digits / 2;
        var first = rock / (long)Math.pow(10, halfDigits);
        var second = rock % (long)Math.pow(10, halfDigits);
        return List.of(first, second);
    }
    private static Map<Long, Long> BlinkRocks(Map<Long, Long> rocks)
    {
        Map<Long, Long> result = new HashMap<>();

        for (var entry : rocks.entrySet())
        {
            var newRocks = blink(entry.getKey());
            for (var newRock : newRocks)
            {
                long current = result.getOrDefault(newRock, 0L);
                result.put(newRock, current + entry.getValue());
            }
        }

        return result;
    }

    public void part1() {
        String out = "part1 >> ";
        Map<Long, Long> rocks = new HashMap<>();
        for (long l : stones) {
            long result = rocks.getOrDefault(l, 0L);
            rocks.put(l, result + 1);
        }
        for (int i = 0; i < ITERATIONS; i++) rocks = BlinkRocks(rocks);

        long result = 0;
        for (long l : rocks.values()) result += l;

        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        Map<Long, Long> rocks = new HashMap<>();
        for (long l : stones) {
            long result = rocks.getOrDefault(l, 0L);
            rocks.put(l, result + 1);
        }
        for (int i = 0; i < ITERATIONS_PT2; i++) rocks = BlinkRocks(rocks);

        long result = 0;
        for (long l : rocks.values()) result += l;

        out += result;
        System.out.println(out);
    }
}
