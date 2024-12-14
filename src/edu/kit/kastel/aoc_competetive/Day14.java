package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;
import edu.kit.kastel.aoc_competetive.helper.Coord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 extends FileReader {

    private static final int DAY = 14;
    private final String[] lines;

    private final int width, height;
    List<Robot> robots;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day14(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        if (exampleNr == 0) {
            width = 101;
            height = 103;
        } else {
            width = 11;
            height = 7;
        }
        robots = new ArrayList<>();

        Pattern p1 = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");
        for (String line : lines) {
            Matcher m1 = p1.matcher(line);

            if (!m1.matches()) {
                System.out.printf("Skipped following line:%n%s%n", line);
                continue;
            }
            robots.add(new Robot(Integer.parseInt(m1.group(1)), Integer.parseInt(m1.group(2)), Integer.parseInt(m1.group(3)), Integer.parseInt(m1.group(4))));
        }
    }

    List<Robot> moveAllRobots(List<Robot> initialRobots, int seconds) {
        List<Robot> l = new ArrayList<>();
        for (Robot robot : initialRobots) {
            l.add(moveRobot(robot, seconds));
        }
        return l;
    }

    Robot moveRobot(Robot initialRobot, int seconds) {
        int newX = (initialRobot.position.getX() + initialRobot.velocity.getX() * seconds);
        int newY = (initialRobot.position.getY() + initialRobot.velocity.getY() * seconds);
        return new Robot(newX, newY, initialRobot.velocity);
    }

    int getSafetyFactor(List<Robot> robots) {
        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        int qWidth = width / 2, qHeight = height / 2;
        int beginFirstX = 0, beginSecondX = 1 + qWidth;
        int beginFirstY = 0, beginSecondY = 1 + qHeight;
        for (Robot robot : robots) {
            if (robot.position.getX() < qWidth && robot.position.getY() < qHeight) {
                q1++;
                continue;
            }
            if (robot.position.getX() >= beginSecondX && robot.position.getY() < qHeight) {
                q2++;
                continue;
            }
            if (robot.position.getX() < qWidth && robot.position.getY() >= beginSecondY) {
                q3++;
                continue;
            }
            if (robot.position.getX() >= beginSecondX && robot.position.getY() >= beginSecondY) {
                q4++;
            }
        }
        //System.out.printf("%d * %d * %d * %d%n", q1, q2, q3, q4);
        return q1 * q2 * q3 * q4;
    }

    boolean isClustered(List<Robot> robots) {
        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        int qWidth = width / 2, qHeight = height / 2;
        int beginSecondX = 1 + qWidth;
        int beginSecondY = 1 + qHeight;
        for (Robot robot : robots) {
            if (robot.position.getX() < qWidth && robot.position.getY() < qHeight) {
                q1++;
                continue;
            }
            if (robot.position.getX() >= beginSecondX && robot.position.getY() < qHeight) {
                q2++;
                continue;
            }
            if (robot.position.getX() < qWidth && robot.position.getY() >= beginSecondY) {
                q3++;
                continue;
            }
            if (robot.position.getX() >= beginSecondX && robot.position.getY() >= beginSecondY) {
                q4++;
            }
        }
        //System.out.printf("%d * %d * %d * %d%n", q1, q2, q3, q4);
        return Math.max(Math.max(q1, q2), Math.max(q3, q4)) >= 0.7 * robots.size();
    }

    void printRobots(List<Robot> robots) {
        char[][] map = new char[height][width];
        for (char[] c : map) {
            Arrays.fill(c, '.');
        }
        for (Robot robot : robots) {
            int x = robot.position.getX(), y = robot.position.getY();
            map[y][x] = map[y][x] == '.' ? '1' : (char) (map[y][x] + 1);
        }
        for (char[] ar : map) {
            for (char c : ar) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void part1() {
        String out = "part1 >> ";
        int result;

        printRobots(robots);
        printRobots(moveAllRobots(robots, 1));
        printRobots(moveAllRobots(robots, 100));
        result = getSafetyFactor(moveAllRobots(robots, 100));

        out += result;
        System.out.println(out);
    }

    public void part2() {
        List<Robot> robots = this.robots;

        int seconds = 0;
        while (true) {
            seconds++;
            robots = moveAllRobots(robots, 1);
            if (!isClustered(robots)) continue;
            printRobots(robots);

            // Add breakpoint, manually check if we have a christmas tree

            System.out.printf("steps: %d%n", seconds);
        }
    }

    private class Robot {
        final Coord position;
        final Coord velocity;

        public Robot(Coord position, Coord velocity) {
            this.position = position.mod(width, height);
            this.velocity = velocity;
        }
        public Robot(int x, int y, int deltaX, int deltaY) {
            this.position = new Coord(x, y).mod(width, height);
            this.velocity = new Coord(deltaX, deltaY);
        }
        public Robot(int x, int y, Coord velocity) {
            this.position = new Coord(x, y).mod(width, height);
            this.velocity = velocity;
        }
        public Robot add(Coord coord) {
            return new Robot(position.add(coord), velocity);
        }
    }
}
