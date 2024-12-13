package edu.kit.kastel.aoc_competetive.helper;

import java.util.Objects;

public class Coord64 {
    private final long x, y;

    public Coord64(long x, long y) {
        this.x = x;
        this.y = y;
    }
    public Coord64(Coord64 coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public long getX() {
        return x;
    }
    public long getY() {
        return y;
    }

    public Coord64 add(long x, long y) {
        return new Coord64(this.x + x, this.y + y);
    }

    public Coord64 add(Coord64 coord) {
        return new Coord64(this.x + coord.x, this.y + coord.y);
    }
    public Coord64 subtract(Coord64 coord) {
        return new Coord64(this.x - coord.x, this.y - coord.y);
    }
    public Coord64 abs() {
        return new Coord64(Math.abs(this.x), Math.abs(this.y));
    }

    /**
     * Returns absolute distance as Coordinates x and y.
     * @param coord The coordinate to calculate the distance to.
     * @return Distance as x and y.
     */
    public Coord64 diff(Coord64 coord) {
        return new Coord64(this.x - coord.x, this.y - coord.y).abs();
    }
    public static Coord64 diff(Coord64 coord1, Coord64 coord2) {
        if (coord1 == null) return coord2;
        return coord1.diff(coord2);
    }

    public boolean isOutOfBounds(char[][] gridForSize) {
        if (x < 0 || y < 0) return true;
        if (y >= gridForSize.length || x >= gridForSize[(int)y].length) return true;
        return false;
    }
    public boolean isInBounds(char[][] gridForSize) {
        if (x < 0 || y < 0) return false;
        if (y >= gridForSize.length || x >= gridForSize[(int)y].length) return false;
        return true;
    }
    public boolean isInBounds(String[] gridForSize) {
        if (x < 0 || y < 0) return false;
        if (y >= gridForSize.length || x >= gridForSize[(int)y].length()) return false;
        return true;
    }
    public <T> boolean isInBounds(T[][] gridForSize) {
        if (x < 0 || y < 0) return false;
        if (y >= gridForSize.length || x >= gridForSize[(int)y].length) return false;
        return true;
    }
    public boolean isInBounds(long height, long width) {
        if (x < 0 || y < 0) return false;
        if (y >= height || x >= width) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord64 coord = (Coord64) o;
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
