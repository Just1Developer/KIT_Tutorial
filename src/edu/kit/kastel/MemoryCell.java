package edu.kit.kastel;

public class MemoryCell {
    private String command;
    private int argA;
    private int argB;

    public Element e;

    public MemoryCell(String command, int argA, int argB) {
        this.command = command;
        this.argA = argA;
        this.argB = argB;
    }

    public MemoryCell(MemoryCell other) {
        this.command = other.command;
        this.argA = other.argA;
        this.argB = other.argB;
    }
}
