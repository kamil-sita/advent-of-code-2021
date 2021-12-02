package pl.ksitarski.adventofcode.aoc2021;

import java.util.List;

public class Day2Part1 implements Solution {


    public static void main(String[] args) {
        System.out.println(new Day2Part1().solve(Utils.readFile("day2.txt")));
    }

    @Override
    public int solve(List<String> lines) {
        int horizontalPosition = 0;
        int depth = 0;

        for (String line : lines) {
            String[] lineSplit = line.split(" ");
            String command = lineSplit[0];
            int value = Integer.parseInt(lineSplit[1]);

            switch (command) {
                case "up" -> {
                    depth -= value;
                }
                case "down" -> {
                    depth += value;
                }
                case "forward" -> {
                    horizontalPosition += value;
                }
            }
        }

        return horizontalPosition * depth;
    }
}
