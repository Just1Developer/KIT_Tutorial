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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20 extends FileReader {

    private static final int DAY = 20;
    private String[] lines;

    char[][] map;
    List<Tile[]> processor;
    List<Tile[]> processorFwd;
    int startX, startY;
    int endX, endY;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day20(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        processor = new ArrayList<>();
        processorFwd = new ArrayList<>();
        map = new char[lines.length][];

        for (int i = 0; i < lines.length; i++) {
            processor.add(new Tile[lines[i].length()]);
            processorFwd.add(new Tile[lines[i].length()]);
            for (int j = 0; j < lines[i].length(); j++) {
                processor.getLast()[j] = new Tile(j, i, Integer.MAX_VALUE);
                processorFwd.getLast()[j] = new Tile(j, i, Integer.MAX_VALUE);
            }
        }

        int y = -1;
        for (String line : lines) {
            y++;
            int x = -1;
            map[y] = line.toCharArray();
            for (char c : map[y]) {
                x++;
                if (c == 'S') {
                    endX = x;
                    endY = y;
                } else if (c == 'E') {
                    startX = x;
                    startY = y;
                }
            }
        }
    }

    void print(List<Tile[]> processor) {
        for (Tile[] line : processor) {
            for (Tile t : line) {
                System.out.print(t);
            }
            System.out.println();
        }
        System.out.println();
    }

    void findPathBackward() {
        Queue<Tile> q = new LinkedList<>();
        q.add(processor.get(startY)[startX]);
        while (!q.isEmpty()) {
            Tile t = q.remove();

            // If is edge, cancel
            if (t.minValue == -1) continue;
            if (t.isOutOfBounds()) continue;

            if (t.y > 0) {
                // Move up
                Tile up = processor.get(t.y - 1)[t.x];
                // Do we need a jump? Do we have a jump?
                enqueueConditional(q, t, up);
            }
            if (t.y < processor.size() - 1) {
                // Move down
                Tile down = processor.get(t.y + 1)[t.x];
                // Do we need a jump? Do we have a jump?
                enqueueConditional(q, t, down);
            }

            if (t.x > 0) {
                // Move left
                Tile left = processor.get(t.y)[t.x - 1];
                // Do we need a jump? Do we have a jump?
                enqueueConditional(q, t, left);
            }
            if (t.x < processor.get(t.y).length - 1) {
                // Move right
                Tile right = processor.get(t.y)[t.x + 1];
                // Do we need a jump? Do we have a jump?
                enqueueConditional(q, t, right);
            }
        }
        //return processor.get(endY)[endX].minValue;
    }

    private void enqueueConditional(Queue<Tile> q, Tile t, Tile neighbor) {
        boolean requireJump = map[neighbor.y][neighbor.x] == '#';
        if (!requireJump || neighbor.hasJumpTemp) {
            if (neighbor.minValue > t.minValue + 1) {
                neighbor.minValue = t.minValue + 1;
                if (requireJump) {
                    neighbor.wallValue = neighbor.hasJump ? 1 : 2;
                    neighbor.hasJumpTemp = neighbor.hasJump;
                    neighbor.hasJump = false;
                } else {
                    if (!neighbor.hasJump) neighbor.hasJumpTemp = false;
                    neighbor.wallValue = 0;
                }
                // If still wall or if we still have a jump
                if (requireJump || neighbor.hasJump) {
                    q.add(neighbor);
                }
            }
        }
    }

    long findPathForward() {
        Queue<Tile> q = new LinkedList<>();
        q.add(processor.get(endY)[endX]);
        long minValue = Integer.MAX_VALUE;
        while (!q.isEmpty()) {
            Tile t = q.remove();

            // If is edge, cancel
            if (t.minValue == -1) continue;
            if (t.isOutOfBounds()) continue;

            if (t.y > 0) {
                // Move up
                Tile up = processorFwd.get(t.y - 1)[t.x];
                boolean isWall = map[up.y][up.x] == '#';
                if (isWall && up.wallValue <= 2) {
                    long score = up.minValue + 1 + t.minValue;
                    if (minValue > score) minValue = score;
                } else if(!isWall && up.minValue > t.minValue + 1) {
                    up.minValue = t.minValue + 1;
                    q.add(up);
                }
            }
            if (t.y < processorFwd.size() - 1) {
                // Move down
                Tile down = processorFwd.get(t.y + 1)[t.x];
                boolean isWall = map[down.y][down.x] == '#';
                if (isWall && down.wallValue <= 2) {
                    long score = down.minValue + 1 + t.minValue;
                    if (minValue > score) minValue = score;
                } else if(!isWall && down.minValue > t.minValue + 1) {
                    down.minValue = t.minValue + 1;
                    q.add(down);
                }
            }

            if (t.x > 0) {
                // Move left
                Tile left = processorFwd.get(t.y)[t.x - 1];
                boolean isWall = map[left.y][left.x] == '#';
                if (isWall && left.wallValue <= 2) {
                    long score = left.minValue + 1 + t.minValue;
                    if (minValue > score) minValue = score;
                } else if(!isWall && left.minValue > t.minValue + 1) {
                    left.minValue = t.minValue + 1;
                    q.add(left);
                }
            }
            if (t.x < processorFwd.get(t.y).length - 1) {
                // Move right
                Tile right = processorFwd.get(t.y)[t.x + 1];
                boolean isWall = map[right.y][right.x] == '#';
                if (isWall && right.wallValue <= 2) {
                    long score = right.minValue + 1 + t.minValue;
                    if (minValue > score) minValue = score;
                } else if(!isWall && right.minValue > t.minValue + 1) {
                    right.minValue = t.minValue + 1;
                    q.add(right);
                }
            }
        }
        return minValue;
    }

    Result findPath(int skippedWall1Y, int skippedWall1X, int skippedWall2Y, int skippedWall2X, long defaultScore) {
        Queue<Tile> q = new LinkedList<>();
        q.add(processor.get(startY)[startX]);
        processor.get(startY)[startX].minValue = 0;
        long minValue = Integer.MAX_VALUE;
        while (!q.isEmpty()) {
            Tile t = q.remove();

            // If is edge, cancel
            if (t.minValue == -1) continue;
            if (t.isOutOfBounds()) continue;

            /*
            if (t.x == endX && t.y == endY) {
                if (t.minValue < minValue) minValue = t.minValue;
            }*/

            if (t.y > 0) {
                // Move up
                Tile up = processor.get(t.y - 1)[t.x];
                long score = enqueueConditionalTile(q, t, up, skippedWall1Y, skippedWall1X, skippedWall2Y, skippedWall2X);
                if (score < minValue) minValue = score;
            }
            if (t.y < processor.size() - 1) {
                // Move down
                Tile down = processor.get(t.y + 1)[t.x];
                long score = enqueueConditionalTile(q, t, down, skippedWall1Y, skippedWall1X, skippedWall2Y, skippedWall2X);
                if (score < minValue) minValue = score;
            }

            if (t.x > 0) {
                // Move left
                Tile left = processor.get(t.y)[t.x - 1];
                long score = enqueueConditionalTile(q, t, left, skippedWall1Y, skippedWall1X, skippedWall2Y, skippedWall2X);
                if (score < minValue) minValue = score;
            }
            if (t.x < processor.get(t.y).length - 1) {
                // Move right
                Tile right = processor.get(t.y)[t.x + 1];
                long score = enqueueConditionalTile(q, t, right, skippedWall1Y, skippedWall1X, skippedWall2Y, skippedWall2X);
                if (score < minValue) minValue = score;
            }
        }
        // Check if both walls were used in the shortest path (if they were valid)
        /*long score = (skippedWall1X == -1 || skippedWall1Y == -1 || processor.get(endY)[endX].path.contains("(%d:%d)".formatted(skippedWall1X, skippedWall1Y)))
                && (skippedWall2X == -1 || skippedWall2Y == -1 || processor.get(endY)[endX].path.contains("(%d:%d)".formatted(skippedWall2X, skippedWall2Y)))
                ? minValue : defaultScore;* /
        Coord wallCoord1 = new Coord(skippedWall1X, skippedWall1Y);
        Coord wallCoord2 = new Coord(skippedWall2X, skippedWall2Y);
        long score = (wallCoord1.isOutOfBounds(map) || processor.get(endY)[endX].pathList.contains(wallCoord1))
                && (wallCoord1.isOutOfBounds(map) || processor.get(endY)[endX].pathList.contains(wallCoord2))
                ? minValue : defaultScore;
        //return new Result(score, processor.get(endY)[endX].path);//*/
        return new Result(minValue, processor.get(endY)[endX].path);
    }

    private long enqueueConditionalTile(Queue<Tile> q, Tile t, Tile neighbor,
                                        int skippedWall1Y, int skippedWall1X, int skippedWall2Y, int skippedWall2X) {
        boolean isWall = map[neighbor.y][neighbor.x] == '#'
                && (neighbor.y != skippedWall1Y || neighbor.x != skippedWall1X)
                && (neighbor.y != skippedWall2Y || neighbor.x != skippedWall2X);
        if(!isWall && neighbor.minValue > t.minValue + 1) {
            neighbor.minValue = t.minValue + 1;
            neighbor.path = t.path + "(%d:%d)".formatted(neighbor.x, neighbor.y);
            neighbor.pathList = t.pathList;
            neighbor.pathList.add(new Coord(neighbor.x, neighbor.y));   // I hope this referencing doesn't cause bugs, we dont care abt intermediate paths
            q.add(neighbor);
            if (neighbor.x == endX && neighbor.y == endY) {
                return neighbor.minValue;
            }
        }
        return Integer.MAX_VALUE;
    }

    void resetProcessor() {
        for (Tile[] tiles : processor) {
            for (Tile tile : tiles) {
                tile.minValue = Integer.MAX_VALUE;
            }
        }
        processor.get(startY)[startX].minValue = 0;
    }

    void print(int skippedWall1Y, int skippedWall1X, int skippedWall2Y, int skippedWall2X) {
        int y = -1;
        for (char[] line : map) {
            y++;
            int x = -1;
            for (char t : line) {
                x++;
                if ((y == skippedWall1Y && x == skippedWall1X) || (y == skippedWall2Y && x == skippedWall2X))
                    System.out.print('.');
                else System.out.print(t);
            }
            System.out.println();
        }
        System.out.println();
    }

    int minScore;

    private static class Result {
        final long score;
        final String path;

        public Result(long score, String path) {
            this.score = score;
            this.path = path;
        }
    }

    private static class CoordSet {
        private final Coord coord1;
        private final Coord coord2;
        public CoordSet(Coord coord1, Coord coord2) {
            if (coord1.getX() < coord2.getX()) {
                this.coord1 = coord1;
                this.coord2 = coord2;
            } else if (coord1.getX() > coord2.getX()) {
                this.coord1 = coord2;
                this.coord2 = coord1;
            } else if (coord1.getY() < coord2.getY()) {
                this.coord1 = coord1;
                this.coord2 = coord2;
            } else {
                this.coord1 = coord2;
                this.coord2 = coord1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CoordSet coordSet = (CoordSet) o;
            return Objects.equals(coord1, coordSet.coord1) && Objects.equals(coord2, coordSet.coord2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coord1, coord2);
        }
    }

    public void part1() {
        String out = "part1 >> ";
        long result = 0;
        Set<CoordSet> coordSets = new HashSet<>();

        Result defaultResult = findPath(-1, -1, -1, -1, -1);
        long defaultScore = defaultResult.score;
        Set<String> paths = new HashSet<>();
        paths.add(defaultResult.path);
        Map<Long, Long> scores = new HashMap<>();
        int minSavedThreshold = 1;  // example should be 382

        Map<String, Long> savedTimes = new HashMap<>();



        int[] neighbors = {1};
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (isNotWall(x, y)) continue;

                // Once with only that
                resetProcessor();
                Result res = findPath(y, x, -1, -1, defaultScore);
                savedTimes.put(res.path, defaultScore - res.score);
                long saved = defaultScore - res.score;
                if (saved != 0)
                    scores.put(saved, scores.getOrDefault(saved, 0L) + (paths.add(res.path) ? 1 : 0));

                /*
                for (int offy : neighbors) {
                    if (isNotWall(x, y + offy)) continue;
                    resetProcessor();
                    res = findPath(y, x, y + offy, x, defaultScore);
                    savedTimes.put(res.path, defaultScore - res.score);

                    if (res.score >= defaultScore) continue;

                    saved = defaultScore - res.score;
                    scores.put(saved, scores.getOrDefault(saved, 0L) + (paths.add(res.path) ? 1 : 0));
                }
                for (int offx : neighbors) {
                    if (isNotWall(x + offx, y)) continue;
                    resetProcessor();
                    res = findPath(y, x, y, x + offx, defaultScore);
                    savedTimes.put(res.path, defaultScore - res.score);

                    if (res.score >= defaultScore) continue;

                    saved = defaultScore - res.score;
                    scores.put(saved, scores.getOrDefault(saved, 0L) + (paths.add(res.path) ? 1 : 0));
                    //if (res < result) result = res;
                }
                 */
            }
        }

        for (var entry : scores.entrySet()) {
            if (entry.getKey() < minSavedThreshold) continue;
            result += entry.getKey() * entry.getValue();
        }
        long result2 = 0;
        for (var value : savedTimes.values()) {
            if (value >= minSavedThreshold) result2 += value;
        }

        /* Todo debug
        for (var entry : savedTimes.entrySet()) {
            System.out.println(entry.getValue() + " saved: " + entry.getKey());
        }
        print(-1, -1, -1, -1);*/

        out += result;
        out += " || " + result2;
        System.out.println(out);
    }

    boolean isNotWall(int x, int y) {
        if (x < 0 || y < 0 || y >= map.length || x >= map[y].length) return false;
        return map[y][x] != '#';
    }

    public void part2() {
        String out = "part2 >> ";
        int result = 0;

        out += result;
        System.out.println(out);
    }

    private class Tile {
        final int x, y;
        long minValue;

        int wallValue = 3;

        List<Coord> pathList;
        String path = "";

        boolean hasJump = true;
        boolean hasJumpTemp = true;

        public Tile(int x, int y, long minValue, boolean hasJump) {
            this(x, y, minValue);
            this.hasJump = hasJump;
        }
        public Tile(int x, int y, long minValue) {
            this.x = x;
            this.y = y;
            this.minValue = minValue;
            pathList = new ArrayList<>();
            pathList.add(new Coord(x, y));
        }

        public boolean isOutOfBounds() {
            return x < 0 || y < 0 || y >= map.length || x >= map[y].length;
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
            return hasJump ? "2" : hasJumpTemp ? "1" : ".";
        }
    }
}
