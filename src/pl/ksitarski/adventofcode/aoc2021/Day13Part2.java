package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Coords;
import pl.ksitarski.adventofcode.aoc2021.utils.Map2d;
import pl.ksitarski.adventofcode.aoc2021.utils.Output;

import java.util.List;

import static pl.ksitarski.adventofcode.aoc2021.utils.Utils.readFile;


public class Day13Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day13Part2().solve(readFile("day13.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        Paper paper = new Paper();

        for (String line : lines) {
            if (line.contains(",")) {
                String[] coords = line.split(",");
                paper.putDot(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            }
            if (line.contains("=")) {
                boolean horizontal = line.startsWith("fold along y=");
                int v = Integer.parseInt(line.split("=")[1]);
                paper.fold(horizontal, v);
            }
        }

        return paper.printToString();
    }

    private static class Paper {
        private Map2d<Boolean> map = new Map2d<>();

        public void putDot(int x, int y) {
            map.put(new Coords(x, y), true);
        }

        public void fold(boolean horizontal, int v) {
            Map2d<Boolean> newMap = new Map2d<>();
            map.iteratorOnPresent((coords, value, modifyThis, aroundThis) -> {
                int x = coords.getX();
                int y = coords.getY();
                if (horizontal && y > v) {
                    y = 2 * v - y;
                }
                if (!horizontal && x > v) {
                    x = 2 * v - x;
                }
                newMap.put(new Coords(x, y), true);
            });
            map = newMap;
        }

        public int count() {
            final int[] a = {0};
            map.iteratorOnPresent((coords, value, modifyThis, aroundThis) -> a[0]++);
            return a[0];
        }

        public void print() {
            map.print(b -> "#", " ");
        }

        public String printToString() {
            StringBuilder sb = new StringBuilder();
            map.print(b -> "#", " ", false, new Output() {
                @Override
                public void print(String s) {
                    sb.append(s);
                }

                @Override
                public void println(String s) {
                    sb.append("\r\n");
                }
            });
            return sb.toString();
        }
    }
}