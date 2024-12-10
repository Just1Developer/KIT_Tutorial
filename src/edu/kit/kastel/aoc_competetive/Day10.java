package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Day10 extends FileReader {

    private static final int DAY = 10;
    private final String[] lines;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day10(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);


    }

    static class Path {
        List<Coord> path;

        public Path() {
            path = new ArrayList<>();
        }
        public Path(Coord root) {
            this();
            path.add(root);
        }
        public Path add(Coord coord) {
            Path newPath = new Path();
            newPath.path.addAll(this.path);
            newPath.path.add(coord);
            return newPath;
        }
        public int length() {
            return path.size();
        }
        public Coord getTip() {
            return path.get(length() - 1);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Path path1 = (Path) o;
            return Objects.equals(path, path1.path);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(path);
        }
    }

    int scoreOf(Coord coord) {
        return coord.isInBounds(lines) ? lines[coord.getY()].charAt(coord.getX()) - '0' : 0;
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        Map<Coord, Set<Coord>> trailPairs;
        trailPairs = new HashMap<>();
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                int c = lines[y].charAt(x) - '0';
                if (c != 0) continue;

                // Found a head
                Set<Coord> reachable = new HashSet<>();
                Queue<Coord> process = new LinkedList<>();
                Coord root = new Coord(x, y);
                process.add(root);
                while (!process.isEmpty()) {
                    Coord coord = process.remove();
                    // Check all neighbors
                    int score = scoreOf(coord);

                    if (score == 9) {
                        reachable.add(coord);
                        continue;
                    }

                    Coord neighbor = new Coord(coord.getX() + 1, coord.getY());
                    if (scoreOf(neighbor) == score + 1) process.add(neighbor);
                    neighbor = new Coord(coord.getX() - 1, coord.getY());
                    if (scoreOf(neighbor) == score + 1) process.add(neighbor);
                    neighbor = new Coord(coord.getX(), coord.getY() + 1);
                    if (scoreOf(neighbor) == score + 1) process.add(neighbor);
                    neighbor = new Coord(coord.getX(), coord.getY() - 1);
                    if (scoreOf(neighbor) == score + 1) process.add(neighbor);
                }
                trailPairs.put(root, reachable);
            }
        }
        for (var auto : trailPairs.values()) {
            result += auto.size();
        }

        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";

        int result = 0;
        Map<Coord, Set<Path>> trailPairs;
        trailPairs = new HashMap<>();
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                int c = lines[y].charAt(x) - '0';
                if (c != 0) continue;

                // Found a head
                Set<Path> reachable = new HashSet<>();
                Queue<Path> process = new LinkedList<>();
                Coord root = new Coord(x, y);
                process.add(new Path(root));
                while (!process.isEmpty()) {
                    Path path = process.remove();
                    Coord coord = path.getTip();
                    // Check all neighbors
                    int score = scoreOf(coord);

                    if (score == 9) {
                        reachable.add(path);
                        continue;
                    }

                    Coord neighbor = new Coord(coord.getX() + 1, coord.getY());
                    if (scoreOf(neighbor) == score + 1) process.add(path.add(neighbor));
                    neighbor = new Coord(coord.getX() - 1, coord.getY());
                    if (scoreOf(neighbor) == score + 1) process.add(path.add(neighbor));
                    neighbor = new Coord(coord.getX(), coord.getY() + 1);
                    if (scoreOf(neighbor) == score + 1) process.add(path.add(neighbor));
                    neighbor = new Coord(coord.getX(), coord.getY() - 1);
                    if (scoreOf(neighbor) == score + 1) process.add(path.add(neighbor));
                }
                trailPairs.put(root, reachable);
            }
        }
        for (var auto : trailPairs.values()) {
            result += auto.size();
        }
        out += result;

        System.out.println(out);
    }
}
