package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.Utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day5Part1 implements Solution {
    public static void main(String[] args) {
        System.out.println(new Day5Part1().solve(Utils.readFile("day5.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        Board board = new Board();

        for (String line : lines) {
            String[] coords = line.split(" -> ");
            String[] firstCoords = coords[0].split(",");
            String[] secondCoords = coords[1].split(",");
            board.addLine(
                    new Pair<>(Integer.parseInt(firstCoords[0]), Integer.parseInt(firstCoords[1])),
                    new Pair<>(Integer.parseInt(secondCoords[0]), Integer.parseInt(secondCoords[1]))
            );
        }
        return board.overlapCount();
    }

    public static class Board {
        private final Map<Pair<Integer, Integer>, Integer> map = new HashMap<>();

        public void addLine(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
            if (Objects.equals(from.getT(), to.getT())) { //vertical
                int x = from.getT();
                int smallerY = Math.min(from.getU(), to.getU());
                int biggerY = Math.max(from.getU(), to.getU());

                for (int y = smallerY; y <= biggerY; y++) {
                    putAtPoint(x, y);
                }
            } else if (Objects.equals(from.getU(), to.getU())) { //horizontal
                int y = from.getU();
                int smallerX = Math.min(from.getT(), to.getT());
                int biggerX = Math.max(from.getT(), to.getT());

                for (int x = smallerX; x <= biggerX; x++) {
                    putAtPoint(x, y);
                }
            } else {
                //skip
            }
        }

        private void putAtPoint(int x, int y) {
            Pair<Integer, Integer> key = new Pair<>(x, y);
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }

        public int overlapCount() {
            return (int) map.values()
                    .stream()
                    .filter(integer -> integer > 1)
                    .count();
        }
    }

}
