package edu.kit.kastel;

import java.io.File;
import java.util.Arrays;

/**
 * Main entry point of the program.
 * @author uwwfh
 */
public class Main {
    public static void main(String[] args) {
        int LIM = 20000;
        for (int i = 0; i < LIM; i++) {
            int[] c = new int[i];
            for (int j = 0; j < i; j++) {
                c[j] = LIM - j;
            }
            int[] c1 = c.clone();
            O1_Mergesort.mergesort_O1(c1);
            int[] c2 = c.clone();
            O1_Mergesort.mergesort_O1(c2);

            if (i % 1000 == 0) System.out.println(i);

            if (!Arrays.equals(c1, c2)) {
                System.out.println("Not equal for i = " + i + ": m1: " + System.lineSeparator() + Arrays.toString(c1) + " vs m2: " + System.lineSeparator() + Arrays.toString(c2));
            }
        }

        System.out.println("Hello world!");
        for (String line : FileReader.readFile("AdventOfCodeInput/day1.txt")) {
            System.out.println(line);
        }
        System.out.println(new File("AdventOfCodeInput/day1.txt").getAbsolutePath());
    }
}