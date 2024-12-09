package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Day9 extends FileReader {

    private static final int DAY = 9;
    private final String[] lines;

    List<Integer> memoryL;
    String memory, memoryRev;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day9(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        memoryL = new ArrayList<>();
        /*
        memoryFwd = new int[lines[0].length()];
        memoryBwd = new int[lines[0].length()];
        for (int i = 0; i < lines[0].length(); i++) {
            memoryFwd[i] = lines[0].charAt(i) - '0';
            memoryBwd[memoryBwd.length - 1 - i] = lines[0].charAt(i) - '0';
        }*/
        StringBuilder memoryBuilder = new StringBuilder();
        StringBuilder memoryRevBuilder = new StringBuilder();
        for (int i = 0, id = 0; i < lines[0].length(); i++, id += i & 1) {
            String insert, insert2;
            if ((i & 1) == 1) {
                insert = ".".repeat(lines[0].charAt(i) - '0');
                insert2 = insert;
                for (int j = 0; j < lines[0].charAt(i) - '0'; j++) {
                    memoryL.add(-1);
                }
            } else {
                insert = "%d".formatted(id).repeat(lines[0].charAt(i) - '0');
                insert2 = "X".repeat(lines[0].charAt(i) - '0');
                for (int j = 0; j < lines[0].charAt(i) - '0'; j++) {
                    memoryL.add(id);
                }
            }
            memoryBuilder.append(insert);
            memoryRevBuilder.insert(0, insert2);
        }

        memory = memoryBuilder.toString();
        memoryRev = memoryRevBuilder.toString();
    }

    int findFirst(List<Integer> list, int num) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == num) return i;
        }
        return -1;
    }

    int findLastThatIsnt(List<Integer> list, int num) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) != num) return i;
        }
        return -1;
    }

    String swapChars(String s, int i, int j) {
        if (i > j) {
            int t = i;
            i = j;
            j = t;
        }
        return (i == 0 ? "" : s.substring(0, i)) + s.charAt(j) + s.substring(i + 1, j) + s.charAt(i) + (j >= s.length() - 1 ? "" : s.substring(j + 1));
    }

    public void part1() {
        String out = "part1 >> ";
/*
        String mem = memory, memRev = memoryRev;
        final int LAST_INDEX = memoryRev.length() - 1;

        while (true) {
            int firstFree = mem.indexOf('.');
            int lastTaken = memRev.indexOf('X');
            if (firstFree > LAST_INDEX - lastTaken) break;
            mem = swapChars(mem, firstFree, LAST_INDEX - lastTaken);
            memRev = swapChars(memRev, LAST_INDEX - firstFree, lastTaken);

            /*
            System.out.println(mem);
            System.out.println(memRev);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }* /
        }

        int checksum = 0;
        for (int i = 0; i < mem.length(); i++) {
            if (mem.charAt(i) == '.') break;
            checksum += (mem.charAt(i) - '0') * i;
        }
        out += checksum;
 */

        while (true) {
            int firstFree = findFirst(memoryL, -1);
            int lastTaken = findLastThatIsnt(memoryL, -1);
            if (firstFree > lastTaken) break;
            // Swap entries, we know List.get(firstFree) == -1
            memoryL.set(firstFree, memoryL.get(lastTaken));
            memoryL.set(lastTaken, -1);
        }

        long checksum = 0;
        for (int i = 0; i < memoryL.size(); i++) {
            if (memoryL.get(i) == -1) break;
            checksum += memoryL.get(i) * i;
        }
        out += checksum;


        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";



        System.out.println(out);
    }
}
