package pl.ksitarski.adventofcode.aoc2021;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static pl.ksitarski.adventofcode.aoc2021.Utils.*;


public class Day11Part2 implements Solution {

    private static final int FLASH_ENERGY_LEVEL = 9;

    public static void main(String[] args) {
        System.out.println(new Day11Part2().solve(readFile("day11.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        OctupusMatrix octupusMatrix = new OctupusMatrix();

        {
            int y = 0;
            for (String line : lines) {
                int x = 0;
                for (char c : line.toCharArray()) {
                    octupusMatrix.put(new Coords(x, y), Integer.parseInt(c + ""));
                    x++;
                }
                y++;
            }
        }

        int i = 1;
        while (!octupusMatrix.iterate()) {
            octupusMatrix.print();
            i++;
        }
        octupusMatrix.print();
        return i;
    }

    private static class OctupusMatrix {
        private final Map2d<Integer> map2d = new Map2d<>();
        private final Set<Coords> flashed = new HashSet<>();
        private int flashCount = 0;

        public void put(Coords coords, Integer val) {
            map2d.put(coords, val);
        }

        public boolean iterate() {
            incrementAllByOne();
            flash();
            keepEnergyLevelsAfterFlash();
            flashed.clear();
            return allFlashed();
        }

        private boolean allFlashed() {
            final int[] flashedCount = {0};
            map2d.iterator((coords, value, modifyThis, aroundThis) -> {
                if (value == 0) {
                    flashedCount[0]++;
                }
            });

            return flashedCount[0] == map2d.getHeight() * map2d.getWidth();
        }

        private void incrementAllByOne() {
            map2d.iterator((coords, value, modifyThis, aroundThis) -> modifyThis.accept(value + 1));
        }

        private void flash() {
            final boolean[] anyModified = {false};
            do {
                anyModified[0] = false;
                map2d.iterator((coords, value, modifyThis, aroundThis) -> {
                    if (flashed.contains(coords)) {
                        return;
                    }
                    if (value > FLASH_ENERGY_LEVEL) {
                        aroundThis.accept((coords1, value1, modifyThis1, aroundThis1) -> modifyThis1.accept(value1 + 1));
                        flashed.add(coords);
                        flashCount++;
                        anyModified[0] = true;
                    }
                });
            } while(anyModified[0]);
        }

        private void keepEnergyLevelsAfterFlash() {
            map2d.iterator((coords, value, modifyThis, aroundThis) -> {
                if (flashed.contains(coords)) {
                    modifyThis.accept(0);
                }
            });
        }

        public int getFlashCount() {
            return flashCount;
        }

        public void print() {
            map2d.print();
        }
    }
}