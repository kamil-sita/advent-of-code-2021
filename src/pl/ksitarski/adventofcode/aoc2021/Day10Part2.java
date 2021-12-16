package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;


public class Day10Part2 implements Solution {

    private static final Set<Character> BEGINNING_CHARS = Set.of('(', '[', '{', '<');
    private static final Map<Character, Integer> CHAR_ADDING_SCORING = Map.of(
            ')', 1,
            ']', 2,
            '}', 3,
            '>', 4
    );
    private static final Map<Character, Character> CORRESPONDING_CHARACTERS = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>'
    );

    public static void main(String[] args) {
        System.out.println(new Day10Part2().solve(Utils.readFile("day10.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        List<String> correctLines = new ArrayList<>();
        for (String line : lines) {
            AnalysisLine analysisLine = new AnalysisLine(line);
            analysisLine.parse();
            if (!analysisLine.hasFailed()) {
                correctLines.add(line);
            }
        }

        List<AnalysisLine> analysisLines = new ArrayList<>();
        for (String line : correctLines) {
            AnalysisLine analysisLine = new AnalysisLine(line);
            analysisLine.fix();
            analysisLines.add(analysisLine);
        }

        analysisLines.sort(Comparator.comparingLong(AnalysisLine::getScore));

        int middleIndex = analysisLines.size() / 2; //always odd

        return analysisLines.get(middleIndex).getScore();
    }

    public static class AnalysisLine {
        private final Stack<Character> parsingStack = new Stack<>();
        private final String line;
        private Character failedOnCharacter = null;
        private long score;

        public AnalysisLine(String line) {
            this.line = line;
        }

        public void parse() {
            for (char c : line.toCharArray()) {
                if (BEGINNING_CHARS.contains(c)) {
                    parsingStack.add(c);
                } else {
                    char topmostChar = parsingStack.peek();
                    char correspondingChar = CORRESPONDING_CHARACTERS.get(topmostChar);
                    if (correspondingChar != c) {
                        failedOnCharacter = c;
                        return;
                    }
                    parsingStack.pop();
                }
            }
        }

        public void fix() {
            score = 0;
            for (char c : line.toCharArray()) {
                if (BEGINNING_CHARS.contains(c)) {
                    parsingStack.add(c);
                } else {
                    parsingStack.pop();
                }
            }
            while (!parsingStack.isEmpty()) {
                char charOnStack = parsingStack.pop();
                char correspondingChar = CORRESPONDING_CHARACTERS.get(charOnStack);
                score = score * 5 + CHAR_ADDING_SCORING.get(correspondingChar);
            }
        }

        public boolean hasFailed() {
            return failedOnCharacter != null;
        }

        public char failedOnChar() {
            return failedOnCharacter;
        }

        public long getScore() {
            return score;
        }
    }
}