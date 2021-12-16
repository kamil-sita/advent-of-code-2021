package pl.ksitarski.adventofcode.aoc2021.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Coords {
    private final int x;
    private final int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coords of(int x, int y) {
        return new Coords(x, y);
    }

    public Coords withXDiff(int diffX) {
        return new Coords(x + diffX, y);
    }

    public Coords withYDiff(int diffY) {
        return new Coords(x, y + diffY);
    }

    public Coords withDiff(int diffX, int diffY) {
        return new Coords(x + diffX, y + diffY);
    }

    public List<Coords> around() {
        return around(true);
    }

    public List<Coords> around(boolean includeDiagonals) {
        List<Coords> coords = new ArrayList<>();
        coords.add(withXDiff(1));
        coords.add(withXDiff(-1));
        coords.add(withYDiff(1));
        coords.add(withYDiff(-1));
        if (includeDiagonals) {
            coords.add(withDiff(1, 1));
            coords.add(withDiff(-1, 1));
            coords.add(withDiff(1, -1));
            coords.add(withDiff(-1, -1));
        }
        return coords;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return x == coords.x && y == coords.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coords{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
