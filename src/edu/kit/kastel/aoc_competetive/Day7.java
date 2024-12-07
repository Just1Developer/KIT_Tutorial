package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 extends FileReader {

    private static final int DAY = 7;
    private final String[] lines;

    List<Calculation> calculations;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day7(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        calculations = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\d+): ([\\d ]+)");//("(\\d+): (?:(\\d)+ ?)+");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (!matcher.find()) continue;
            List<Integer> nums = new ArrayList<>();
            long result = Long.parseLong(matcher.group(1));
            String[] numbers = matcher.group(2).trim().split(" ");
            for (String number : numbers) {
                nums.add(Integer.parseInt(number));
            }
            if (nums.size() >= 25) System.out.println(nums.size());
            calculations.add(new Calculation(result, nums));
        }
    }

    public void part1() {
        String out = "part1 >> ";
        long sum = 0;

        for (Calculation calculation : calculations) {
            if (calculation.works()) sum += calculation.result;
        }

        out += sum;
        System.out.println(out);
    }

    public void part2() {
        String out = "part2 >> ";
        long sum = 0;

        for (Calculation calculation : calculations) {
            if (calculation.works3ops()) sum += calculation.result;
            //else System.out.println(calculation.result + ": " + Arrays.toString(calculation.values.toArray()));
        }

        out += sum;
        System.out.println(out);
    }

    static class Calculation {
        long result;
        List<Integer> values;

        public Calculation(long result, List<Integer> values) {
            this.result = result;
            this.values = new ArrayList<>(values);
        }

        public boolean works() {
            for (int operands = 0; operands < (1 << (values.size() - 1)); operands++) {        // 0 = +, 1 = *
                long currentSum = values.get(0);
                int currentOps = operands;
                for (int i = 1; i < values.size(); i++) {
                    if ((currentOps & 1) == 0) currentSum += values.get(i);
                    else currentSum *= values.get(i);
                    currentOps >>= 1;
                }
                if (currentSum == result) return true;
            }
            return false;
        }

        public boolean works3ops() {
            for (Base3Counter32bit operands = new Base3Counter32bit(); operands.smallerThan(Math.pow(3, values.size())); operands.increment()/*, System.out.printf("%d < %d: %b%n", operands.getTotalValue(), (int) Math.pow(values.size(), 3), operands.smallerThan(Math.pow(values.size(), 3)))*/) {        // 0 = +, 1 = *
                Base3Counter32bit currentOps = new Base3Counter32bit(operands);
                long currentSum = values.get(0);

                for (int i = 1, j = 0; i < values.size(); i++, j++) {
                    if ((currentOps.getAt(j)) == 0)
                        currentSum += values.get(i);
                    else if ((currentOps.getAt(j)) == 1)
                        currentSum *= values.get(i);
                    if ((currentOps.getAt(j)) == 2)
                        currentSum = Long.parseLong(currentSum + "" + values.get(i));   // Not the best, but easiest to write
                }
                if (currentSum == result) return true;
            }
            return false;
        }

        public boolean works3opsTooComplicated() {
            for (Base3Counter32bit operands = new Base3Counter32bit(); operands.smallerThan(Math.pow(3, values.size())); operands.increment()/*, System.out.printf("%d < %d: %b%n", operands.getTotalValue(), (int) Math.pow(values.size(), 3), operands.smallerThan(Math.pow(values.size(), 3)))*/) {        // 0 = +, 1 = *
                Base3Counter32bit currentOps = new Base3Counter32bit(operands);

                // First, generate new list of numbers:
                List<Long> newValues = new ArrayList<>();
                StringBuilder number = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    number.append(values.get(i));
                    if (i == values.size() - 1 || currentOps.getAt(i) != 2) {
                        // Flush
                        newValues.add(Long.parseLong(number.toString()));
                        number.setLength(0);
                    }
                }
                long currentSum = newValues.get(0);

                // Then, same as before
                for (int i = 1, j = 0; i < newValues.size(); i++, j++) {
                    while (currentOps.getAt(j) == 2) j++;
                    if ((currentOps.getAt(j)) == 0)
                        currentSum += newValues.get(i);
                    else
                        currentSum *= newValues.get(i);
                }
                if (currentSum == result) return true;
            }
            return false;
        }
    }

    static class Base3Counter32bit {
        static final int BITS = 32;
        static final int BASE = 3;

        byte[] digits;
        public Base3Counter32bit() {
            digits = new byte[BITS];
        }
        public Base3Counter32bit(byte[] digits) {
            this.digits = new byte[BITS];
            System.arraycopy(digits, 0, this.digits, 0, digits.length);
        }
        public Base3Counter32bit(Base3Counter32bit base3Counter32bit) {
            this(base3Counter32bit.digits);
        }
        public void increment() {
            int index = 0;
            while (digits[index] == BASE - 1) {
                digits[index] = 0;
                index++;
            }
            digits[index]++;
        }
        public void rightShift() {
            byte[] newDigis = new byte[BITS];
            System.arraycopy(digits, 1, newDigis, 0, newDigis.length - 1);
            digits = newDigis;
        }
        public byte getAt(int index) {
            return digits[index];
        }
        public long getTotalValue() {
            long totalValue = 0;
            for (int i = 0, mul = 1; i < digits.length; i++) {
                totalValue += ((long) digits[i]) * mul;
                mul *= BASE;
            }
            return totalValue;
        }
        public String stringValue() {
            StringBuilder sb = new StringBuilder();
            for (int i = 31; i >= 0; i--) {
                sb.append(digits[i]);
            }
            return sb.toString();
        }
        public boolean smallerThan(double target) {
            return getTotalValue() < target;
        }
    }

}
