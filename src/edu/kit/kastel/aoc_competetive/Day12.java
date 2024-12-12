package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class Day12 extends FileReader {

    private static final int DAY = 12;
    private static String[] lines;

    List<Region> regions;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day12(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        regions = new ArrayList<>();

        Set<Coord> covered = new HashSet<>();
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                Coord c = new Coord(x, y);
                if (!covered.add(c)) continue;
                // Find entire region
                final char VALUE = lines[y].charAt(x);
                Set<Coord> region = new HashSet<>();
                Queue<Coord> queue = new LinkedList<>();
                queue.add(c);
                while (!queue.isEmpty()) {
                    Coord current = queue.poll();
                    if (!current.isInBounds(lines)) continue;
                    if (lines[current.getY()].charAt(current.getX()) != VALUE) continue;
                    if (!region.add(current)) continue;

                    covered.add(current);
                    queue.add(current.add(1, 0));
                    queue.add(current.add(-1, 0));
                    queue.add(current.add(0, 1));
                    queue.add(current.add(0, -1));
                }
                regions.add(new Region(region));
            }
        }
    }

    public void part1() {
        String out = "part1 >> ";
        int result = 0;

        for (Region region : regions) {
            result += region.getPricePt1();
        }

        out += result;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        int result = 0;

        for (Region region : regions) {
            result += region.getPricePt2();
        }

        out += result;
        System.out.println(out);
    }

    static class Region {
        Set<Coord> coords = new HashSet<>();
        public Region(Set<Coord> coords) {
            this.coords.addAll(coords);
        }
        int getArea() {
            return coords.size();
        }
        int getPerimeter() {
            int perimeter = 0;
            for (Coord c : coords) {
                if (!coords.contains(c.add(1, 0))) perimeter++;
                if (!coords.contains(c.add(-1, 0))) perimeter++;
                if (!coords.contains(c.add(0, 1))) perimeter++;
                if (!coords.contains(c.add(0, -1))) perimeter++;
            }
            return perimeter;
        }
        Set<LineSegment> getAllPerimeters() {
            Set<LineSegment> perimeter = new HashSet<>();
            for (Coord c : coords) {
                if (!coords.contains(c.add(1, 0))) perimeter.add(new LineSegment(c.add(1, 0), c.add(1, 1)));
                if (!coords.contains(c.add(-1, 0))) perimeter.add(new LineSegment(c.add(0, 0), c.add(0, 1)));
                if (!coords.contains(c.add(0, 1))) perimeter.add(new LineSegment(c.add(0, 1), c.add(1, 1)));
                if (!coords.contains(c.add(0, -1))) perimeter.add(new LineSegment(c.add(0, 0), c.add(1, 0)));
            }
            return perimeter;
        }
        int getSides() {
            var perimeters = getAllPerimeters();

            boolean changed;
            do {
                changed = false;
                Set<LineSegment> newPerimeter = new HashSet<>();
                for (var perimeter : perimeters) {
                    Optional<LineSegment> swappedWith = Optional.empty();
                    Optional<LineSegment> mergeResult = Optional.empty();
                    for (var per : newPerimeter) {
                        mergeResult = per.merge(perimeter);
                        if (mergeResult.isEmpty()) continue;
                        swappedWith = Optional.of(per);
                        break;
                    }
                    if (swappedWith.isPresent()) {
                        // Deny merge if diagonal
                        Coord mergeCoord;
                        if ((mergeResult.get().direction == 0 && mergeResult.get().begin.getX() == swappedWith.get().begin.getX())
                        || (mergeResult.get().direction == 1 && mergeResult.get().begin.getY() == swappedWith.get().begin.getY())) {
                            mergeCoord = swappedWith.get().end;
                        } else {
                            mergeCoord = swappedWith.get().begin;
                        }

                        // if mergeCoord (bottom right) is a diagonal
                        // If the alignment is:
                        //   A | B
                        //   -- --
                        //   B | A
                        // We do not want to merge the diagonals or horizontals
                        Coord topLeftCoord = mergeCoord.add(-1, -1);
                        Coord topRightCoord = mergeCoord.add(0, -1);
                        Coord bottomLeftCoord = mergeCoord.add(-1, 0);
                        Coord bottomRightCoord = mergeCoord;
                        boolean topLeft = coords.contains(topLeftCoord) && topLeftCoord.isInBounds(lines);
                        boolean topRight = coords.contains(topRightCoord) && topRightCoord.isInBounds(lines);
                        boolean bottomLeft = coords.contains(bottomLeftCoord) && bottomLeftCoord.isInBounds(lines);
                        boolean bottomRight = coords.contains(bottomRightCoord) && bottomRightCoord.isInBounds(lines);

                        // Either one of the coords is out of bounds, or it's not a diagonal. Then we merge.
                        if (!topLeftCoord.isInBounds(lines) || !topRightCoord.isInBounds(lines) || !bottomLeftCoord.isInBounds(lines) || !bottomRightCoord.isInBounds(lines)
                            || !(topLeft == bottomRight && topRight == bottomLeft && topLeft != topRight)) {
                            newPerimeter.remove(swappedWith.get()); // Remove old line segment, which is now being merged
                            newPerimeter.add(mergeResult.get());    // Add the merged one
                            changed = true;
                            continue;
                        } else {
                            System.out.println("Skipped merge:");
                            //System.out.printf(" %c | %c%n -- --%n %c | %c%n-------%n", topLeft ? 'T' : 'F', topRight ? 'T' : 'F', bottomLeft ? 'T' : 'F', bottomRight ? 'T' : 'F');
                            System.out.printf(mergeResult.get().direction == 0 ? " %c %c%n ---%n %c %c%n" : " %c | %c%n %c | %c%n", topLeftCoord.isInBounds(lines) ? lines[topLeftCoord.getY()].charAt(topLeftCoord.getX()) : '#', topRightCoord.isInBounds(lines) ? lines[topRightCoord.getY()].charAt(topRightCoord.getX()) : '#', bottomLeftCoord.isInBounds(lines) ? lines[bottomLeftCoord.getY()].charAt(bottomLeftCoord.getX()) : '#', bottomRightCoord.isInBounds(lines) ? lines[bottomRightCoord.getY()].charAt(bottomRightCoord.getX()) : '#');
                        }
                        // Diagonal. Don't Merge
                    }
                    // Cannot be merged into anything
                    newPerimeter.add(perimeter);
                }
                perimeters = newPerimeter;
            } while (changed);

            return perimeters.size();
        }

        static class LineSegment {
            Coord begin, end; // Top Left Corners of the areas
            int direction;  // 0 = left/right, 1 = up/down

            public LineSegment(Coord begin, Coord end) {
                if (begin.getX() != end.getX() && begin.getY() != end.getY()) throw new IllegalArgumentException("Coordinates must build a non-diagonal line to be a line segment");
                if (begin.getX() != end.getX()) {
                    direction = 0;
                    if (begin.getX() < end.getX()) {
                        this.begin = begin;
                        this.end = end;
                    } else {
                        this.begin = end;
                        this.end = begin;
                    }
                } else {
                    direction = 1;
                    if (begin.getY() < end.getY()) {
                        this.begin = begin;
                        this.end = end;
                    } else {
                        this.begin = end;
                        this.end = begin;
                    }
                }
            }
            public LineSegment(Coord begin, Coord end, int direction) {
                this.begin = begin;
                this.end = end;
                this.direction = direction;
            }

            boolean add(Coord coord) {
                if (direction == 0) {
                    if (coord.add(1, 0).equals(begin)) {
                        begin = coord;
                        return true;
                    }
                    if (coord.add(-1, 0).equals(end)) {
                        end = coord;
                        return true;
                    }
                } else if (direction == 1) {
                    if (coord.add(0, -1).equals(begin)) {
                        begin = coord;
                        return true;
                    }
                    if (coord.add(0, 1).equals(end)) {
                        end = coord;
                        return true;
                    }
                }
                return false;
            }

            Optional<LineSegment> merge(LineSegment other) {
                if (other == null) return Optional.empty();
                if (other == this) return Optional.empty();
                if (other.direction != direction) return Optional.empty();
                if (direction == 0) {
                    if (begin.getY() != other.begin.getY()) return Optional.empty();
                    if (begin.getX() == other.end.getX()) {
                        return Optional.of(new LineSegment(other.begin, end, direction));
                    }
                    if (end.getX() == other.begin.getX()) {
                        return Optional.of(new LineSegment(begin, other.end, direction));
                    }
                } else if (direction == 1) {
                    if (begin.getX() != other.begin.getX()) return Optional.empty();
                    if (begin.getY() == other.end.getY()) {
                        return Optional.of(new LineSegment(other.begin, end, direction));
                    }
                    if (end.getY() == other.begin.getY()) {
                        return Optional.of(new LineSegment(begin, other.end, direction));
                    }
                }
                return Optional.empty();
            }

            @Override
            public String toString() {
                return "(begin: %s, end: %s)".formatted(begin, end);
            }

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                LineSegment segment = (LineSegment) o;
                return direction == segment.direction && Objects.equals(begin, segment.begin) && Objects.equals(end, segment.end);
            }

            @Override
            public int hashCode() {
                return Objects.hash(begin, end, direction);
            }
        }

        int getPricePt1() {
            return getArea() * getPerimeter();
        }
        int getPricePt2() {
            System.out.printf("%d Sides * %d Area%n", getSides(), getArea());
            return getSides() * getArea();
        }
    }
}
