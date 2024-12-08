package edu.kit.kastel.aoc_competetive.y2023;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Day 10 part 2 of 2023.
 */
public class Day10 extends FileReader {
    private static int DAY = 10;

    private String[] lines;

    char[][] map;

    final List<Character> CONNECT_UPWARDS = List.of('|', 'J', 'L');
    final List<Character> CONNECT_RIGHTWARDS = List.of('-', 'F', 'L');
    final List<Character> CONNECT_DOWNWARDS = List.of('|', 'F', '7');
    final List<Character> CONNECT_LEFTWARDS = List.of('-', 'J', '7');

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day10(int exampleNr) {
        String filepath = "./input/2023/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        HashMap<Integer, Character> startPositions = new HashMap<>();
        // Up, Right, Down, Left
        startPositions.put(0b1100, 'L');
        startPositions.put(0b1010, '|');
        startPositions.put(0b1001, 'J');
        startPositions.put(0b0110, 'F');
        startPositions.put(0b0011, '7');
        startPositions.put(0b0101, '-');

        // Identify and replace the start position

        Coord start = null;

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                if (lines[i].charAt(j) != 'S') continue;
                start = new Coord(j, i);
                int posValue = 0;
                if (j > 0 && CONNECT_RIGHTWARDS.contains(lines[i].charAt(j - 1))) posValue |= 0b0001;                       // Check Left
                if (j < lines[i].length() - 1 && CONNECT_LEFTWARDS.contains(lines[i].charAt(j + 1))) posValue |= 0b0100;    // Check Right
                if (i > 0 && CONNECT_DOWNWARDS.contains(lines[i - 1].charAt(j))) posValue |= 0b1000;                        // Check Up
                if (i < lines.length - 1 && CONNECT_UPWARDS.contains(lines[i + 1].charAt(j))) posValue |= 0b0010;           // If the one below is connected upwards and so on

                if (!startPositions.containsKey(posValue)) {
                    throw new IllegalArgumentException("Bytevalue was %h".formatted(posValue));
                }
                lines[i] = lines[i].substring(0, j) + startPositions.get(posValue) + lines[i].substring(j + 1);

                break;
            }
        }
        // Print map
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println();

        if (start == null) {
            System.out.println("start was null.");
            return;
        }

        // Now, find the loop

        Set<Coord> loopCoords = new HashSet<>();
        Coord currentLocation = start;  // Coord is immutable, reference is okay

        // Todo still a bug with example 4 somehow looping, but the actual sample doesn't have that problem, so it's fine
        // Woohoo we're done. It took a year but we got there. Day 10 Part 2 finished

        do {
            // First, move:
            char current = getChar(lines, currentLocation);
            loopCoords.add(currentLocation);
            // Check up:
            if (CONNECT_UPWARDS.contains(current) && CONNECT_DOWNWARDS.contains(getChar(lines, currentLocation, -1, 0)) && (!loopCoords.contains(currentLocation.add(0, -1)) || start.equals(currentLocation.add(0, -1)))) {
                currentLocation = currentLocation.add(0, -1);
            } else if (CONNECT_RIGHTWARDS.contains(current) && CONNECT_LEFTWARDS.contains(getChar(lines, currentLocation, 0, 1)) && (!loopCoords.contains(currentLocation.add(1, 0)) || start.equals(currentLocation.add(1, 0)))) {
                currentLocation = currentLocation.add(1, 0);
            } else if (CONNECT_DOWNWARDS.contains(current) && CONNECT_UPWARDS.contains(getChar(lines, currentLocation, 1, 0)) && (!loopCoords.contains(currentLocation.add(0, 1)) || start.equals(currentLocation.add(0, 1)))) {
                currentLocation = currentLocation.add(0, 1);
            } else if (CONNECT_LEFTWARDS.contains(current) && CONNECT_RIGHTWARDS.contains(getChar(lines, currentLocation, 0, -1)) && (!loopCoords.contains(currentLocation.add(-1, 0)) || start.equals(currentLocation.add(-1, 0)))) {
                currentLocation = currentLocation.add(-1, 0);
            }
            loopCoords.add(currentLocation);
        } while (!currentLocation.equals(start));   // Finished loop

        // Print map
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                System.out.print(loopCoords.contains(new Coord(j, i)) ? lines[i].charAt(j) : '.');
            }
            System.out.println();
        }
        System.out.println();

        // Finally, construct the map with what we know

        map = new char[lines.length * 3][];
        for (int i = 0; i < lines.length; i++) {
            map[3 * i] = new char[lines[i].length() * 3];
            map[3 * i + 1] = new char[lines[i].length() * 3];
            map[3 * i + 2] = new char[lines[i].length() * 3];
            for (int j = 0; j < lines[i].length(); j++) {
                fill(3 * i, 3 * j, loopCoords.contains(new Coord(j, i)) ? lines[i].charAt(j) : '.');
            }
        }
    }

    char getChar(String[] map, Coord coord) { return getChar(map, coord, 0, 0); }
    char getChar(String[] map, Coord coord, int yOffset, int xOffset) {
        int y = coord.getY() + yOffset;
        int x = coord.getX() + xOffset;
        if (x < 0 || y < 0 || y >= map.length || x > map[y].length()) return '.';
        return map[y].charAt(x);
    }

    void fill(int upperLeftY, int upperLeftX, char c) {
        char[][] symbol = switch (c) {
            case '|' -> new char[][] {
                    {'.','#','.'},
                    {'.','#','.'},
                    {'.','#','.'}};
            case '-' -> new char[][] {
                    {'.','.','.'},
                    {'#','#','#'},
                    {'.','.','.'}};
            case 'L' -> new char[][] {
                    {'.','#','.'},
                    {'.','#','#'},
                    {'.','.','.'}};
            case 'J' -> new char[][] {
                    {'.','#','.'},
                    {'#','#','.'},
                    {'.','.','.'}};
            case '7' -> new char[][] {
                    {'.','.','.'},
                    {'#','#','.'},
                    {'.','#','.'}};
            case 'F' -> new char[][] {
                    {'.','.','.'},
                    {'.','#','#'},
                    {'.','#','.'}};
            default -> new char[][] {
                    {'.','.','.'},
                    {'.','.','.'},
                    {'.','.','.'}};
        };
        map[upperLeftY][upperLeftX] = symbol[0][0]; map[upperLeftY][upperLeftX + 1] = symbol[0][1]; map[upperLeftY][upperLeftX + 2] = symbol[0][2];
        map[upperLeftY + 1][upperLeftX] = symbol[1][0]; map[upperLeftY + 1][upperLeftX + 1] = symbol[1][1]; map[upperLeftY + 1][upperLeftX + 2] = symbol[1][2];
        map[upperLeftY + 2][upperLeftX] = symbol[2][0]; map[upperLeftY + 2][upperLeftX + 1] = symbol[2][1]; map[upperLeftY + 2][upperLeftX + 2] = symbol[2][2];
    }

    void printMap() {
        printMap(map);
    }
    void printMap(char[][] map) {
        for (char[] _map : map) {
            if (_map == null) { System.out.println("null"); continue; }
            for (char c : _map) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public void part1() {
        String out = "part1 >> ---";
        printMap();
        System.out.println(out);
    }

    char[][] shrink(char[][] map) {
        char[][] newMap = new char[map.length / 3][];
        for (int i = 1, y = 0; i < map.length; i += 3, y++) {
            newMap[y] = new char[map[i].length / 3];
            for (int j = 1, x = 0; j < map[i].length; j += 3, x++) {
                newMap[y][x] = map[i][j];
            }
        }
        return newMap;
    }

    public void part2() {
        String out = "part2 >> ";

        char[][] mapClone = new char[map.length][];
        for (int i = 0; i < map.length; i++) {
            mapClone[i] = new char[map[i].length];
            System.arraycopy(map[i], 0, mapClone[i], 0, map[i].length);
        }

        // Fill map:
        Set<Coord> handled = new HashSet<>();
        Queue<Coord> checkThese = new LinkedList<>();
        checkThese.add(new Coord(0, 0));
        while (!checkThese.isEmpty()) {
            Coord coord = checkThese.remove();
            if (!handled.add(coord)) continue;
            // Add it's 4 neighbors, if their char is '.'
            if (coord.getY() > 0 && mapClone[coord.getY() - 1][coord.getX()] == '.') checkThese.offer(coord.add(0, -1));    // Up
            if (coord.getX() > 0 && mapClone[coord.getY()][coord.getX() - 1] == '.') checkThese.offer(coord.add(-1, 0));    // Left
            if (coord.getY() < map.length - 1 && mapClone[coord.getY() + 1][coord.getX()] == '.') checkThese.offer(coord.add(0, 1));    // Down
            if (coord.getX() < map[coord.getY()].length - 1 && mapClone[coord.getY()][coord.getX() + 1] == '.') checkThese.offer(coord.add(1, 0));    // Right
            mapClone[coord.getY()][coord.getX()] = 'X';
        }
        printMap(mapClone);

        System.out.println();
        var shrunkMap = shrink(mapClone);

        int enclosed = 0;
        for (char[] ar : shrunkMap) {
            for (char c : ar) {
                if (c == '.') enclosed++;
            }
        }
        out += enclosed;

        printMap(shrunkMap);

        System.out.println(out);
    }
}
