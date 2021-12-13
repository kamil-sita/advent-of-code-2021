package pl.ksitarski.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    }

    public static class Map2d<T> {
        private final Map<Coords, T> map = new HashMap<>();
        private int width;
        private int height;

        public Map2d() {
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

        public List<T> getValuesAround(Coords coords) {
            List<T> values = new ArrayList<>();
            List<Coords> existingCoords = getPositionsAround(coords);
            for (Coords existingCoord : existingCoords) {
                values.add(get(existingCoord).get());
            }
            return values;
        }

        public List<Coords> getPositionsAround(Coords coords) {
            List<Coords> values = new ArrayList<>();
            addCoordIfExists(new Coords(coords.getX() - 1, coords.getY()), values);
            addCoordIfExists(new Coords(coords.getX() + 1, coords.getY()), values);
            addCoordIfExists(new Coords(coords.getX(), coords.getY() - 1), values);
            addCoordIfExists(new Coords(coords.getX(), coords.getY() + 1), values);
            addCoordIfExists(new Coords(coords.getX() - 1, coords.getY() + 1), values);
            addCoordIfExists(new Coords(coords.getX() + 1, coords.getY() - 1), values);
            addCoordIfExists(new Coords(coords.getX() - 1, coords.getY() - 1), values);
            addCoordIfExists(new Coords(coords.getX() + 1, coords.getY() + 1), values);
            return values;
        }

        private void addCoordIfExists(Coords coords, List<Coords> values) {
            if (get(coords).isPresent()) {
                values.add(coords);
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
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
