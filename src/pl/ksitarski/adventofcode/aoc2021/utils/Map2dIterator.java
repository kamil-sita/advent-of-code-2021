package pl.ksitarski.adventofcode.aoc2021.utils;

import java.util.function.Consumer;

public interface Map2dIterator<T> {
    void doSth(Coords coords, T value, Consumer<T> modifyThis, Consumer<Map2dIterator<T>> aroundThis);
}
