package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Utils;

import java.util.*;


public class Day10Part1 implements Solution {

    private static final Set<Character> BEGINNING_CHARS = Set.of('(', '[', '{', '<');
    private static final Map<Character, Integer> CHAR_MISMATCH_SCORING = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137
    );
    private static final Map<Character, Character> CORRESPONDING_CHARACTERS = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>'
    );

    public static void main(String[] args) {
        System.out.println(new Day10Part1().solve(Utils.readFile("day10.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        long syntaxErrorScore = 0;

        for (String line : lines) {
            AnalysisLine analysisLine = new AnalysisLine(line);
            analysisLine.parse();
            if (analysisLine.hasFailed()) {
                syntaxErrorScore += CHAR_MISMATCH_SCORING.get(analysisLine.failedOnChar());
            }
        }

        return syntaxErrorScore;
    }

    public static class AnalysisLine {
        private final Stack<Character> parsingStack = new Stack<>();
        private final String line;
        private Character failedOnCharacter = null;

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

        public boolean hasFailed() {
            return failedOnCharacter != null;
        }

        public char failedOnChar() {
            return failedOnCharacter;
        }

    }
}