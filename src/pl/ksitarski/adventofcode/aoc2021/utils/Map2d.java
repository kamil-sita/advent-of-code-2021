package pl.ksitarski.adventofcode.aoc2021.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Map2d<T> {
    private final Map<Coords, T> map = new HashMap<>();
    private int width;
    private int height;

    public Map2d() {
    }

    public static Map2d<Integer> fromStrings(List<String> strings) {
        Map2d<Integer> map2d = new Map2d<>();
        for (int y = 0; y < strings.size(); y++) {
            String s = strings.get(y);
            for (int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                map2d.put(Coords.of(x, y), c - '0');
            }
        }
        return map2d;
    }

    public static Map2d<Integer> fromStrings(List<String> strings, MapImport<Character, Integer> mapImport) {
        Map2d<Integer> map2d = new Map2d<>();
        for (int y = 0; y < strings.size(); y++) {
            String s = strings.get(y);
            for (int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                Coords coords = Coords.of(x, y);
                mapImport.transfer(c, coords, map2d::put);
            }
        }
        return map2d;
    }

    public interface MapImport<T, U> {
        void transfer(T t, Coords coords, BiConsumer<Coords, U> applyFunc);
    }

    public Map2d(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void put(Coords coords, T t) {
        if (coords.getX() >= width) {
            width = coords.getX() + 1;
        }
        if (coords.getY() >= height) {
            height = coords.getY() + 1;
        }
        map.put(coords, t);
    }

    public Optional<T> get(Coords coords) {
        return map.containsKey(coords) ? Optional.of(map.get(coords)) : Optional.empty();
    }

    public T forceGet(Coords coords) {
        return map.get(coords);
    }

    public List<Coords> getPositionsAround(Coords coords) {
        return getPositionsAround(coords, true);
    }

    public List<Coords> getPositionsAround(Coords coords, boolean diagonals) {
        return coords.around(diagonals)
                .stream()
                .filter(this::coordExists)
                .collect(Collectors.toList());
    }

    public boolean coordExists(Coords coords) {
        return map.containsKey(coords);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Coords bottomRightCoord() {
        return new Coords(width - 1, height - 1);
    }

    public void setWidth(int width) {
        if (this.width > width) {
            throw new RuntimeException();
        }
        this.width = width;
    }

    public void setHeight(int height) {
        if (this.height > height) {
            throw new RuntimeException();
        }
        this.height = height;
    }

    public void iterator(Map2dIterator<T> iterator) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coords coords = new Coords(x, y);
                createIterator(iterator, coords);
            }
        }
    }

    private void createIterator(Map2dIterator<T> iterator, Coords coords) {
        iterator.doSth(
                coords,
                forceGet(coords),
                (t) -> put(coords, t),
                (iter) -> {
                    List<Coords> coordsAround = getPositionsAround(coords);
                    for (Coords coordAround : coordsAround) {
                        createIterator(iter, coordAround);
                    }
                }
        );
    }

    public void iteratorOnPresent(Map2dIterator<T> iterator) {
        map.forEach((coords, v) -> createIterator(iterator, coords));
    }

    public void print() {
        print(Object::toString, ".");
    }

    public void print(Function<T, String> fun, String def) {
        print(fun, def, true, new Output() {
            @Override
            public void print(String s) {
                System.out.print(s);
            }

            @Override
            public void println(String s) {
                System.out.println(s);
            }
        });
    }

    public void print(Function<T, String> fun, String def, boolean separators, Output output) {
        if (separators) {
            output.println("===");
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coords coords = new Coords(x, y);
                Optional<T> optionalT = get(coords);
                if (optionalT.isPresent()) {
                    output.print(fun.apply(optionalT.get()));
                } else {
                    output.print(def);
                }
            }
            output.println();
        }
        if (separators) {
            output.println("===");
        }
    }
}
