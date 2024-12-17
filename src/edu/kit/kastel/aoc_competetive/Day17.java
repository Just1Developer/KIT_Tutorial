package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.List;

public class Day17 extends FileReader {

    private static final long DAY = 17;
    private String[] lines;

    String programStr;
    long regA, regB, regC;
    int opPtr = 0;
    int[] opCodes;
    int[] operands;
    int[] program;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day17(long exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        regA = Integer.parseInt(lines[0].substring("Register A: ".length()));
        regB = Integer.parseInt(lines[1].substring("Register B: ".length()));
        regC = Integer.parseInt(lines[2].substring("Register C: ".length()));

        programStr = lines[4].substring("Program: ".length());
        opCodes = new int[(programStr.length() >> 2) + 1];
        operands = new int[(programStr.length() >> 2) + 1];

        for (int i = 0, c = 0; i < programStr.length(); i += 4) {
            opCodes[c] = programStr.charAt(i) - '0';
            operands[c++] = programStr.charAt(i + 2) - '0';
        }

        program = new int[(programStr.length() >> 1) + 1];
        for (int i = 0, c = 0; i < programStr.length(); i += 2) {
            program[c++] = programStr.charAt(i) - '0';
        }
    }

    String runProgram() {
        StringBuilder programBuilder = new StringBuilder();

        //while (opPtr < program.length - 1) {
            //long opCode = program[opPtr++];
            //long literalOperand = program[opPtr++];

        while (opPtr < opCodes.length) {
            int opCode = opCodes[opPtr];
            int literalOperand = operands[opPtr++];

            long comboOperand = switch (literalOperand) {
                case 4 -> regA;
                case 5 -> regB;
                case 6 -> regC;
                default -> literalOperand;
            };

            // Perform operation
            switch (opCode) {
                case 0:
                    // Division: regA / 1 << operand
                    regA = regA / (1L << comboOperand);
                    break;
                case 1:
                    // bxl: Bitwise XOR
                    regB = regB ^ literalOperand;
                    break;
                case 2:
                    // bst
                    regB = comboOperand & 0x7;
                    break;
                case 3:
                    // jnz
                    if (regA != 0) {
                        opPtr = literalOperand << 1;    // Multiply by 2 to get index of x op code address
                    }
                    break;
                case 4:
                    // bxc
                    regB ^= regC;
                    break;
                case 5:
                    // out
                    programBuilder.append(",%d".formatted(comboOperand & 0x7));
                    break;
                case 6:
                    // bdv
                    regB = regA / (1L << comboOperand);
                    break;
                case 7:
                    // cdv
                    regC = regA / (1L << comboOperand);
                    break;
            }
        }

        return programBuilder.isEmpty() ? "" : programBuilder.deleteCharAt(0).toString();
    }

    public void part1() {
        String out = "part1 >> ";
        long result = 0;

        System.out.println("regA: " + regA);
        System.out.println("regB: " + regB);
        System.out.println("regC: " + regC);
        out += runProgram();
        System.out.println("regA: " + regA);
        System.out.println("regB: " + regB);
        System.out.println("regC: " + regC);

        //out += result;
        System.out.println(out);
    }

    String run(long regA) {
        this.regA = regA;
        this.regB = 0;
        this.regC = 0;
        this.opPtr = 0;
        return runProgram();
    }

    // IT WORKS! WE ARE SO BACK!!

    /**
     *
     * 2,4  B = A & 0b111
     * 1,1  B ^= 0b001
     * 7,5  C = A >> B                     C = A / 2^B
     * 0,3  A >> 3                         A = A / 2 << 3
     * 1,4  B ^= 0b100
     * 4,5  B ^= C
     * 5,5  OUT B & 0b111
     * 3,0  REPEAT TILL A == 0
     * <br/>
     * B = last 3 bits of a
     * Flip last bit of B
     * C = A >> B
     * Flip 3rd bit of B
     * B XOR C
     * <br/>
     * Meaning: Zahl i hängt immer von 3 Bits aus A ab
     * */
    List<Long> getThreeBitsFor(int target) {
        List<Long> list = new ArrayList<>();
        list.add(0L);
        return getThreeBitsFor(target, list);
    }
    List<Long> getThreeBitsFor(int target, List<Long> possibleAs) {
        // This we can just bruteforce (used once)
        long b, c;
        List<Long> newPossibleAs = new ArrayList<>();
        for (long prevA : possibleAs) {
            for (int a = 0; a < 8; a++) {
                long A = (prevA << 3) | a;   // 111 => 100
                b = a;
                b ^= 1;
                c = A >> b;
                b ^= 4;
                b ^= c;
                if ((b & 0b111) == target) newPossibleAs.add(A);
            }
        }
        return newPossibleAs;
    }

    // We need 45 Bit
    long find() {
        // We need to find it 15 times
        List<Long> possibilities = new ArrayList<>();
        possibilities.add(0L);
        for (int i = program.length - 1; i >= 0; i--) {
            possibilities = getThreeBitsFor(program[i], possibilities);
        }
        System.out.println(possibilities);
        return possibilities.isEmpty() ? -1 : possibilities.getFirst();
    }

    public void part2() {
        String out = "part2 >> ";

        long result = find();

        out += programStr + " >> " + result;
        System.out.println(out);
    }
}
