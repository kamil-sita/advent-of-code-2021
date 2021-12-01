package pl.ksitarski.adventofcode.aoc2021;

import java.util.List;
import java.util.stream.Collectors;

public class Day1Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day1Part1().solve(Utils.readFile("day1.txt")));
    }

    @Override
    public int solve(List<String> lines) {
        List<Integer> integers = lines.stream().map(Integer::parseInt).collect(Collectors.toList());

        int biggerThanPrevious = 0;
        Integer previous = null;
        for (Integer current : integers) {
            if (previous != null && previous < current) {
                biggerThanPrevious++;
            }
            previous = current;
        }

        return biggerThanPrevious;
    }
}
