package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;
import java.util.stream.Collectors;

public class Day8Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day8Part2().solve(Utils.readFile("day8.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        List<FourDigitDisplay> fourDigitDisplays = new ArrayList<>();

        int sum = 0;

        for (String line : lines) {
            String input = line.split("\\|")[0].trim();
            String output = line.split("\\|")[1].trim();

            fourDigitDisplays.add(
                    new FourDigitDisplay(split(input), split(output))
            );
        }

        for (int i = 0; i < fourDigitDisplays.size(); i++) {
            FourDigitDisplay fourDigitDisplay = fourDigitDisplays.get(i);
            boolean anyRuleImplemented = false;
            do {
                anyRuleImplemented = false;
                for (Rule rule : rules) {
                    anyRuleImplemented |= rule.tryDeduceNewFact(fourDigitDisplay);
                }
                if (fourDigitDisplay.canDeduceAllOutputs()) {
                    break;
                }
            } while (anyRuleImplemented);

            if (!fourDigitDisplay.canDeduceAllOutputs()) {
                throw new RuntimeException();
            } else {
                sum += fourDigitDisplay.deduceDisplay();
            }
        }

        return sum;
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

    private interface Rule {
        //returns true if a NEW fact was deduced
        boolean tryDeduceNewFact(FourDigitDisplay fourDigitDisplay);
    }

    private final List<Rule> rules = List.of(
            //1
            fdd -> {
                if (fdd.segmentsMakingUpKnown(1)) {
                    return false;
                }

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (fdd.isOne(segments)) {
                        fdd.segmentsMakingUpAre(1, segments);
                        return true;
                    }
                }
                return false;
            },
            //4
            fdd -> {
                if (fdd.segmentsMakingUpKnown(4)) {
                    return false;
                }

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (fdd.isFour(segments)) {
                        fdd.segmentsMakingUpAre(4, segments);
                        return true;
                    }
                }
                return false;
            },
            //7
            fdd -> {
                if (fdd.segmentsMakingUpKnown(7)) {
                    return false;
                }

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (fdd.isSeven(segments)) {
                        fdd.segmentsMakingUpAre(7, segments);
                        return true;
                    }
                }
                return false;
            },
            //8
            fdd -> {
                if (fdd.segmentsMakingUpKnown(8)) {
                    return false;
                }
                fdd.segmentsMakingUpAre(8, Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g'));
                return true;
            },
            //3
            fdd -> {
                if (fdd.segmentsMakingUpKnown(3)) {
                    return false;
                }
                if (! (fdd.segmentsMakingUpKnown(1) || fdd.segmentsMakingUpKnown(7))) {
                    return false;
                }

                Set<Character> segmentsThatMakeUpOneOrSeven;
                if (fdd.segmentsMakingUpKnown(1)) {
                    segmentsThatMakeUpOneOrSeven = fdd.segmentsMakingUp(1);
                } else {
                    segmentsThatMakeUpOneOrSeven = fdd.segmentsMakingUp(7);
                }

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (segments.size() == 5) {
                        if (segments.containsAll(segmentsThatMakeUpOneOrSeven)) {
                            fdd.segmentsMakingUpAre(3, segments);
                            return true;
                        }
                    }
                }
                return false;
            },
            //9
            fdd -> {
                if (fdd.segmentsMakingUpKnown(9)) {
                    return false;
                }
                if (! (fdd.segmentsMakingUpKnown(4))) {
                    return false;
                }

                Set<Character> segmentsThatMakeUpFour = fdd.segmentsMakingUp(4);

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (segments.size() == 6) {
                        if (segments.containsAll(segmentsThatMakeUpFour)) {
                            fdd.segmentsMakingUpAre(9, segments);
                            return true;
                        }
                    }
                }
                return false;
            },
            //2
            fdd -> {
                if (fdd.segmentsMakingUpKnown(2)) {
                    return false;
                }
                if (! (fdd.segmentsMakingUpKnown(9))) {
                    return false;
                }

                Set<Character> segmentsThatMakeUpNine = fdd.segmentsMakingUp(9);

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (segments.size() == 5) {
                        if (!segmentsThatMakeUpNine.containsAll(segments)) {
                            fdd.segmentsMakingUpAre(2, segments);
                            return true;
                        }
                    }
                }
                return false;
            },
            //5
            fdd -> {
                if (fdd.segmentsMakingUpKnown(5)) {
                    return false;
                }
                if (! (fdd.segmentsMakingUpKnown(2) && fdd.segmentsMakingUpKnown(3))) {
                    return false;
                }

                Set<Character> segmentsThatMakeUpTwo = fdd.segmentsMakingUp(2);
                Set<Character> segmentsThatMakeUpThree = fdd.segmentsMakingUp(3);

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (segments.size() == 5) {
                        if (!segments.containsAll(segmentsThatMakeUpTwo) && !segments.containsAll(segmentsThatMakeUpThree)) {
                            fdd.segmentsMakingUpAre(5, segments);
                            return true;
                        }
                    }
                }
                return false;
            },
            //6
            fdd -> {
                if (fdd.segmentsMakingUpKnown(6)) {
                    return false;
                }
                if (! (fdd.segmentsMakingUpKnown(5) || ! (fdd.segmentsMakingUpKnown(9)))) {
                    return false;
                }

                Set<Character> segmentsThatMakeUpFive = fdd.segmentsMakingUp(5);
                Set<Character> segmentsThatMakeUpNine = fdd.segmentsMakingUp(9);

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (segments.size() == 6) {
                        if (segments.containsAll(segmentsThatMakeUpFive)) {
                            if (!segments.containsAll(segmentsThatMakeUpNine)) {
                                fdd.segmentsMakingUpAre(6, segments);
                                return true;
                            }
                        }
                    }
                }
                return false;
            },
            //0
            fdd -> {
                if (fdd.segmentsMakingUpKnown(0)) {
                    return false;
                }
                if (! (fdd.segmentsMakingUpKnown(5))) {
                    return false;
                }

                Set<Character> segmentsThatMakeUpFive = fdd.segmentsMakingUp(5);

                for (Set<Character> segments : fdd.getInputsAndOutputs()) {
                    if (segments.size() == 6) {
                        if (!segments.containsAll(segmentsThatMakeUpFive)) {
                            fdd.segmentsMakingUpAre(0, segments);
                            return true;
                        }
                    }
                }
                return false;
            }
    );

    private static class FourDigitDisplay {

        private final List<Set<Character>> inputs;
        private final List<Set<Character>> outputs;
        private final List<Set<Character>> inputsAndOutputs;
        private final Map<Integer, Set<Character>> segmentsMakingUpNumber = new HashMap<>();

        public FourDigitDisplay(List<Set<Character>> inputs, List<Set<Character>> outputs) {
            this.inputs = inputs;
            this.outputs = outputs;
            this.inputsAndOutputs = new ArrayList<>();
            inputsAndOutputs.addAll(inputs);
            inputsAndOutputs.addAll(outputs);
        }

        public List<Set<Character>> getInputs() {
            return inputs;
        }

        public List<Set<Character>> getOutputs() {
            return outputs;
        }

        public List<Set<Character>> getInputsAndOutputs() {
            return inputsAndOutputs;
        }

        public boolean segmentsMakingUpKnown(int number) {
            return segmentsMakingUpNumber.containsKey(number);
        }

        public void segmentsMakingUpAre(int number, Set<Character> characters) {
            segmentsMakingUpNumber.put(number, characters);
        }

        public Set<Character> segmentsMakingUp(int number) {
            return segmentsMakingUpNumber.get(number);
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

        public boolean canDeduceWhatNumber(Set<Character> segments) {
            for (Set<Character> segmentsMakingUpNumbera : segmentsMakingUpNumber.values()) {
                if (segments.equals(segmentsMakingUpNumbera)) {
                    return true;
                }
            }
            return false;
        }

        public int deduceNumber(Set<Character> segments) {
            for (Integer number : segmentsMakingUpNumber.keySet()) {
                if (segmentsMakingUpNumber.get(number).equals(segments)) {
                    return number;
                }
            }
            throw new RuntimeException();
        }

        public boolean canDeduceAllOutputs() {
            for (Set<Character> segments : outputs) {
                if (!canDeduceWhatNumber(segments)) {
                    return false;
                }
            }
            return true;
        }

        public int deduceDisplay() {
            return 1000 * deduceNumber(outputs.get(0)) + 100 * deduceNumber(outputs.get(1)) + 10 * deduceNumber(outputs.get(2)) + deduceNumber(outputs.get(3));
        }
    }
}
