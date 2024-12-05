package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 extends FileReader {

    private static int DAY = 5;

    int beginUpdateLine = 0;
    private String[] lines;
    Map<Integer, List<Integer>> preconditions;

    /**
     * Initializes code for day 3.
     * @param exampleNr The example number for the filepath.
     */
    public Day5(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);
        preconditions = new HashMap<>();
        Pattern p = Pattern.compile("(\\d+)\\|(\\d+)");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.isBlank()) {
                beginUpdateLine = i + 1;
                break;
            }
            Matcher m = p.matcher(line);
            if (!m.matches()) continue;
            int y = Integer.parseInt(m.group(1)), x = Integer.parseInt(m.group(2));
            if (!preconditions.containsKey(y)) preconditions.put(y, new ArrayList<Integer>());
            preconditions.get(y).add(x);
        }
    }

    public void part1() {
        String out = "part1 >> ";
        int sum = 0;

        for (int i = beginUpdateLine; i < lines.length; i++) {
            String line = lines[i];
            String[] nums = line.split(",");
            List<Integer> occurred = new ArrayList<>();
            boolean isOrdered = true;
            for (String num : nums) {
                if (num.isBlank()) continue;
                int n = Integer.parseInt(num);
                isOrdered &= isOrderedWith(n, occurred);
                occurred.add(n);
                //N++;
            }
            //System.out.println(isOrdered);
            if (isOrdered) sum += middleOf(nums);
        }

        out += sum;
        System.out.println(out);
    }

    //int N = 1;

    private boolean isOrderedWith(int n, List<Integer> occurred) {
        if (!preconditions.containsKey(n)) return true;
        for (int precondition : preconditions.get(n)) {
            if (occurred.contains(precondition)) {
                //System.out.printf("%d. Failed precondition: %d because occurred list contained %d%n", N, n, precondition);
                return false;
            }
        }
        return true;
    }

    private int middleOf(String[] nums) {
        return Integer.parseInt(nums[nums.length >> 1]);
    }



    public void part2() {
        String out = "part2 >> ";
        int sum = 0;

        for (int i = beginUpdateLine; i < lines.length; i++) {
            String line = lines[i];
            String[] nums = line.split(",");
            List<Integer> occurred = new ArrayList<>();
            boolean isOrdered = true;
            for (String num : nums) {
                if (num.isBlank()) continue;
                int n = Integer.parseInt(num);
                /*
                int collisionIndex = isOrderedWith2(n, occurred);
                if (collisionIndex != -1) {
                    occurred.add(collisionIndex, n);
                    isOrdered = false;
                } else {
                    occurred.add(n);
                }*/
                isOrdered &= isOrderedWith(n, occurred);
                occurred.add(n);
            }
            //System.out.println(isOrdered);
            if (!isOrdered) {
                sortTillValid(occurred);
                //System.out.println(Arrays.toString(occurred.toArray()));
                //System.out.println(occurred.get(occurred.size() >> 1));
                sum += occurred.get(occurred.size() >> 1);
            }
        }

        out += sum;
        System.out.println(out);
    }

    private void sortTillValid(List<Integer> list) {
        while (true) {
            List<Integer> occurred = new ArrayList<>();
            boolean isOrdered = true;
            for (int i = 0; i < list.size(); i++) {
                int n = list.get(i);
                int collisionIndex = isOrderedWith2(n, occurred);
                if (collisionIndex != -1) {
                    occurred.add(collisionIndex, n);
                    list.remove((Object) n);
                    list.add(collisionIndex, n);
                    isOrdered = false;
                    break;
                } else {
                    occurred.add(n);
                }
            }
            if (isOrdered) return;
        }
    }

    private int isOrderedWith2(int n, List<Integer> occurred) {
        if (!preconditions.containsKey(n) || occurred.isEmpty()) return -1;
        for (int precondition : preconditions.get(n)) {
            if (occurred.contains(precondition)) {
                return occurred.indexOf(precondition);
            }
        }
        return -1;
    }


    /*
    public void part2() {
        String out = "part2 >> ";
        int sum = 0;

        for (int i = beginUpdateLine; i < lines.length; i++) {
            String line = lines[i];
            String[] nums = line.split(",");
            List<Integer> occurred = new ArrayList<>();
            boolean isOrdered = true;
            for (String num : nums) {
                if (num.isBlank()) continue;
                int n = Integer.parseInt(num);
                int insertOffset = isOrderedWith2(n, occurred);
                occurred.add(occurred.size() - insertOffset, n);
                isOrdered &= insertOffset == 1;
                //N++;
            }
            //System.out.println(isOrdered);
            if (!isOrdered) {
                System.out.println(Arrays.toString(occurred.toArray()));
                System.out.println(occurred.get(occurred.size() >> 1));
                sum += occurred.get(occurred.size() >> 1);
            }
        }

        out += sum;
        System.out.println(out);
    }

    private int isOrderedWith2(int n, List<Integer> occurred) {
        if (!preconditions.containsKey(n) || occurred.isEmpty()) return 0;
        int insertOffset = 0;
        for (int precondition : preconditions.get(n)) {
            if (occurred.contains(precondition)) {
                // Maintain order within occurred for the rest
                occurred.remove((Object) precondition);
                occurred.add(precondition);
                insertOffset++;
            }
        }
        return insertOffset;
    }*/

}
