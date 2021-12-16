package pl.ksitarski.adventofcode.aoc2021.utils;

public interface Output {
    default void print() {
        print("");
    }

    void print(String s);

    default void println() {
        println("");
    }

    void println(String s);
}
