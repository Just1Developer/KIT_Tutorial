package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Day16 extends FileReader {

    private static final int DAY = 16;
    private String[] lines;

    List<Tile[][]> maze;
    int startX, startY;
    int endX, endY;

    int COST_TURN = 1000;
    int COST_STRAIGHT = 1;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day16(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        maze = new ArrayList<>();


        int y = 0;
        for (String line : lines) {
            Tile[][] tiles = new Tile[line.length()][4];
            int x = -1;
            for (char c : line.toCharArray()) {
                x++;
                if (c == '#') {
                    for (int i = 0; i < 4; i++) {
                        tiles[x][i] = new Tile(x, y, -1, i);
                    }
                }
                else if (c == 'S') {
                    startX = x;
                    startY = y;
                    tiles[x][1] = new Tile(x, y, 0, 1);
                } else {
                    tiles[x][0] = new Tile(x, y, Integer.MAX_VALUE, 0);
                    if (c == 'E') {
                        endX = x;
                        endY = y;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    if (tiles[x][i] == null) tiles[x][i] = new Tile(x, y, Integer.MAX_VALUE, i);
                }
            }
            maze.add(tiles);
            y++;
        }
    }

    void print() {
        for (Tile[][] line : maze) {
            for (Tile[] row : line) {
                Tile t = null;
                for (Tile dir : row) {
                    if (t == null || dir.minValue < t.minValue) t = dir;
                }
                System.out.print(t);
            }
            System.out.println();
        }
        System.out.println();
    }

    Tile getMin(int x, int y) {
        Tile t = null;
        for (Tile dir : maze.get(y)[x]) {
            if (t == null || dir.minValue < t.minValue) t = dir;
        }
        return t;
    }

    int endDirectionForShortest;

    int findPath() {
        Queue<Tile> q = new LinkedList<>();
        q.add(maze.get(startY)[startX][1]); // 1 is start direction
        int minCost = Integer.MAX_VALUE;
        while (!q.isEmpty()) {
            Tile t = q.poll();

            // If is edge, cancel
            if (t.minValue == -1) continue;

            Tile rotate1 = new Tile(t.x, t.y, t.minValue + COST_TURN, (t.direction + 1) % 4);
            Tile rotate2 = new Tile(t.x, t.y, t.minValue + COST_TURN, t.direction == 0 ? 3 : t.direction - 1);
            Tile walk = new Tile(t.x + (t.direction == 1 ? 1 : t.direction == 3 ? -1 : 0), t.y + (t.direction == 2 ? 1 : t.direction == 0 ? -1 : 0), t.minValue + COST_STRAIGHT, t.direction);
            if (maze.get(walk.y)[walk.x][walk.direction].minValue >= walk.minValue && walk.minValue < minCost) {
                // Never happens with edges / walls
                q.add(walk);
                if (maze.get(walk.y)[walk.x][walk.direction].minValue > walk.minValue) {
                    walk.backPointers.clear();
                }
                walk.backPointers.add(new int[] { t.x, t.y, t.direction });
                maze.get(walk.y)[walk.x][walk.direction] = walk;
                if (walk.x == endX && walk.y == endY) {
                    minCost = walk.minValue;
                }
            }
            if (maze.get(t.y)[t.x][rotate1.direction].minValue >= rotate1.minValue && rotate1.minValue < minCost) {
                q.add(rotate1);
                if (maze.get(t.y)[t.x][rotate1.direction].minValue > rotate1.minValue) {
                    rotate1.backPointers.clear();
                }
                rotate1.backPointers.add(new int[] { t.x, t.y, t.direction });
                maze.get(t.y)[t.x][rotate1.direction] = rotate1;
            }
            if (maze.get(t.y)[t.x][rotate2.direction].minValue >= rotate2.minValue && rotate2.minValue < minCost) {
                q.add(rotate2);
                if (maze.get(t.y)[t.x][rotate2.direction].minValue > rotate2.minValue) {
                    rotate2.backPointers.clear();
                }
                rotate2.backPointers.add(new int[] { t.x, t.y, t.direction });
                maze.get(t.y)[t.x][rotate2.direction] = rotate2;
            }
        }
        return minCost;
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        result = findPath();
        print();

        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ---";
        int result = 0;

        // Todo for (all minimums of finish tile, find all backpointers and do it again, until there are no more backpointers)
        // Path already found
        int currentX, currentY, currentDirection;

        out += result;
        System.out.println(out);
    }

    static class Tile {
        int backPtrX = -1, backPtrY = -1, backPtrDirection = -1;
        List<int[]> backPointers = new ArrayList<>();

        final int x, y;
        final int minValue;
        /**
         * 0 = up, then clockwise
         */
        final int direction;

        public Tile(int x, int y, int minValue, int direction) {
            this.x = x;
            this.y = y;
            this.minValue = minValue;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return minValue == tile.minValue && direction == tile.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(minValue, direction);
        }

        @Override
        public String toString() {
            return new String[] {"^", ">", "v", "<"}[direction];
        }
    }
}
