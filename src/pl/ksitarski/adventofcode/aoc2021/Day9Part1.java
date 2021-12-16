package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Coords;
import pl.ksitarski.adventofcode.aoc2021.utils.Utils;

import java.util.*;


public class Day9Part1 implements Solution {


    public static void main(String[] args) {
        System.out.println(new Day9Part1().solve(Utils.readFile("day9.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
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

        int sumOfRiskPoints = 0;

        {
            for (int y = 0; y < cave.getHeight(); y++) {
                outer: for (int x = 0; x < cave.getWidth(); x++) {
                    int v = cave.getValueFrom(x, y).get();
                    List<Integer> valuesAround = cave.getValuesAround(x, y);

                    for (int i = 0; i < valuesAround.size(); i++) {
                        if (v >= valuesAround.get(i)) {
                            continue outer;
                        }
                    }

                    sumOfRiskPoints += (v + 1);
                }
            }
        }

        return sumOfRiskPoints;
    }

    private static class Cave {
        private final Map<Coords, Integer> map = new HashMap<>(); //probably would be more optimal as 2D array of 2D list
        private int width = 0;
        private int height = 0;

        public void put(int x, int y, int v) {
            if (x >= width) {
                width = x + 1;
            }
            if (y >= height) {
                height = y + 1;
            }
            map.put(new Coords(x, y), v);
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Optional<Integer> getValueFrom(int x, int y) {
            if (map.containsKey(new Coords(x, y))) {
                return Optional.of(map.get(new Coords(x, y)));
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

        private void addIfExists(int x, int y, List<Integer> values) {
            if (getValueFrom(x, y).isPresent()) {
                values.add(getValueFrom(x, y).get());
            }
        }


    }
}