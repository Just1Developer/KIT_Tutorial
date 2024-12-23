package edu.kit.kastel.aoc_competetive;

import edu.kit.kastel.FileReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 extends FileReader {

    private static final int DAY = 23;
    private String[] lines;

    List<String> computers;
    Set<Edge> edges;
    List<Set<String>> relevantConnections;

    /**
     * Initializes code for day {@code DAY}.
     * @param exampleNr The example number for the filepath.
     */
    public Day23(int exampleNr) {
        String filepath = "./input/day%d%s.txt".formatted(DAY, exampleNr > 0 ? "e%d".formatted(exampleNr) : "");
        lines = readFile(filepath);

        computers = new ArrayList<>();
        edges = new HashSet<>();

        for (String line : lines) {
            String comp1 = line.substring(0, 2);
            String comp2 = line.substring(3);

            int c1Index, c2Index;

            if (!computers.contains(comp1)) {
                c1Index = computers.size();
                computers.add(comp1);
            } else {
                c1Index = computers.indexOf(comp1);
            }
            if (!computers.contains(comp2)) {
                c2Index = computers.size();
                computers.add(comp2);
            } else {
                c2Index = computers.indexOf(comp2);
            }

            edges.add(new Edge(c1Index, c2Index));
            edges.add(new Edge(c2Index, c1Index));
        }
    }

    private Set<Set<Integer>> findGraphTriangles(Set<Edge> edges) {
        // Build adjacency list
        Map<Integer, Set<Integer>> adjacencyList = edges.stream()
                .flatMap(edge -> Stream.of(
                        Map.entry(edge.u(), edge.v()),
                        Map.entry(edge.v(), edge.u())
                ))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
                ));

        Set<Set<Integer>> cliques = new HashSet<>();

        // Iterate over each edge (u, v)
        for (Edge edge : edges) {
            int u = edge.u();
            int v = edge.v();

            // Find common neighbors of u and v
            Set<Integer> neighborsU = adjacencyList.getOrDefault(u, Set.of());
            Set<Integer> neighborsV = adjacencyList.getOrDefault(v, Set.of());

            for (Integer w : neighborsU) {
                if (neighborsV.contains(w)) {
                    // Found a triangle (u, v, w)
                    cliques.add(Set.of(u, v, w));
                }
            }
        }

        return cliques;
    }

    boolean isCliqueValid(Set<Integer> clique) {
        for (int vertex : clique) {
            if (computers.get(vertex).charAt(0) == 't') return true;
        }
        return false;
    }

    public void part1() {
        String out = "part1 >> ";
        long result = 0;
        long start = System.currentTimeMillis();

        var cliques = findGraphTriangles(edges);
        for (var clique : cliques) {
            if (isCliqueValid(clique)) result++;
        }

        long end = System.currentTimeMillis();
        out += result;
        out += ", Time: " + (end - start) + "ms";
        System.out.println(out);
    }


    // Part 2, as expected, the regular Clique-Problem:


    private Set<Integer> findLargestClique(Set<Edge> edges) {
        // Build adjacency list
        Map<Integer, Set<Integer>> adjacencyList = edges.stream()
                .flatMap(edge -> Stream.of(
                        Map.entry(edge.u(), edge.v()),
                        Map.entry(edge.v(), edge.u())
                ))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
                ));

        // Prepare input for Bron-Kerbosch
        Set<Integer> allNodes = adjacencyList.keySet();
        Set<Integer> largestClique = new HashSet<>();
        bronKerbosch(new HashSet<>(), allNodes, new HashSet<>(), adjacencyList, largestClique);

        return largestClique;
    }

    private void bronKerbosch(
            Set<Integer> R,
            Set<Integer> P,
            Set<Integer> X,
            Map<Integer, Set<Integer>> adjacencyList,
            Set<Integer> largestClique
    ) {
        if (P.isEmpty() && X.isEmpty()) {
            // Found a maximal clique
            if (R.size() > largestClique.size()) {
                largestClique.clear();
                largestClique.addAll(R);
            }
            return;
        }

        Set<Integer> PClone = new HashSet<>(P);
        for (Integer node : PClone) {
            Set<Integer> neighbors = adjacencyList.getOrDefault(node, Set.of());
            bronKerbosch(
                    union(R, node),
                    intersection(P, neighbors),
                    intersection(X, neighbors),
                    adjacencyList,
                    largestClique
            );
            P.remove(node);
            X.add(node);
        }
    }

    private Set<Integer> union(Set<Integer> set, Integer element) {
        Set<Integer> result = new HashSet<>(set);
        result.add(element);
        return result;
    }

    private Set<Integer> intersection(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    private Set<Integer> difference(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }


    public void part2() {
        String out = "part2 >> ";
        long start = System.currentTimeMillis();

        var lanParty = findLargestClique(edges);
        List<String> puters = new ArrayList<>(lanParty.stream().map(id -> computers.get(id)).toList());
        puters.sort(Comparator.naturalOrder());
        StringBuilder builder = new StringBuilder();
        puters.forEach((a) -> builder.append(',').append(a));
        builder.deleteCharAt(0);    // Remove first comma
        out += builder;

        long end = System.currentTimeMillis();
        out += " - Time: %dms".formatted(end - start);
        System.out.println(out);
    }

    private record Edge(int u, int v) {    }
}
