package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;


public class Day9Part2 implements Solution {


    public static void main(String[] args) {
        System.out.println(new Day9Part2().solve(Utils.readFile("day9.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        Cave cave = new Cave();

        {
            int y = 0;
            for (String line : lines) {
                int x = 0;
                for (char c : line.toCharArray()) {
                    cave.put(x, y, Integer.parseInt(c + ""));
                    x++;
                }
                y++;
            }
        }

        {
            for (int y = 0; y < cave.getHeight(); y++) {
                for (int x = 0; x < cave.getWidth(); x++) {
                    if (cave.isPartOfBasin(x, y)) {
                        if (cave.isPartOfKnownBasin(x, y)) {
                            continue;
                        } else {
                            Basin basin = new Basin();
                            Set<Utils.Coords> visited = new HashSet<>();
                            Set<Utils.Coords> toVisit = new HashSet<>(); //slower access time but don't have to keep track of things to visit
                            toVisit.add(new Utils.Coords(x, y));
                            while (toVisit.size() > 0) {
                                Utils.Coords coords = toVisit.stream().findAny().get();
                                if (visited.contains(coords)) {
                                    toVisit.remove(coords);
                                    continue;
                                }
                                if (!cave.isPartOfBasin(coords.getX(), coords.getY())) {
                                    visited.add(coords);
                                    toVisit.remove(coords);
                                    continue;
                                }
                                List<Utils.Coords> coordsAround = cave.getCoordsAround(coords.getX(), coords.getY());
                                toVisit.addAll(coordsAround);
                                basin.add(coords.getX(), coords.getY());
                                visited.add(coords);
                            }
                            cave.addBasin(basin);
                        }
                    }
                }
            }
        }

        List<Basin> basins = cave.getBasins();

        basins.sort(Comparator.comparingInt(Basin::size));

        int basinCount = basins.size();
        return basins.get(basinCount - 1).size() * basins.get(basinCount - 2).size() * basins.get(basinCount - 3).size();
    }

    private static class Cave {
        private final Map<Utils.Coords, Integer> map = new HashMap<>(); //probably would be more optimal as 2D array of 2D list
        private final List<Basin> basins = new ArrayList<>();
        private int width = 0;
        private int height = 0;

        public void put(int x, int y, int v) {
            if (x >= width) {
                width = x + 1;
            }
            if (y >= height) {
                height = y + 1;
            }
            map.put(new Utils.Coords(x, y), v);
        }

        public boolean isPartOfBasin(int x, int y) {
            return getValueFrom(x, y).get() < 9;
        }

        public boolean isPartOfKnownBasin(int x, int y) {
            for (Basin basin : basins) {
                if (basin.contains(x, y)) {
                    return true;
                }
            }
            return false;
        }

        public void addBasin(Basin basin) {
            basins.add(basin);
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Optional<Integer> getValueFrom(int x, int y) {
            if (map.containsKey(new Utils.Coords(x, y))) {
                return Optional.of(map.get(new Utils.Coords(x, y)));
            } else {
                return Optional.empty();
            }
        }

        public List<Integer> getValuesAround(int x, int y) {
            List<Integer> values = new ArrayList<>();
            addIfExists(x - 1, y, values);
            addIfExists(x + 1, y, values);
            addIfExists(x, y - 1, values);
            addIfExists(x, y + 1, values);
            return values;
        }

        public List<Utils.Coords> getCoordsAround(int x, int y) {
            List<Utils.Coords> values = new ArrayList<>();
            addCoordIfExists(x - 1, y, values);
            addCoordIfExists(x + 1, y, values);
            addCoordIfExists(x, y - 1, values);
            addCoordIfExists(x, y + 1, values);
            return values;
        }

        private void addIfExists(int x, int y, List<Integer> values) {
            if (getValueFrom(x, y).isPresent()) {
                values.add(getValueFrom(x, y).get());
            }
        }

        private void addCoordIfExists(int x, int y, List<Utils.Coords> values) {
            if (getValueFrom(x, y).isPresent()) {
                values.add(new Utils.Coords(x, y));
            }
        }

        public List<Basin> getBasins() {
            return basins;
        }
    }

    private static class Basin {
        private final Set<Utils.Coords> basin = new HashSet<>();

        public boolean contains(int x, int y) {
            return basin.contains(new Utils.Coords(x, y));
        }

        public void add(int x, int y) {
            basin.add(new Utils.Coords(x, y));
        }

        public int size() {
            return basin.size();
        }
    }
}