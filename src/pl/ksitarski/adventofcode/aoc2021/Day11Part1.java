package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.Utils.*;


public class Day11Part1 implements Solution {

    private static final int FLASH_ENERGY_LEVEL = 9;

    public static void main(String[] args) {
        System.out.println(new Day11Part1().solve(readFile("day11.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
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

        for (int i = 0; i < 100; i++) {
            octupusMatrix.print();
            octupusMatrix.iterate();
        }
        octupusMatrix.print();

        return octupusMatrix.getFlashCount();
    }

    private static class OctupusMatrix {
        private final Utils.Map2d<Integer> map2d = new Utils.Map2d<>();
        private final Set<Coords> flashed = new HashSet<>();
        private int flashCount = 0;

        public void put(Coords coords, Integer val) {
            map2d.put(coords, val);
        }

        public void iterate() {
            incrementAllByOne();
            flash();
            keepEnergyLevelsAfterFlash();
            flashed.clear();
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