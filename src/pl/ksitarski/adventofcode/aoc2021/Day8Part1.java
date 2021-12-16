package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day8Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day8Part1().solve(Utils.readFile("day8.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        List<FourDigitDisplay> fourDigitDisplays = new ArrayList<>();

        for (String line : lines) {
            String input = line.split("\\|")[0].trim();
            String output = line.split("\\|")[1].trim();

            fourDigitDisplays.add(
                    new FourDigitDisplay(split(input), split(output))
            );
        }

        int count = 0;
        for (FourDigitDisplay fourDigitDisplay : fourDigitDisplays) {
            for (Set<Character> output : fourDigitDisplay.outputs) {
                if (fourDigitDisplay.isOne(output) || fourDigitDisplay.isFour(output) || fourDigitDisplay.isSeven(output) || fourDigitDisplay.isEight(output)) {
                    count++;
                }
            }
        }

        return count;
    }

    private List<Set<Character>> split(String input) {
        return Arrays.stream(input.split(" ")).map(str -> {
            Set<Character> set = new HashSet<>();
            for (char c : str.toCharArray()) {
                set.add(c);
            }
            return set;
        }).collect(Collectors.toList());
    }

    private static class FourDigitDisplay {

        private final List<Set<Character>> inputs;
        private final List<Set<Character>> outputs;

        public FourDigitDisplay(List<Set<Character>> inputs, List<Set<Character>> outputs) {
            this.inputs = inputs;
            this.outputs = outputs;
        }

        public List<Set<Character>> getInputs() {
            return inputs;
        }

        public List<Set<Character>> getOutputs() {
            return outputs;
        }

        public boolean isOne(Set<Character> display) {
            return display.size() == 2;
        }

        public boolean isFour(Set<Character> display) {
            return display.size() == 4;
        }

        public boolean isSeven(Set<Character> display) {
            return display.size() == 3;
        }

        public boolean isEight(Set<Character> display) {
            return display.size() == 7;
        }
    }
}
