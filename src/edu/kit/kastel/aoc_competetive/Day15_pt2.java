package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day15_pt2 extends FileReader {

    private static final int DAY = 15;
    private String[] lines;

    List<char[]> map;
    List<char[]> moves;
    int robotX, robotY;

    private static final char ROBOT = '@';
    private static final char BOX_SINGLE = 'O';
    private static final char BOX = '[';
    private static final char BOX_OTHER = ']';
    private static final char SPACE = '.';
    private static final char BORDER = '#';

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day15_pt2(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        map = new ArrayList<>();
        moves = new ArrayList<>();

        List<char[]> fill = map;


        int y = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                // comparing references on purpose
                if (fill == map) {
                    fill = moves;
                }
                continue;
            }
            if (fill == map) {
                char[] lineArray = new char[line.length() * 2];
                int x = -2;
                for (char c : line.toCharArray()) {
                    x += 2;
                    if (c == ROBOT) {
                        robotX = x;
                        robotY = y;
                        lineArray[x] = ROBOT;
                        lineArray[x + 1] = SPACE;
                    } else if (c == BOX_SINGLE) {
                        lineArray[x] = BOX;
                        lineArray[x + 1] = BOX_OTHER;
                    } else {    // Space or Border
                        lineArray[x] = c;
                        lineArray[x + 1] = c;
                    }
                }
                fill.add(lineArray);
            } else {
                fill.add(line.toCharArray());
            }
            y++;
        }
    }

    void moveRobot(char direction) {
        // BoxesOffsetX = Direction in which to look for boxes, OffsetView = Scan the 3 tiles for boxes
        int deltaX = 0, deltaY = 0, boxesOffsetX, boxesOffsetY, boxesOffsetViewX = 0, boxesOffsetViewY = 0;
        switch (direction) {
            case '^':
                deltaY = -1;
                boxesOffsetViewX = -1;
                break;
            case '>':
                deltaX = 1;
                boxesOffsetViewX = -1;
                break;
            case 'v':
                deltaY = 1;
                break;
            case '<':
                deltaX = -1;
                break;
            default:
                return;
        }

        char nextMapChar = map.get(robotY + deltaY)[robotX + deltaX];
        if (nextMapChar == BORDER) return;
        else if (nextMapChar == SPACE) {
            map.get(robotY + deltaY)[robotX + deltaX] = ROBOT;
            map.get(robotY)[robotX] = SPACE;
            robotX += deltaX;
            robotY += deltaY;
        } else {
            // Find box to swap it with
            int currentX = robotX + 3 * deltaX;
            int currentY = robotY + deltaY;

            if (deltaX == 0) {
                // Premise: We Move In Y-Direction
                List<int[]> boxesToMove = new ArrayList<>();
                Queue<int[]> boxesToCheck = new LinkedList<>();
                char newNextMapChar = map.get(currentY)[currentX];

                if (newNextMapChar == BOX_OTHER) currentX--;

                var newCoord = new int[]{currentX, currentY};
                boxesToMove.add(newCoord);
                boxesToCheck.add(newCoord);

                while (!boxesToCheck.isEmpty()) {
                    // New coordinate:
                    int[] coord = boxesToCheck.remove();
                    currentX = coord[0];
                    currentY = coord[1] + deltaY;
                    newNextMapChar = map.get(currentY)[currentX];
                    if (newNextMapChar == BORDER) return;
                    if (newNextMapChar != SPACE) {
                        if (newNextMapChar == BOX_OTHER) currentX--;
                        newCoord = new int[]{currentX, currentY};
                        boxesToMove.add(newCoord);
                        boxesToCheck.add(newCoord);
                    }

                    currentX = coord[0] + 1;
                    newNextMapChar = map.get(currentY)[currentX];
                    if (newNextMapChar == BORDER) return;
                    if (newNextMapChar == BOX) {    // If box_other, we already have it
                        newCoord = new int[]{currentX, currentY};
                        boxesToMove.add(newCoord);
                        boxesToCheck.add(newCoord);
                    }
                }

                for (int i = boxesToMove.size() - 1; i >= 0; i--) {
                    int[] coord = boxesToMove.get(i);
                    currentX = coord[0];
                    currentY = coord[1] + deltaY;
                    map.get(currentY)[currentX] = BOX;
                    map.get(currentY)[currentX + 1] = BOX_OTHER;
                    map.get(coord[1])[currentX] = SPACE;
                    map.get(coord[1])[currentX + 1] = SPACE;
                }
            } else {
                char newNextMapChar = map.get(currentY)[currentX];
                if (newNextMapChar == BORDER) return;
                while (newNextMapChar != SPACE) {
                    currentX += deltaX;
                    if (map.get(currentY)[currentX] == BORDER) return;  // Check intermediate
                    currentX += deltaX;
                    newNextMapChar = map.get(currentY)[currentX];
                    if (newNextMapChar == BORDER) return;
                }

                boolean leftBox = deltaX < 0;
                char[] line = map.get(currentY);
                int steps = Math.abs(currentX - (robotX));
                for (int x = robotX + deltaX, i = 0; i < steps; x += deltaX, i++) {
                    line[x] = leftBox ? BOX : BOX_OTHER;
                    leftBox = !leftBox;
                }
            }

            map.get(robotY + deltaY)[robotX + deltaX] = ROBOT;
            map.get(robotY)[robotX] = SPACE;
            robotX += deltaX;
            robotY += deltaY;
        }
    }

    long getCoordinateSum() {
        long sum = 0;
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).length; x++) {
                if (x == 0 || y == 0 || x == map.get(y).length - 1 || y == map.get(y).length - 1) continue; // Border
                if (map.get(y)[x] != BOX) continue;
                sum += x + 100L * y;
            }
        }
        return sum;
    }

    void print() {
        for (char[] line : map) {
            for (char c : line) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void part1() {
        String out = "part1 >> ---";
        out += getCoordinateSum();
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";

        print();
        for (char[] moves : this.moves) {
            for (char move : moves) {
                moveRobot(move);
                //System.out.println(move + ":");
                //print();
            }
        }
        print();

        out += getCoordinateSum();
        System.out.println(out);
    }
}
