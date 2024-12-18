package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 extends FileReader {

    private static final int DAY = 18;
    private String[] lines;

    List<Coord> fallingBytes;
    Tile[][] ram;
    int startX, startY;
    int endX, endY;

    final int BYTE_LIMIT;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day18(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        startX = 0;
        startY = 0;
        if (exampleNr == 0) {
            endX = 70;
            endY = 70;
            BYTE_LIMIT = 1024;
        } else {
            endX = 6;
            endY = 6;
            BYTE_LIMIT = 12;
        }

        ram = new Tile[endX + 1][endY + 1];
        fallingBytes = new ArrayList<>();

        for (int i = 0; i < endX + 1; i++) {
            for (int j = 0; j < endY + 1; j++) {
                ram[i][j] = new Tile(j, i, Integer.MAX_VALUE);
            }
        }
        ram[0][0].minValue = 0;

        Pattern p = Pattern.compile("^(\\d+),(\\d+)");
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (!m.matches()) {
                System.out.println("Error: invalid line " + line);
                continue;
            }
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            fallingBytes.add(new Coord(x, y));
        }
    }

    void print() {
        for (Tile[] line : ram) {
            for (Tile t : line) {
                System.out.print(t);
            }
            System.out.println();
        }
        System.out.println();
    }

    int findPath() {
        Queue<Tile> q = new LinkedList<>();
        q.add(ram[startY][startX]);
        int minCost = Integer.MAX_VALUE;
        while (!q.isEmpty()) {
            Tile t = q.remove();

            // If is edge, cancel
            if (t.minValue == -1) continue;
            if (t.corrupted) continue;
            if (t.isOutOfBounds()) continue;

            if (t.y > 0) {
                // Move up
                Tile up = ram[t.y - 1][t.x];
                if (!up.corrupted && up.minValue > t.minValue + 1) {
                    up.minValue = t.minValue + 1;
                    q.add(up);
                }
            }
            if (t.y < ram.length - 1) {
                // Move down
                Tile down = ram[t.y + 1][t.x];
                if (!down.corrupted && down.minValue > t.minValue + 1) {
                    down.minValue = t.minValue + 1;
                    q.add(down);
                }
            }

            if (t.x > 0) {
                // Move left
                Tile left = ram[t.y][t.x - 1];
                if (!left.corrupted && left.minValue > t.minValue + 1) {
                    left.minValue = t.minValue + 1;
                    q.add(left);
                }
            }
            if (t.x < ram[t.y].length - 1) {
                // Move right
                Tile right = ram[t.y][t.x + 1];
                if (!right.corrupted && right.minValue > t.minValue + 1) {
                    right.minValue = t.minValue + 1;
                    q.add(right);
                }
            }
        }
        return ram[endY][endX].minValue;
    }

    int minScore;

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        for (int i = 0; i < Math.min(fallingBytes.size(), BYTE_LIMIT); i++) {
            Coord c = fallingBytes.get(i);
            ram[c.getY()][c.getX()].minValue = Integer.MAX_VALUE;
            ram[c.getY()][c.getX()].corrupted = true;
        }
        print();

        result = findPath();
        minScore = result;
        print();

        out += result;
        System.out.println(out);
    }

    void reset() {
        for (int y = 0; y < ram.length; y++) {
            for (int x = 0; x < ram[y].length; x++) {
                if (!ram[y][x].corrupted) ram[y][x].minValue = Integer.MAX_VALUE;
                //if (resetCorrupted) ram[y][x].corrupted = false;
            }
        }
        ram[0][0].minValue = 0;
    }

    public void part2() {
        String out = "part2 >> ";

        Coord coordinate = null;
        for (int i = BYTE_LIMIT; i < fallingBytes.size(); i++) {
            reset();
            coordinate = fallingBytes.get(i);
            ram[coordinate.getY()][coordinate.getX()].corrupted = true;
            if (findPath() == Integer.MAX_VALUE) {
                break;
            }
        }

        out += coordinate;
        System.out.println(out);
    }

    private class Tile {
        List<int[]> backPointers = new ArrayList<>();

        final int x, y;
        int minValue;

        boolean corrupted;

        public Tile(int x, int y, int minValue, boolean corrupted) {
            this.x = x;
            this.y = y;
            this.minValue = minValue;
            this.corrupted = corrupted;
        }
        public Tile(int x, int y, int minValue) {
            this.x = x;
            this.y = y;
            this.minValue = minValue;
            this.corrupted = false;
        }

        public boolean isOutOfBounds() {
            return x < 0 || y < 0 || y >= ram.length || x >= ram[y].length;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return minValue == tile.minValue;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minValue);
        }

        @Override
        public String toString() {
            return corrupted ? "#" : ".";
        }
    }
}
