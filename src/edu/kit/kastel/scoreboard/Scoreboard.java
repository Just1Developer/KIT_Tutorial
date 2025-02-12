package edu.kit.kastel.scoreboard;

public class Scoreboard<T extends Comparable<T>> {

    private T firstEntry = null;
    private T secondEntry = null;
    private T thirdEntry = null;

    private final GreaterThanComparator<T> comparator;

    public Scoreboard(GreaterThanComparator<T> comparator) {
        this.comparator = comparator;
    }

    public void add(T entry) {

        if (thirdEntry == null || comparator.isGreaterThan(thirdEntry, entry)) {
            thirdEntry = entry;
            if (secondEntry == null || comparator.isGreaterThan(secondEntry, thirdEntry)) {
                T temp = secondEntry;
                secondEntry = thirdEntry;
                thirdEntry = temp;
                if (secondEntry == null || comparator.isGreaterThan(firstEntry, secondEntry)) {
                    temp = firstEntry;
                    firstEntry = secondEntry;
                    secondEntry = temp;
                }
            }
        }
    }

    public T query(int i) {
        if (i < 1 || i > 3) return null;
        // ...
    }

}
