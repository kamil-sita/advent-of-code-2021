package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Coords;
import pl.ksitarski.adventofcode.aoc2021.utils.Map2d;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.utils.Utils.readFile;


public class Day15Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day15Part1().solve(readFile("day15.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        Map2d<Integer> map2d = Map2d.fromStrings(lines);

        PriorityQueue<PathPoint> priorityQueue = new PriorityQueue<>();
        Set<Coords> analyzed = new HashSet<>();
        priorityQueue.add(new PathPoint(0, new Coords(0, 0)));

        while (true) {
            PathPoint pathPoint = priorityQueue.poll();

            if (pathPoint.coords.equals(new Coords(map2d.getHeight() - 1, map2d.getWidth() - 1))) {
                return pathPoint.cost;
            }

            int pathCost = pathPoint.cost;

            Coords coordsDown = pathPoint.coords.withYDiff(1);
            map2d.get(coordsDown).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsDown, tileCost);
            });

            Coords coordsRight = pathPoint.coords.withXDiff(1);
            map2d.get(coordsRight).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsRight, tileCost);
            });

            Coords coordsLeft = pathPoint.coords.withXDiff(-1);
            map2d.get(coordsLeft).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsLeft, tileCost);
            });

            Coords coordsUp = pathPoint.coords.withYDiff(-1);
            map2d.get(coordsUp).ifPresent(tileCost -> {
                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coordsUp, tileCost);
            });
        }
    }

    private void addIfNotYetAnalyzed(PriorityQueue<PathPoint> priorityQueue, Set<Coords> analyzed, int pathCost, Coords thisCoords, Integer tileCost) {
        if (!analyzed.contains(thisCoords)) {
            priorityQueue.add(new PathPoint(pathCost + tileCost, thisCoords));
            analyzed.add(thisCoords);
        }
    }


    private static class PathPoint implements Comparable<PathPoint> {
        private final int cost;
        private final Coords coords;

        public PathPoint(int cost, Coords coords) {
            this.cost = cost;
            this.coords = coords;
        }

        @Override
        public int compareTo(PathPoint o) {
            return Integer.compare(cost, o.cost);
        }

    }
}