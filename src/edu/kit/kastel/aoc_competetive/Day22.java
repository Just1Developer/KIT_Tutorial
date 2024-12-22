package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day22 extends FileReader {

    private static final int DAY = 22;
    private String[] lines;

    List<Integer> numbers;

    /**
     * Initializes code for day {@code DAY}.
     * @param exampleNr The example number for the filepath.
     */
    public Day22(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        numbers = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) continue;
            numbers.add(Integer.parseInt(line));
        }

        /*
        int secret = 123, decimalOld = secret % 10, decimal, change;
        for (int i = 0; i < 10; i++) {
            secret = nextSecretNumber(secret);
            decimal = secret % 10;
            change = decimal - decimalOld;
            decimalOld = decimal;

            System.out.println("Change: " + change);
        }*/
    }

    int nextSecretNumber(int secret) {
        // Prune = Take last 6 bit only
        // XOR with *64 doesn't do anything, since we only take the 6 lowest bit immediately after
        int MOD_MASK = 0xFFFFFF;
        secret ^= secret << 6;
        secret &= MOD_MASK;
        secret ^= secret >> 5;
        // Skip prune
        secret ^= secret << 11;
        secret &= MOD_MASK;
        return secret;
    }

    public void part1() {
        String out = "part1 >> ";
        long result = 0;

        final int NEXT_NUMBERS = 2000;

        long start = System.currentTimeMillis();

        for (int secret : numbers) {
            for (int i = 0; i < NEXT_NUMBERS; i++) {
                secret = nextSecretNumber(secret);
            }
            result += secret;
        }

        long end = System.currentTimeMillis();

        out += result;
        out += ", Time: " + (end - start) + "ms";
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        long result = 0;

        long start = System.currentTimeMillis();

        Set<NumSeq> sequences = new HashSet<>();

        // We could do this smart, but It would probably only take like a minute
        final int NEXT_NUMBERS = 2000;
        NumSeq seq = new NumSeq(0, 0, 0, 0);

        for (int secret : numbers) {
            int decimalOld = secret % 10, decimal, change = 0;
            seq.clear();
            for (int i = 0; i < NEXT_NUMBERS; i++) {
                secret = nextSecretNumber(secret);
                decimal = secret % 10;
                change = decimal - decimalOld;
                decimalOld = decimal;
                seq.enqueue(change);

                if (i < 3) continue;

                sequences.add(new NumSeq(seq));
            }
        }
        System.out.printf("Found a total of %d sequences%n", sequences.size());

        // For me, this number is 40951. Easily bruteforce-able

        NumSeq highestProfit = null;
        var example = new NumSeq(-2, 1, -1, 3);

        int j = 0;
        for (NumSeq target : sequences) {
            if (++j % 1000 == 0) System.out.println(j);
            int profit = 0;

            for (int secret : numbers) {
                seq.clear();
                int decimalOld = secret % 10, decimal, change = 0;
                //boolean sell = false;
                for (int i = 0; i < NEXT_NUMBERS; i++) {
                    //if (target.equals(example) && sell) {
                    //    System.out.println("selling");
                    //}

                    secret = nextSecretNumber(secret);
                    decimal = secret % 10;
                    change = decimal - decimalOld;

                    /*
                    if (sell) {
                        profit += change;   // Profit
                        if (target.equals(example)) {
                            System.out.printf("%s: Adding %d to profit%n", example, change);
                        }
                        break;
                    }*/

                    decimalOld = decimal;
                    seq.enqueue(change);

                    if (i < 3) continue;

                    if (seq.equals(target)) {
                        // SELL ON NEXT RUN!
                        profit += decimal;   // Profit
                        //if (target.equals(example)) {
                        //    System.out.printf("%s: Adding %d to profit%n", example, change);
                        //}
                        break;
                        //sell = true;
                    }
                }
            }

            //if (target.equals(example)) {
            //    System.out.printf("%s: %d profit%n", example, profit);
            //}

            if (profit > result) {
                System.out.printf("New Record %s - Profit: %d%n", target, profit);
                highestProfit = target;
                result = profit;
            }
        }

        long end = System.currentTimeMillis();

        out += highestProfit + ", result: ";

        out += result;
        out += ", Time: " + (end - start) + "ms";
        System.out.println(out);
    }

    static class NumSeq {
        int val1, val2, val3, val4;

        public NumSeq(int val1, int val2, int val3, int val4) {
            this.val1 = val1;
            this.val2 = val2;
            this.val3 = val3;
            this.val4 = val4;
        }

        public NumSeq(NumSeq prev, int newVal) {
            this.val1 = prev.val2;
            this.val2 = prev.val3;
            this.val3 = prev.val4;
            this.val4 = newVal;
        }

        public NumSeq(NumSeq prev) {
            this.val1 = prev.val1;
            this.val2 = prev.val2;
            this.val3 = prev.val3;
            this.val4 = prev.val4;
        }

        public void enqueue(int val) {
            val1 = val2;
            val2 = val3;
            val3 = val4;
            val4 = val;
        }

        public void clear() {
            // not even necessary
            val1 = 0;
            val2 = 0;
            val3 = 0;
            val4 = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            NumSeq numSeq = (NumSeq) o;
            return val1 == numSeq.val1 && val2 == numSeq.val2 && val3 == numSeq.val3 && val4 == numSeq.val4;
        }

        @Override
        public int hashCode() {
            return Objects.hash(val1, val2, val3, val4);
        }

        @Override
        public String toString() {
            return "(NumSeq: %d, %d, %d, %d)".formatted(val1, val2, val3, val4);
        }
    }

    void increase(int[] targetQueue) {
        targetQueue[0]++;
        if (targetQueue[0] < 10) return;
        targetQueue[0] = -9;
        targetQueue[1]++;
        if (targetQueue[1] < 10) return;
        targetQueue[1] = -9;
        targetQueue[2]++;
        if (targetQueue[2] < 10) return;
        targetQueue[2] = -9;
        targetQueue[3]++;
        if (targetQueue[3] < 10) return;
        targetQueue[3] = -9;
    }
}
