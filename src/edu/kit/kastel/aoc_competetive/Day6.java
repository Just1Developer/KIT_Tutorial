package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 extends FileReader {

    private static final int DAY = 6;
    private final String[] lines;

    Coord guardPosition;
    int guardDirectionIndex;
    List<Character> guardDirections = List.of('^', '>', 'v', '<');
    private final char obstacle = '#';
    private final char path = '.';
    private final char marked = 'X';
    HashMap<Character, Coord> movementDelta = new HashMap<>();
    Map map;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day6(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        movementDelta.put(guardDirections.get(0), new Coord(0, -1));
        movementDelta.put(guardDirections.get(1), new Coord(1, 0));
        movementDelta.put(guardDirections.get(2), new Coord(0, 1));
        movementDelta.put(guardDirections.get(3), new Coord(-1, 0));

        char[][] map = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            map[i] = lines[i].toCharArray();
            for (int j = 0; j < map[i].length; j++) {
                int index = guardDirections.indexOf(map[i][j]);
                if (index == -1) continue;
                guardDirectionIndex = index;
                guardPosition = new Coord(j, i);
            }
        }
        this.map = new Map(map);
    }

    public void part1() {
        String out = "part1 >> ";
        int sum = 0;

        Coord guardPosition = new Coord(this.guardPosition.x, this.guardPosition.y);
        int guardDirectionIndex = this.guardDirectionIndex;
        Map map = new Map(this.map);
        Set<PositionCombo> comboSet = new HashSet<>();

        while(true) {
            // Go Step
            int beforeX = guardPosition.x; int beforeY = guardPosition.y;
            guardPosition.add(movementDelta.get(guardDirections.get(guardDirectionIndex)));
            if (map.get(beforeX, beforeY) == path) sum++;
            map.set(beforeX, beforeY, marked);

            if (guardPosition.outOfBounds(map.map)) {
                sum++;  // For some reason we are out of bounds here
                break;
            }
            if (!comboSet.add(new PositionCombo(guardPosition, guardDirectionIndex))) break; // Loop
            // Rotate Guard
            if (map.get(guardPosition.x, guardPosition.y) == obstacle) {
                guardDirectionIndex = (guardDirectionIndex + 1) % guardDirections.size();
                guardPosition = new Coord(beforeX, beforeY);    // Reset. Spot is marked as visited, so nothing is added
                //map.print();
            }
        }
        map.print();

        out += sum;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        int sum = 0;

        Coord guardPosition = new Coord(this.guardPosition.x, this.guardPosition.y);
        int guardDirectionIndex = this.guardDirectionIndex;
        Map map = new Map(this.map);
        Set<PositionCombo> comboSet = new HashSet<>();
        Set<Coord> positions = new HashSet<>();

        while(true) {
            // Go Step
            int beforeX = guardPosition.x; int beforeY = guardPosition.y;
            positions.add(new Coord(guardPosition));
            guardPosition.add(movementDelta.get(guardDirections.get(guardDirectionIndex)));
            map.set(beforeX, beforeY, marked);

            if (guardPosition.outOfBounds(map.map)) {
                break;
            }
            if (!comboSet.add(new PositionCombo(guardPosition, guardDirectionIndex))) break; // Loop
            // Rotate Guard
            if (map.get(guardPosition.x, guardPosition.y) == obstacle) {
                guardDirectionIndex = (guardDirectionIndex + 1) % guardDirections.size();
                guardPosition = new Coord(beforeX, beforeY);    // Reset. Spot is marked as visited, so nothing is added
                //map.print();
            }
        }

        for (Coord coord : positions) {
            if (wouldLoopIf(coord)) sum++;
        }

        out += sum;
        System.out.println(out);
    }

    boolean wouldLoopIf(Coord obstacle) {
        Coord guardPosition = new Coord(this.guardPosition.x, this.guardPosition.y);
        int guardDirectionIndex = this.guardDirectionIndex;
        Map map = new Map(this.map);
        Set<PositionCombo> comboSet = new HashSet<>();

        if (obstacle.outOfBounds(map.map)) return false;
        map.set(obstacle, this.obstacle);

        while(true) {
            // Go Step
            int beforeX = guardPosition.x; int beforeY = guardPosition.y;
            guardPosition.add(movementDelta.get(guardDirections.get(guardDirectionIndex)));
            map.set(beforeX, beforeY, marked);

            if (guardPosition.outOfBounds(map.map)) {
                return false;
            }
            if (!comboSet.add(new PositionCombo(guardPosition, guardDirectionIndex))) return true; // Loop
            // Rotate Guard
            if (map.get(guardPosition.x, guardPosition.y) == this.obstacle) {
                guardDirectionIndex = (guardDirectionIndex + 1) % guardDirections.size();
                guardPosition = new Coord(beforeX, beforeY);    // Reset. Spot is marked as visited, so nothing is added
            }
        }
    }

    static class PositionCombo {
        private final Coord coord;
        private final int direction;
        public PositionCombo(Coord coord, int direction) {
            this.coord = new Coord(coord.x, coord.y);
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionCombo that = (PositionCombo) o;
            return direction == that.direction && Objects.equals(coord, that.coord);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord, direction);
        }
    }

    static class Map {
        public char[][] map;

        public Map(char[][] map) {
            this.map = map;
        }
        public Map(Map map) {
            this.map = new char[map.map.length][];
            for (int i = 0; i < map.map.length; i++) {
                this.map[i] = new char[map.map[i].length];
                System.arraycopy(map.map[i], 0, this.map[i], 0, map.map[i].length);
            }
        }

        public char get(int x, int y) {
            return map[y][x];
        }
        public char get(Coord coord) {
            return get(coord.x, coord.y);
        }
        public void set(int x, int y, char marked) {
            map[y][x] = marked;
        }
        public void set(Coord coord, char marked) {
            map[coord.y][coord.x] = marked;
        }
        public void print() {
            System.out.println(this);
        }
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (char[] ch : map) {
                for (char c : ch) {
                    builder.append(c);
                }
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }
    }

    static class Coord {
        private int x, y;
        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public Coord(Coord coord) {
            this.x = coord.x;
            this.y = coord.y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }
        public void add(int x, int y) {
            this.x += x;
            this.y += y;
        }
        public void add(Coord coord) {
            this.x += coord.getX();
            this.y += coord.getY();
        }
        public boolean outOfBounds(char[][] gridForSize) {
            if (x < 0 || y < 0) return true;
            if (y >= gridForSize.length || x >= gridForSize[y].length) return true;
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x && y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
