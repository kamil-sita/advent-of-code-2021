package pl.ksitarski.adventofcode.aoc2021;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day15Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day15Part2().solve(readFile("day15.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        int width = lines.get(0).length();
        int height = lines.size();

        Utils.Map2d<Integer> map2d = Utils.Map2d.fromStrings(lines, (character, coords, applyFunc) -> {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    int v = character - '0';
                    applyFunc.accept(coords.withDiff(i * width, j * height), (v + i + j - 1) % 9 + 1);
                }
            }
        });

        PriorityQueue<PathPoint> priorityQueue = new PriorityQueue<>();
        Set<Utils.Coords> analyzed = new HashSet<>();
        priorityQueue.add(new PathPoint(0, new Utils.Coords(0, 0)));

        while (true) {
            PathPoint pathPoint = priorityQueue.poll();

            if (pathPoint.coords.equals(map2d.bottomRightCoord())) {
                return pathPoint.cost;
            }

            int pathCost = pathPoint.cost;

            Utils.Coords coordsDown = pathPoint.coords.withYDiff(1);
            map2d.get(coordsDown).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsDown, tileCost);
            });

            Utils.Coords coordsRight = pathPoint.coords.withXDiff(1);
            map2d.get(coordsRight).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsRight, tileCost);
            });

            Utils.Coords coordsLeft = pathPoint.coords.withXDiff(-1);
            map2d.get(coordsLeft).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsLeft, tileCost);
            });

            Utils.Coords coordsUp = pathPoint.coords.withYDiff(-1);
            map2d.get(coordsUp).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsUp, tileCost);
            });
        }
    }

    private void importMap(List<String> lines, Utils.Map2d<Integer> map2d, int i, int j, int width, int height) {
        int y = 0;
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                int actualX = width * i + x;
                int actualY = height * j + y;
                int v = (Integer.parseInt(line.charAt(x) + "") + i + j - 1) % 9 + 1;
                map2d.put(new Utils.Coords(actualX, actualY), v);
            }
            y++;
        }
    }

    private void addIfNotYetAnalyzed(PriorityQueue<PathPoint> priorityQueue, Set<Utils.Coords> analyzed, int pathCost, Utils.Coords thisCoords, Integer tileCost) {
        if (!analyzed.contains(thisCoords)) {
            priorityQueue.add(new PathPoint(pathCost + tileCost, thisCoords));
            analyzed.add(thisCoords);
        }
    }


    private static class PathPoint implements Comparable<PathPoint> {
        private final int cost;
        private final Utils.Coords coords;

        public PathPoint(int cost, Utils.Coords coords) {
            this.cost = cost;
            this.coords = coords;
        }

        @Override
        public int compareTo(PathPoint o) {
            return Integer.compare(cost, o.cost);
        }

    }
}