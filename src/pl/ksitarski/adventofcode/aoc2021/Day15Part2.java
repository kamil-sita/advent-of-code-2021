package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Coords;
import pl.ksitarski.adventofcode.aoc2021.utils.Map2d;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import static pl.ksitarski.adventofcode.aoc2021.utils.Utils.readFile;


public class Day15Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day15Part2().solve(readFile("day15.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        int width = lines.get(0).length();
        int height = lines.size();

        Map2d<Integer> map2d = Map2d.fromStrings(lines, (character, coords, applyFunc) -> {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    int v = character - '0';
                    applyFunc.accept(coords.withDiff(i * width, j * height), (v + i + j - 1) % 9 + 1);
                }
            }
        });

        PriorityQueue<PathPoint> priorityQueue = new PriorityQueue<>();
        Set<Coords> analyzed = new HashSet<>();
        priorityQueue.add(new PathPoint(0, new Coords(0, 0)));

        while (!priorityQueue.isEmpty()) {
            PathPoint pathPoint = priorityQueue.poll();

            if (pathPoint.coords.equals(map2d.bottomRightCoord())) {
                return pathPoint.cost;
            }

            int pathCost = pathPoint.cost;

            pathPoint.coords.around(false)
                    .stream()
                    .filter(map2d::coordExists)
                    .forEach(
                            coord -> {
                                addIfNotYetAnalyzed(priorityQueue, analyzed, pathCost, coord, map2d.forceGet(coord));
                            }
                    );
        }
        throw new RuntimeException();
    }

    private void addIfNotYetAnalyzed(PriorityQueue<PathPoint> priorityQueue, Set<Coords> analyzed, int pathCost, Coords thisCoords, Integer tileCost) {
        if (!analyzed.contains(thisCoords)) {
            priorityQueue.add(new PathPoint(pathCost + tileCost, thisCoords));
            analyzed.add(thisCoords);
        }
    }


    private record PathPoint(int cost, Coords coords) implements Comparable<PathPoint> {

        @Override
        public int compareTo(PathPoint o) {
            return Integer.compare(cost, o.cost);
        }

    }
}