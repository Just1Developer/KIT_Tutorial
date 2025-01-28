package edu.kit.kastel.debugging.cinema;

import java.util.HashMap;
import java.util.Map;

class RabattManager {
    private Map<String, Double> rabatte = new HashMap<>();

    public void setRabatt(String film, double prozentsatz) {
        rabatte.put(film, prozentsatz / 100);
    }

    public double getRabatt(String film) {
        return rabatte.getOrDefault(film, 0.0);
    }
}
