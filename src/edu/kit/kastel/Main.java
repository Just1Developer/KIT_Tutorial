package edu.kit.kastel;

import edu.kit.kastel.aoc_competetive.Day13;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Main entry point of the program.
 * @author uwwfh
 */
public class Main {

    /**
     * Advent of Code
     */
    public static void main(String[] args) {
        var day = new Day13(0);
        day.part1();
        day.part2();

        B b = new B();
    }

    static class A {
        private A() {
            System.out.println("A");
        }
    }
    static class B extends A {
        public B() {
            System.out.println("B");
        }
    }

    public static <T> void myVoid(T myVariable) {

    }

    public static void printList(List<?> list) {

    }

    /**
     * Other Entry point.
     */
    public static void main2(String[] args) {

        //*
        int[] a = {1, 3, 5, 7, 9, 2, 4, 6};
        O1_Mergesort.mergesort_logN(a);
        System.out.println(">> " + Arrays.toString(a));
        if (true) return;   // prevent "unreachable code" error
        //*/

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