package edu.kit.kastel.debugging.cinema;

class Kunde {
    private String name;
    private int alter;

    public Kunde(String name, int alter) {
        this.name = name;
        this.alter = alter;
    }

    public String getName() {
        return name;
    }

    public int getAlter() {
        return alter;
    }
}
