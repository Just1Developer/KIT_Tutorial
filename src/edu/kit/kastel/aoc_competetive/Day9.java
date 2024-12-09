package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.List;

public class Day9 extends FileReader {

    private static final int DAY = 9;
    private final String[] lines;

    List<Integer> memoryL;
    List<Block> memory;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day9(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        memoryL = new ArrayList<>();
        memory = new ArrayList<>();
        for (int i = 0, id = 0; i < lines[0].length(); i++, id += i & 1) {
            int n;
            if ((i & 1) == 1) n = -1;
            else n = id;
            for (int j = 0; j < lines[0].charAt(i) - '0'; j++) {
                memoryL.add(n);
            }
            memory.add(new Block(n, lines[0].charAt(i) - '0'));
        }
    }

    int findFirst(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == -1) return i;
        }
        return -1;
    }

    int findLastThatIsnt(List<Integer> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) != -1) return i;
        }
        return -1;
    }

    int findFirstThatCanAccompany(List<Block> list, int minSize) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).value == -1 && list.get(i).length >= minSize) return i;
        }
        return list.size() + 1;
    }

    public void part1() {
        String out = "part1 >> ";
        while (true) {
            int firstFree = findFirst(memoryL);
            int lastTaken = findLastThatIsnt(memoryL);
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

        int movingBlock = memory.size() - 1;
        int lastBlockMovedValue = memory.size() + 1;
        while (true) {
            while (movingBlock >= 0 && (memory.get(movingBlock).value == -1 || memory.get(movingBlock).value >= lastBlockMovedValue)) movingBlock--;
            if (movingBlock == -1) break;
            // We have a new block that wants to be moved
            Block block = new Block(memory.get(movingBlock));
            lastBlockMovedValue = block.value;
            // Where should it go?
            int index = findFirstThatCanAccompany(memory, block.length);
            if (index > movingBlock) continue;  // We would move it to the right
            Block space = memory.get(index);

            memory.get(movingBlock).value = -1;
            if (space.length == block.length) {
                space.value = block.value;
            } else {
                memory.add(index, new Block(block));
                memory.get(index + 1).length -= block.length;
            }
        }

        long checksum = 0, id = 0;
        for (Block block : memory) {
            if (block.value == -1) { id += block.length; continue; }
            for (int i = 0; i < block.length; i++) {
                checksum += block.value * id;
                id++;
            }
        }
        out += checksum;

        System.out.println(out);
    }

    String listToString(List<Block> memory) {
        // Convert to string because I'm lazy and I wanna print it too
        StringBuilder builder = new StringBuilder();
        for (Block block : memory) {
            String s = block.value == -1 ? "." : "%d".formatted(block.value);
            builder.append(s.repeat(block.length));
        }
        return builder.toString();
    }

    static class Block {
        // Memory Block
        int value;
        int length;

        public Block(int value, int length) {
            this.value = value;
            this.length = length;
        }
        public Block(Block block) {
            this.value = block.value;
            this.length = block.length;
        }

        @Override
        public String toString() {
            return "{value: %d, length: %d}".formatted(value, length);
        }
    }
}
