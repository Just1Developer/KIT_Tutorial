package edu.kit.kastel;

public enum TrafficDensity {
    SPARSE("Leichter Verkehr.", 20),
    MIDDLE("Mittlerer Verkehr.", 75),
    DENSE("Starker Verkehr.", 150),
    CHAOS("Los Angeles.", Integer.MAX_VALUE);

    private final String label;
    private final int maxCars;

    private TrafficDensity(String label, int maxCars) {
        this.label = label;
        this.maxCars = maxCars;
    }

    public String getLabel() {
        return label;
    }

    public int getMaxCars() {
        return maxCars;
    }

    public static TrafficDensity getDensity(int cars) {
        for (TrafficDensity density : values()) {
            if (cars <= density.getMaxCars()) return density;
        }
        // Unreachable here, because last density covers entire integer space
        return CHAOS;
    }
}



