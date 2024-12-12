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

    long stones_0 = 0;
    long stones_1 = 0;
    List<Long> evenDigits;
    List<Long> unevenDigitsOther1;

    List<Long> stones;

    HashMap<Long, List<Long>> splits;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day11(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        stones = new ArrayList<>();
        evenDigits = new ArrayList<>();
        unevenDigitsOther1 = new ArrayList<>();
        splits = new HashMap<>();

        depthSplits = new HashMap<>();
        trackedDepths = new HashMap<>();
        addCounts = new HashMap<>();

        for (String num : lines[0].split(" ")) {
            long val = Long.parseLong(num);
            if (val == 0) stones_0++;
            else if (val == 1) stones_1++;
            else if (num.length() % 2 == 0) evenDigits.add(val);
            else unevenDigitsOther1.add(val);
            stones.add(val);
        }

        stones.sort(Comparator.naturalOrder());
    }



    HashMap<Long, Long> depthSplits;          // From the root <key>, how many splits occur in total
    HashMap<Long, Integer> trackedDepths;           // Begin Depths for each root <key>
    HashMap<Long, List<Long>> addCounts;      // Store reference to: Root we need to add all of <key in deathsplits> that we need to add

    void recursion(List<Long> previousNumbers, long currentNumber, int currentDepth, final int MAX_DEPTH) {
        if (currentDepth > MAX_DEPTH) return;

        previousNumbers.add(currentNumber);
        if (trackedDepths.containsKey(currentNumber)) {
            int depth = trackedDepths.get(currentNumber);
            if (depth <= currentDepth) {
                // Copy the values. previousNumbers[0] is always root
                long key = previousNumbers.getFirst();
                if (!addCounts.containsKey(key)) addCounts.put(key, new ArrayList<>());
                addCounts.get(key).add(currentNumber);
            } else {
                trackedDepths.put(currentNumber, currentDepth);
                depthSplits.put(currentNumber, 0L);
            }
            previousNumbers.remove(previousNumbers.size() - 1L);
            return;
        }


        for (long l : previousNumbers) {
            if (!depthSplits.containsKey(l)) {
                depthSplits.put(l, 0L);
                trackedDepths.put(l, currentDepth);
            }
        }

        if (currentNumber == 0) {
            int newNumber = 1;
            recursion(previousNumbers, newNumber, currentDepth + 1, MAX_DEPTH);
        } else {
            String numberStr = String.valueOf(currentNumber);
            if (numberStr.length() % 2 == 0) {
                long l1 = Long.parseLong(numberStr.substring(0, numberStr.length() / 2));
                long l2 = Long.parseLong(numberStr.substring(numberStr.length() / 2));
                for (Long previousNumber : previousNumbers) {  // In the list of every number, save that there is a split going on
                    depthSplits.put(previousNumber, depthSplits.get(previousNumber) + 1);
                }
                recursion(previousNumbers, l1, currentDepth + 1, MAX_DEPTH);
                recursion(previousNumbers, l2, currentDepth + 1, MAX_DEPTH);
            } else {
                recursion(previousNumbers, currentNumber * 2024, currentDepth + 1, MAX_DEPTH);
            }
        }
        previousNumbers.removeLast();
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

    public void part_both() {
        Map<Long, Long> rocks = new HashMap<>();
        for (long l : stones) {
            long result = rocks.getOrDefault(l, 0L);
            rocks.put(l, result + 1);
        }
        for (int i = 0; i < ITERATIONS; i++) {
            rocks = BlinkRocks(rocks);
        }
        long sum = 0;
        for (long l : rocks.values()) {
            sum += l;
        }
        System.out.println("part 1 >> " + sum);
        for (int i = ITERATIONS; i < ITERATIONS_PT2; i++) {
            rocks = BlinkRocks(rocks);
        }
        sum = 0;
        for (long l : rocks.values()) {
            sum += l;
        }
        System.out.println("part 2 >> " + sum);
    }


/*
    HashMap<Long, List<Long>> depthSplits;          // For the root <key>, how many splits occur at depth offset <0, 1, 2, 3, ...>
    HashMap<Long, Integer> trackedDepths;           // Begin Depths for each root <key>
    HashMap<Map.Entry<Long, Integer>, List<Map.Entry<Long, Integer>>> addCounts;      // Store reference to: <Root, Depth> we need to add all of <key in deathsplits, beginindex> until end


    void recursion(List<Long> previousNumbers, long currentNumber, int currentDepth, final int MAX_DEPTH) {
        if (currentDepth > MAX_DEPTH) return;

        previousNumbers.add(currentNumber);
        if (trackedDepths.containsKey(currentNumber)) {
            int depth = trackedDepths.get(currentNumber);
            if (depth <= currentDepth) {
                // Copy the values. previousNumbers[0] is always root
                Map.Entry<Long, Integer> key = new AbstractMap.SimpleEntry<>(previousNumbers.getFirst(), currentDepth);
                Map.Entry<Long, Integer> entry = new AbstractMap.SimpleEntry<>(currentNumber, currentDepth - depth);
                if (!addCounts.containsKey(key)) addCounts.put(key, new ArrayList<>());
                addCounts.get(key).add(entry);
            } else {
                int depthDifference = currentDepth - depth;
                trackedDepths.put(currentNumber, currentDepth);
                depthSplits.put(currentNumber, new ArrayList<>());
                // Adjust all references
                for (var auto : addCounts.values()) {
                    for (Map.Entry<Long, Integer> entry : auto) {
                        if (entry.getKey() != currentNumber) continue;
                        entry.setValue(entry.getValue() + depthDifference);
                    }
                }
            }
            previousNumbers.remove(previousNumbers.size() - 1L);
            return;
        }


        for (long l : previousNumbers) {
            if (!depthSplits.containsKey(l)) {
                depthSplits.put(l, new ArrayList<>());
                trackedDepths.put(l, currentDepth);
            }
            if (depthSplits.get(l).size() <= currentDepth)
                depthSplits.get(l).add(0L);
        }

        if (currentNumber == 0) {
            int newNumber = 1;
            recursion(previousNumbers, newNumber, currentDepth + 1, MAX_DEPTH);
        } else {
            String numberStr = String.valueOf(currentNumber);
            if (numberStr.length() % 2 == 0) {
                long l1 = Long.parseLong(numberStr.substring(0, numberStr.length() / 2));
                long l2 = Long.parseLong(numberStr.substring(numberStr.length() / 2));
                for (int i = 0; i < previousNumbers.size(); i++) {  // In the list of every number, save that there is a split going on
                    var list = depthSplits.get(currentNumber);
                    int depthIndex = previousNumbers.size() - 1 - i;
                    list.set(depthIndex, list.get(depthIndex) + 1);
                }
                recursion(previousNumbers, l1, currentDepth + 1, MAX_DEPTH);
                recursion(previousNumbers, l2, currentDepth + 1, MAX_DEPTH);
            } else {
                recursion(previousNumbers, currentNumber * 2024, currentDepth + 1, MAX_DEPTH);
            }
        }

        previousNumbers.remove(previousNumbers.size() - 1L);
    }

 */





    // Returns the number of splits that occur from this stone forward.
    long countSplits(long stone, int currentDepth, int MAX_DEPTH, Map<Long, Long> cache) {
        // If we've reached or exceeded MAX_DEPTH, return 0 splits.
        if (currentDepth >= MAX_DEPTH) return 0;

        // Check if we have cached results (optionally keyed by stone+depth if needed)
        // For simplicity, assume just caching by stone regardless of depth:
        if (cache.containsKey(stone)) {
            // If the cached depth isn't right, handle that logic here.
            // If acceptable, just return the cached value.
            return cache.get(stone);
        }

        // Determine the action based on the stone:
        String s = String.valueOf(stone);
        long splits = 0;

        if (stone == 0) {
            // Stone becomes 1 after one iteration, no splits here.
            // Just proceed to next iteration with stone=1
            splits += countSplits(1, currentDepth + 1, MAX_DEPTH, cache);
        } else if (s.length() % 2 == 0) {
            // Even number of digits => split into two stones
            int mid = s.length() / 2;
            long left = Long.parseLong(s.substring(0, mid));
            long right = Long.parseLong(s.substring(mid));

            // One split occurred here
            splits = 1;
            // Now, the resulting two stones each continue evolving:
            splits += countSplits(left, currentDepth + 1, MAX_DEPTH, cache);
            splits += countSplits(right, currentDepth + 1, MAX_DEPTH, cache);
        } else {
            // Odd number of digits => multiply by 2024
            long newStone = stone * 2024;
            splits += countSplits(newStone, currentDepth + 1, MAX_DEPTH, cache);
        }

        // Cache the result for this stone
        // If depth matters, incorporate it in the key or store depth info.
        cache.put(stone, splits);
        return splits;
    }


    // Returns the number of splits that occur from this stone forward.
    long countSplits2(long stone, int currentDepth, int MAX_DEPTH, Map<Long, Long> cache) {
        // If we've reached or exceeded MAX_DEPTH, return 0 splits.
        if (currentDepth >= MAX_DEPTH) return 0;

        // Check if we have cached results (optionally keyed by stone+depth if needed)
        // For simplicity, assume just caching by stone regardless of depth:
        if (cache.containsKey(stone)) {
            // If the cached depth isn't right, handle that logic here.
            // If acceptable, just return the cached value.
            return cache.get(stone);
        }

        // Determine the action based on the stone:
        String s = String.valueOf(stone);
        long splits = 0;

        if (stone == 0) {
            // Stone becomes 1 after one iteration, no splits here.
            // Just proceed to next iteration with stone=1
            splits += countSplits2(1, currentDepth + 1, MAX_DEPTH, cache);
        } else if (s.length() % 2 == 0) {
            // Even number of digits => split into two stones
            int mid = s.length() / 2;
            long left = Long.parseLong(s.substring(0, mid));
            long right = Long.parseLong(s.substring(mid));

            // One split occurred here
            splits = 1;
            // Now, the resulting two stones each continue evolving:
            splits += countSplits2(left, currentDepth + 1, MAX_DEPTH, cache);
            splits += countSplits2(right, currentDepth + 1, MAX_DEPTH, cache);
        } else {
            // Odd number of digits => multiply by 2024
            long newStone = stone * 2024;
            splits += countSplits2(newStone, currentDepth + 1, MAX_DEPTH, cache);
        }

        // Cache the result for this stone
        // If depth matters, incorporate it in the key or store depth info.
        cache.put(stone, splits);
        return splits;
    }


/*
    void recursion2(List<Long> previousNumbers, long currentNumber, int currentDepth, final int MAX_DEPTH) {
        if (currentDepth > MAX_DEPTH) return;

        previousNumbers.add(currentNumber);
        if (trackedDepths.containsKey(currentNumber)) {
            int depth = trackedDepths.get(currentNumber);
            if (depth <= currentDepth) {
                // previousNumbers.get(0) is the root
                Map.Entry<Long, Integer> key = new AbstractMap.SimpleEntry<>(previousNumbers.get(0), currentDepth);
                Map.Entry<Long, Integer> entry = new AbstractMap.SimpleEntry<>(currentNumber, currentDepth - depth);
                if (!addCounts.containsKey(key)) {
                    addCounts.put(key, new ArrayList<>());
                }
                addCounts.get(key).add(entry);
            } else {
                int depthDifference = currentDepth - depth;
                trackedDepths.put(currentNumber, currentDepth);
                depthSplits.put(currentNumber, new ArrayList<>());

                // Adjust all references in addCounts for this currentNumber
                for (var auto : addCounts.values()) {
                    for (Map.Entry<Long, Integer> e : auto) {
                        if (e.getKey().equals(currentNumber)) {
                            e.setValue(e.getValue() + depthDifference);
                        }
                    }
                }
            }
            previousNumbers.remove(previousNumbers.size() - 1);
            return;
        }

        // Ensure that each number in previousNumbers has depthSplits initialized and sized correctly
        for (long l : previousNumbers) {
            if (!depthSplits.containsKey(l)) {
                depthSplits.put(l, new ArrayList<>());
                trackedDepths.put(l, currentDepth);
            }
            while (depthSplits.get(l).size() <= currentDepth) {
                depthSplits.get(l).add(0L);
            }
        }

        if (currentNumber == 0) {
            // 0 transforms into 1 at the next depth
            recursion2(previousNumbers, 1, currentDepth + 1, MAX_DEPTH);
        } else {
            String numberStr = String.valueOf(currentNumber);
            if (numberStr.length() % 2 == 0) {
                // Even number of digits => one split event
                List<Long> currentList = depthSplits.get(currentNumber);
                // Ensure currentList is large enough for currentDepth
                while (currentList.size() <= currentDepth) {
                    currentList.add(0L);
                }
                // Increment one split event for this stone at this depth
                currentList.set(currentDepth, currentList.get(currentDepth) + 1);

                long l1 = Long.parseLong(numberStr.substring(0, numberStr.length() / 2));
                long l2 = Long.parseLong(numberStr.substring(numberStr.length() / 2));
                recursion2(previousNumbers, l1, currentDepth + 1, MAX_DEPTH);
                recursion2(previousNumbers, l2, currentDepth + 1, MAX_DEPTH);
            } else {
                // Odd number of digits => multiply by 2024
                long newNumber = currentNumber * 2024;
                recursion2(previousNumbers, newNumber, currentDepth + 1, MAX_DEPTH);
            }
        }

        previousNumbers.removeLast();
    }


 */




    public void part1_old() {
        String out = "part1 >> ";

        long totalStones = 0;
        for (long stone : stones) {
            int MAX_DEPTH = 25;

            //*
            recursion(new ArrayList<>(), stone, 0, MAX_DEPTH);
            // # of splits is exponent in number of resulting stones.
            for (int depth = 0; depth < MAX_DEPTH; depth++) {
                // Also take the references into account, and loop over there
            }//*/

            /*
            long splits = countSplits(stone, 0, MAX_DEPTH, new HashMap<>());
            //long stones = 1L << splits;
            long stones = 1L + splits;
            totalStones += stones;
            System.out.println(splits);
             */
        }
        out += totalStones;

        System.out.println(out);
    }


    public void part1() {
        String out = "part1 >> ";

        long totalStonesAcrossAll = 0;
        for (long stone : stones) {
            recursion(new ArrayList<>(), stone, -2, 4);
            long exponent = depthSplits.getOrDefault(stone, 0L);
            for (long ref : addCounts.getOrDefault(stone, new ArrayList<>())) {
                exponent += depthSplits.getOrDefault(ref, 0L);
            }
            System.out.println(exponent);
            totalStonesAcrossAll += exponent + 1;
        }

        out += totalStonesAcrossAll;
        System.out.println(out);
    }

    public void iterate(int iterations) {
        for (int k = 0; k < iterations; k++) {
            System.out.println("Iteration #" + k);
            for (int i = 0; i < stones.size(); i++) {
                long value = stones.get(i);
                if (value == 0) {
                    stones.set(i, 1L);
                    continue;
                }
                String val = String.valueOf(value);
                if ((val.length() & 1) == 0) {
                    stones.set(i, Long.parseLong(val.substring(0, val.length() / 2)));
                    stones.add(++i, Long.parseLong(val.substring(val.length() / 2)));
                    continue;
                }
                stones.set(i, value * 2024);
            }
        }
    }

    public void part1_old2() {
        String out = "part1 >> ";

        iterate(ITERATIONS);

        out += stones.size();
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        int result = 0;

        //iterate(ITERATIONS_PT2 - ITERATIONS);

        out += result;
        System.out.println(out);
    }
}
