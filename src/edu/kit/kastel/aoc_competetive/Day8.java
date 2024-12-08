package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 extends FileReader {

    private static final int DAY = 8;
    private final String[] lines;

    char[][] map;
    Map<Character, List<Coord>> antennas;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day8(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        map = new char[lines.length][];
        antennas = new HashMap<>();

        Pattern pattern = Pattern.compile("[a-zA-Z\\d]");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            map[y] = line.toCharArray();
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (!pattern.matcher(c + "").matches()) continue;
                if (!antennas.containsKey(c)) antennas.put(c, new ArrayList<>());
                antennas.get(c).add(new Coord(x, y));
            }
        }
    }

    public void part1() {
        String out = "part1 >> ";

        Set<Coord> antinode = new HashSet<>();

        for (char freq : antennas.keySet()) {
            var list = antennas.get(freq);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (i == j) continue;
                    Coord coord1 = list.get(i);
                    Coord coord2 = list.get(j);
                    Coord diff = coord2.subtract(coord1);
                    Coord newCoord1 = coord1.subtract(diff);
                    Coord newCoord2 = coord2.add(diff);

                    if (newCoord1.isInBounds(map) /*&& map[newCoord1.getY()][newCoord1.getX()] == '.'*/) antinode.add(newCoord1);
                    if (newCoord2.isInBounds(map) /*&& map[newCoord2.getY()][newCoord2.getX()] == '.'*/) antinode.add(newCoord2);
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (antinode.contains(new Coord(j, i))) System.out.print('#');
                else System.out.print(map[i][j]);
            }
            System.out.println();
        }

        out += antinode.size();
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";

        Set<Coord> antinode = new HashSet<>();

        for (char freq : antennas.keySet()) {
            var list = antennas.get(freq);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (i == j) continue;
                    Coord coord1 = list.get(i);
                    Coord coord2 = list.get(j);
                    Coord diff = coord2.subtract(coord1);

                    Coord newCoord1 = coord1, newCoord2 = coord2;
                    do {
                        antinode.add(newCoord1);
                        newCoord1 = newCoord1.subtract(diff);
                    } while (newCoord1.isInBounds(map));
                    do {
                        antinode.add(newCoord2);
                        newCoord2 = newCoord2.add(diff);
                    } while (newCoord2.isInBounds(map));
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (antinode.contains(new Coord(j, i)) && map[i][j] == '.') System.out.print('#');
                else System.out.print(map[i][j]);
            }
            System.out.println();
        }

        out += antinode.size();
        System.out.println(out);
    }
}
