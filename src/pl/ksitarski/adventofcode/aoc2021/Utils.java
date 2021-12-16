package pl.ksitarski.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {

    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();

        try (
             FileInputStream fileInputStream = new FileInputStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static class Pair<T, U> {
        private final T t;
        private final U u;

        public Pair(T t, U u) {
            this.t = t;
            this.u = u;
        }

        public T getT() {
            return t;
        }

        public U getU() {
            return u;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(t, pair.t) && Objects.equals(u, pair.u);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t, u);
        }

        @Override
        public String toString() {
            return "<" + t + ", " + u + ">";
        }
    }

    public static class Coords {
        private final int x;
        private final int y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static Coords of(int x, int y) {
            return new Coords(x,  y);
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

    public static class Map2d<T> {
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
            System.out.println("===");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Coords coords = new Coords(x, y);
                    Optional<T> optionalT = get(coords);
                    if (optionalT.isPresent()) {
                        System.out.print(fun.apply(optionalT.get()));
                    } else {
                        System.out.print(def);
                    }
                }
                System.out.println();
            }
            System.out.println("===");

        }
    }

    public interface Map2dIterator<T> {
        void doSth(Coords coords, T value, Consumer<T> modifyThis, Consumer<Map2dIterator<T>> aroundThis);
    }
}
