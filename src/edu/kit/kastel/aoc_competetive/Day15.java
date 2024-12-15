package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.List;

public class Day15 extends FileReader {

    private static final int DAY = 15;
    private String[] lines;

    List<char[]> map;
    List<char[]> moves;
    Coord robotPosition;
    int robotX, robotY;

    private static final char ROBOT = '@';
    private static final char BOX = 'O';
    private static final char SPACE = '.';
    private static final char BORDER = '#';

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day15(int exampleNr) {
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
            fill.add(line.toCharArray());
            if (fill == map) {
                int x = -1;
                for (char c : map.getLast()) {
                    x++;
                    if (c == ROBOT) {
                        robotPosition = new Coord(x, y);
                        robotX = x;
                        robotY = y;
                    }
                }
            }
            y++;
        }
    }

    void moveRobot(char direction) {
        int deltaX, deltaY;
        switch (direction) {
            case '^':
                deltaX = 0;
                deltaY = -1;
                break;
            case '>':
                deltaX = 1;
                deltaY = 0;
                break;
            case 'v':
                deltaX = 0;
                deltaY = 1;
                break;
            case '<':
                deltaX = -1;
                deltaY = 0;
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
        } else if (nextMapChar == BOX) {
            // Find box to swap it with
            int currentX = robotX + 2 * deltaX;
            int currentY = robotY + 2 * deltaY;
            char newNextMapChar = map.get(currentY)[currentX];
            if (newNextMapChar == BORDER) return;
            while (newNextMapChar != SPACE) {
                currentX += deltaX;
                currentY += deltaY;
                newNextMapChar = map.get(currentY)[currentX];
                if (newNextMapChar == BORDER) return;
            }

            map.get(robotY + deltaY)[robotX + deltaX] = ROBOT;
            map.get(robotY)[robotX] = SPACE;
            map.get(currentY)[currentX] = BOX;  // MOVE BOX
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
        String out = "part1 >> ";
        int result = 0;

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

    public void part2() {
        String out = "part2 >> ---";
        out += getCoordinateSum();
        System.out.println(out);
    }
}
