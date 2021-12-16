package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day7Part1().solve(Utils.readFile("day7.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        List<Integer> positions = Arrays.stream(lines.get(0).split(","))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        int smallestAmountOfFuelToSpend = Integer.MAX_VALUE;

        int minTriedPosition = positions.get(0);
        int maxTriedPosition = positions.get(positions.size() - 1);

        for (int i = minTriedPosition; i <= maxTriedPosition; i++) {
            int fuel = 0;
            for (int position : positions) {
                fuel += Math.abs(position - i);
            }
            if (fuel < smallestAmountOfFuelToSpend) {
                smallestAmountOfFuelToSpend = fuel;
            }
        }

        return smallestAmountOfFuelToSpend;
    }
}
