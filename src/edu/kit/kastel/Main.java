package edu.kit.kastel;

import java.io.File;

/**
 * Main entry point of the program.
 * @author uwwfh
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        for (String line : FileReader.readFile("AdventOfCodeInput/day1.txt")) {
            System.out.println(line);
        }
        System.out.println(new File("AdventOfCodeInput/day1.txt").getAbsolutePath());
    }
}