package edu.kit.kastel;

import java.util.List;
import java.util.Objects;

public class EqualsTestClass {
    private int attribute1;
    private String attribute2;
    private int attribute3;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        EqualsTestClass testClass = (EqualsTestClass) obj;
        return attribute1 == testClass.attribute1 && attribute3 == testClass.attribute3
                && Objects.equals(attribute2, testClass.attribute2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute1, attribute2, attribute3);
    }

    public boolean contains(String string, String target) {
        // Null check:
        if (string == null || target == null) return false;

        // Basisfälle:
        if (target.isEmpty()) return true;
        if (target.length() < string.length()) return false;    // Nicht möglich

        // Falls der String hier mit target beginnt, kommt target vor:
        if (string.startsWith(target)) return true;

        // Sonst, vorne ein Zeichen abschneiden und nochmal schauen: (Rekursion)
        return contains(string.substring(1), target);
    }

    public int calculateSum(List<Integer> numbers) {
        // Basisfälle:
        if (numbers == null || numbers.isEmpty()) return 0;
        if (numbers.size() == 1) return numbers.get(0);

        // Zähle mit rekursion alle anderen, und addiere das hier auf
        int firstValue = numbers.get(0);
        numbers.remove(0);                  // Element löschen
        int followingSum = calculateSum(numbers); // Rekursion
        numbers.add(0, firstValue);         // Liste wiederherstellen

        // Dann die von allen Zahlen in der Liste (bislang) zurückgeben:
        return firstValue + followingSum;
    }

    public int calculateSum2(List<Integer> numbers) {
        // Basisfälle:
        if (numbers == null || numbers.isEmpty()) return 0;
        if (numbers.size() == 1) return numbers.get(0);

        // Zähle mit rekursion alle anderen, und addiere das hier auf
        int currentSum = calculateSum(numbers.subList(1, numbers.size()));  // <-- Erstellt hier neue Liste, eher vermeiden
        currentSum += numbers.get(0);

        // Dann die von allen Zahlen in der Liste (bislang) zurückgeben:
        return currentSum;
    }
}









