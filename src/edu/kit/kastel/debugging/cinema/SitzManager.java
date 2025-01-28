package edu.kit.kastel.debugging.cinema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class SitzManager {
    private Map<String, Set<Integer>> belegteSitze = new HashMap<>();

    public boolean reserviereSitz(String film, int platz) {
        if (!belegteSitze.containsKey(film)) {
            belegteSitze.put(film, new HashSet<>());
        }

        if (belegteSitze.get(film).contains(platz)) {
            return false; // Sitz bereits belegt
        }
        belegteSitze.get(film).add(platz);
        return true;
    }

    public boolean istSitzBelegt(String film, int platz) {
        return belegteSitze.containsKey(film) && belegteSitze.get(film).contains(platz);
    }
}
