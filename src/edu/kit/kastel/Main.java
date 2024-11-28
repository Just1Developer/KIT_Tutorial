package edu.kit.kastel;

import java.io.File;
import java.util.Arrays;

/**
 * Main entry point of the program.
 * @author uwwfh
 */
public class Main {
    public static void main(String[] args) {

        /*
        int[] a = {3, 2, 1};
        O1_Mergesort.mergesort_noRecursion(a);
        System.out.println(">> " + Arrays.toString(a));
        if (true) return;   // prevent "unreachable code" error
        */

        int LIM = 15;
        for (int i = 0; i < LIM; i++) {
            //if (i != 5 && i != 6 && i != 9 && i != 11) continue;

            int[] c = new int[i];
            for (int j = 0; j < i; j++) {
                c[j] = LIM - j;
            }
            int[] c1 = c.clone();
            O1_Mergesort.mergesort_O1_noRecursion(c1);
            int[] c2 = c.clone();
            Arrays.sort(c2);

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