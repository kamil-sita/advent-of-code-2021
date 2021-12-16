package pl.ksitarski.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day3Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day3Part2().solve(Utils.readFile("day3.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        String oxygen = findTheSearchedLine(lines, true);
        String co2 = findTheSearchedLine(lines, false);

        int oxygenAsDecimal = Integer.parseInt(oxygen, 2);
        int co2AsDecimal = Integer.parseInt(co2, 2);

        return oxygenAsDecimal * co2AsDecimal;
    }

    public static String findTheSearchedLine(List<String> lines, boolean isSearchingForMostCommon) {
        int lineLength = lines.get(0).length();
        List<String> linesCopy = new ArrayList<>(lines);

        for (int i = 0; i < lineLength; i++) {
            if (linesCopy.size() == 1) {
                return linesCopy.get(0);
            }

            int balance = 0;

            for (String line : linesCopy) {
                char character = line.charAt(i);
                if (character == '0') {
                    balance--;
                } else if (character == '1') {
                    balance++;
                } else {
                    throw new RuntimeException();
                }
            }

            char searchingForChar = (balance >= 0) ^ isSearchingForMostCommon ? '1' : '0';

            final int finalI = i;

            linesCopy = linesCopy.stream()
                    .filter(s -> s.charAt(finalI) == searchingForChar)
                    .collect(Collectors.toList());
        }
        if (linesCopy.size() != 1) {
            throw new RuntimeException();
        }
        return linesCopy.get(0);
    }
}
