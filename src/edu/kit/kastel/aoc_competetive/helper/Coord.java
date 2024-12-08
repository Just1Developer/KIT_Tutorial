package edu.kit.kastel.aoc_competetive.helper;

import java.util.Objects;

public class Coord {
    private final int x, y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Coord(Coord coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public Coord add(int x, int y) {
        return new Coord(this.x + x, this.y + y);
    }

    public Coord add(Coord coord) {
        return new Coord(this.x + coord.x, this.y + coord.y);
    }
    public Coord subtract(Coord coord) {
        return new Coord(this.x - coord.x, this.y - coord.y);
    }
    public Coord abs() {
        return new Coord(Math.abs(this.x), Math.abs(this.y));
    }

    /**
     * Returns absolute distance as Coordinates x and y.
     * @param coord The coordinate to calculate the distance to.
     * @return Distance as x and y.
     */
    public Coord diff(Coord coord) {
        return new Coord(this.x - coord.x, this.y - coord.y).abs();
    }
    public static Coord diff(Coord coord1, Coord coord2) {
        if (coord1 == null) return coord2;
        return coord1.diff(coord2);
    }

    public boolean isOutOfBounds(char[][] gridForSize) {
        if (x < 0 || y < 0) return true;
        if (y >= gridForSize.length || x >= gridForSize[y].length) return true;
        return false;
    }
    public boolean isInBounds(char[][] gridForSize) {
        if (x < 0 || y < 0) return false;
        if (y >= gridForSize.length || x >= gridForSize[y].length) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(x, y);
    }
}
