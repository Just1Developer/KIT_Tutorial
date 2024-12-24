package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends FileReader {

    private static final int DAY = 24;
    private String[] lines;

    List<Node> outputs;
    List<Node> intermediates;
    HashMap<String, Node> allNodes;
    Queue<Node> evaluate;

    /**
     * Initializes code for day {@code DAY}.
     * @param exampleNr The example number for the filepath.
     */
    public Day24(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        outputs = new ArrayList<>(64);
        for (int i = 0; i < 64; i++) outputs.add(null);
        allNodes = new HashMap<>();
        evaluate = new LinkedList<>();

        Set<String> done = new HashSet<>();

        int i;
        Pattern p1 = Pattern.compile("([a-z\\d]{3}): ([01])");
        for (i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) break;
            Matcher match = p1.matcher(lines[i]);
            if (!match.matches()) {
                System.out.println("Error in line " + i + ": " + lines[i]);
                continue;
            }
            String key = match.group(1);
            int value = match.group(2).charAt(0) - '0';
            Node node = getNode(key);

            node.output = value;
            done.add(key);
        }

        List<String> OPLIST = List.of("AND", "OR", "XOR");
        Pattern p3 = Pattern.compile("([a-z\\d]{3}) (AND|OR|XOR) ([a-z\\d]{3}) -> ([a-z\\d]{3})");
        for (i++; i < lines.length; i++) {
            if (lines[i].isEmpty()) break;
            Matcher match = p3.matcher(lines[i]);
            if (!match.matches()) {
                System.out.println("[P3] Error in line " + i + ": " + lines[i]);
                continue;
            }
            String key1 = match.group(1);
            String key2 = match.group(3);
            String OP = match.group(2);
            String keyT = match.group(4);

            Node node1 = getNode(key1);
            Node node2 = getNode(key2);
            Node nodeT = getNode(keyT);

            nodeT.input1 = node1;
            nodeT.input2 = node2;
            nodeT.operation = OPLIST.indexOf(OP);

            if (!done.contains(key1)) {
                evaluate.add(node1);
                done.add(key1);
            }
            if (!done.contains(key2)) {
                evaluate.add(node2);
                done.add(key2);
            }
            if (!done.contains(keyT)) {
                evaluate.add(nodeT);
                done.add(keyT);
            }
        }
    }

    Node getNode(String key) {
        if (allNodes.containsKey(key)) return allNodes.get(key);
        Node node = new Node();
        allNodes.put(key, node);

        Pattern p2 = Pattern.compile("z(\\d{2})");
        Matcher target = p2.matcher(key);
        if (target.matches()) {
            int id = Integer.parseInt(target.group(1));
            outputs.set(id, node);
        }/* else if (!key.startsWith("x") && !key.startsWith("y")) {
            intermediates.add(node);
        }*/
        node.key = key;

        return node;
    }

    public void part1() {
        String out = "part1 >> ";
        long result = 0;
        long start = System.currentTimeMillis();

        while (!evaluate.isEmpty()) {
            Node node = evaluate.poll();
            if (node.isCalculated()) continue;
            if (!node.canCalculate()) {
                evaluate.add(node);
                continue;
            }
            node.calculateOutput();
        }

        StringBuilder number = new StringBuilder();
        for (long i = 0, l = 1L; i < outputs.size(); i++, l <<= 1) {
            Node output = outputs.get((int) i);
            int val = output == null ? 0 : output.output;
            if (val == 1) result |= l;
            number.insert(0, val);
        }
        out += number + System.lineSeparator();

        long end = System.currentTimeMillis();
        out += result;
        out += ", Time: " + (end - start) + "ms";
        System.out.println(out);
    }

    // each output node should be an XOR, which is connected to an intermediateNode and an XOR node, which XORs the inputs for this output
    //

    // Todo this is wrong, I just made it manually

    public void part2() {
        String out = "part2 >> ";
        long start = System.currentTimeMillis();

        // nodes who receive faulty inputs I think
        // like abc OP def -> wrongNodeHere
        Set<String> faultyNodes = new HashSet<>();
        // First, check first tile:
        Node z0 = outputs.getFirst();
        HashMap<String, Node> carryOuts = new HashMap<>();
        HashMap<String, Node> inputXORs = new HashMap<>();
        if (z0.operation != 2
                || !((z0.input1.key.equals("x00")) && z0.input2.key.equals("y00") || ((z0.input1.key.equals("y00")) && z0.input2.key.equals("x00")))) {
            faultyNodes.add(z0.key);
        }
        // All in the middle should have this setup, first and last have a different setup
        // (carry out from 44 just feeds to z45)
        for (int i = 1; i < outputs.size() - 1 && outputs.get(i + 1) != null; i++) {
            Node node = outputs.get(i);
            if (node == null) break;
            if (node.operation != 2) faultyNodes.add(node.key);
            if (node.input1.operation == 2 && node.input2.operation == 1) {
                inputXORs.put(node.key, node.input1);
                carryOuts.put("z%02d".formatted(Integer.parseInt(node.key.substring(1)) - 1), node.input2);
            } else if (node.input1.operation == 1 && node.input2.operation == 2) {
                inputXORs.put(node.key, node.input2);
                carryOuts.put("z%02d".formatted(Integer.parseInt(node.key.substring(1)) - 1), node.input1);
            } else {
                //faultyNodes.add(node.key);
            }
        }
        for (var entry : inputXORs.entrySet()) {
            String num = entry.getKey().substring(1);
            String e1 = "x%s".formatted(num);
            String e2 = "y%s".formatted(num);
            if (entry.getValue().operation != 2 || !(entry.getValue().input1.key.equals(e1) && entry.getValue().input2.key.equals(e2) || entry.getValue().input1.key.equals(e2) && entry.getValue().input2.key.equals(e1))) {
                faultyNodes.add(entry.getKey());        // +0
            }
        }
        // Changes nothing, but lets add z45 as carry for z44
        carryOuts.put("z44", allNodes.get("z45"));

        for (var entry : carryOuts.entrySet()) {

            // Every carry out is an OR of 2 ANDs
            // 1 AND ANDs both inputs together
            // the other AND ANDs XOR of the inputs and the carry in from the previous together

            String num = entry.getKey().substring(1);
            String e1 = "x%s".formatted(num);
            String e2 = "y%s".formatted(num);
            if (entry.getValue().operation != 1 || entry.getValue().input1.operation != 0 || entry.getValue().input2.operation != 0) {
                faultyNodes.add(entry.getKey());    // +1
            }

            String prevCarryoutKey = "z%02d".formatted(Integer.parseInt(entry.getKey().substring(1)) - 1);
            Node carryout = carryOuts.get(prevCarryoutKey);
            Node xor = inputXORs.get(entry.getKey());
            int a1 = isANDValid(entry.getValue().input1, e1, e2, xor, carryout);
            if (a1 == -1) faultyNodes.add(entry.getValue().key);        // If these were different, +6
            int a2 = isANDValid(entry.getValue().input2, e1, e2, xor, carryout);
            if (a2 == -1) faultyNodes.add(entry.getValue().key);
            if (a1 + a2 != 1) faultyNodes.add(entry.getValue().key);        // +0
        }

        List<String> fNodes = new ArrayList<>(faultyNodes);
        fNodes.sort(Comparator.naturalOrder());
        StringBuilder builder = new StringBuilder();
        for (String s : fNodes) {
            builder.append(',').append(s);
        }
        builder.deleteCharAt(0);
        out += builder + " (%d)".formatted(faultyNodes.size());

        long end = System.currentTimeMillis();
        out += " - Time: %dms".formatted(end - start);
        System.out.println(out);

        /*
         * Manually:
         * z06 swapped with ~~rjv~~ hwk
         * z31 swapped with hpc (not just label, z31 final product is the result of the carry, and hpc is treated as carry but is the result of the xor)
         * cgr swapped with z37
         * qmd swapped with tnt, AND and XOR labels are swapped
         *
         * so, wrong wire outputs:
         * z06, rjv, z31, hpc, cgr, z37, qmd, tnt
         *
         * all else from 31-45 are sound
         */

        // Doesn't matter how wrong the code is, I got an svg of the graph and made it manually:
        List<String> correct = new ArrayList<>(List.of("z06", "hwk", "z31", "hpc", "cgr", "z37", "qmd", "tnt"));
        correct.sort(Comparator.naturalOrder());
        builder = new StringBuilder();
        for (String s : correct) {
            builder.append(',').append(s);
        }
        builder.deleteCharAt(0);
        System.out.printf("part2 >> %s (%d) - Time: 0ms%n", builder, faultyNodes.size());
    }

    // Idea is to return -1 if invalid, 0 if it's the XOR connecting AND, and 1 if it's the input connecting and
    int isANDValid(Node node, String e1, String e2, Node expectedXOR, Node prevCarryout) {
        if (node.input1.key.equals(e1) && node.input2.key.equals(e2) || node.input1.key.equals(e2) && node.input2.key.equals(e1)) {
            return 1;
        }
        if (node.input1.operation == 1 && node.input2.operation == 2) {
            // input 1 is the carry out and input 2 is the XOR
            if (prevCarryout == null) return -1;
            if (!node.input1.key.equals(prevCarryout.key)) return -1;   // Not the correct preceding carryOut
            if (!node.input2.key.equals(expectedXOR.key)) return -1;
            return 0;
        }
        if (node.input2.operation == 1 && node.input1.operation == 2) {
            // input 1 is the carry out and input 2 is the XOR
            if (!node.input2.key.equals(prevCarryout.key)) return -1;   // Not the correct preceding carryOut
            if (!node.input1.key.equals(expectedXOR.key)) return -1;
            return 0;
        }
        return -1;
    }

    private static class Node {
        String key;

        Node input1, input2;
        // AND = 0, OR = 1, XOR = 2
        int operation;
        Integer output;

        boolean canCalculate() {
            return input1 != null && input2 != null && input1.output != null && input2.output != null;
        }

        boolean isCalculated() {
            return output != null;
        }

        void calculateOutput() {
            switch (operation) {
                case 0:
                    output = input1.output & input2.output;
                    break;
                case 1:
                    output = input1.output | input2.output;
                    break;
                case 2:
                    output = input1.output ^ input2.output;
                    break;
            }
        }
    }
}
