package pl.ksitarski.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day3Part1().solve(Utils.readFile("day3.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        List<MostCommon> mostCommonList = new ArrayList<>();

        for (String line : lines) {
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char character = charArray[i];
                Integer integer = Integer.parseInt(character + "");
                if (mostCommonList.size() == i) {
                    mostCommonList.add(new MostCommon());
                }
                mostCommonList.get(i).add(integer);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (MostCommon mostCommon : mostCommonList) {
            sb.append(mostCommon.mostCommon());
        }
        int mostCommonAsDecimal = Integer.parseInt(sb.toString(), 2);

        StringBuilder sb2 = new StringBuilder();
        for (MostCommon mostCommon : mostCommonList) {
            sb2.append(mostCommon.leastCommon());
        }
        int leastCommonAsDecimal = Integer.parseInt(sb2.toString(), 2);

        return mostCommonAsDecimal * leastCommonAsDecimal;
    }

    public static class MostCommon {
        private final Map<Integer, Integer> mostCommon = new HashMap<>();

        public void add(Integer v) {
            if (mostCommon.containsKey(v)) {
                mostCommon.put(v, mostCommon.get(v) + 1);
            } else {
                mostCommon.put(v, 1);
            }
        }

        public Integer mostCommon() {
            Map.Entry<Integer, Integer> mostCommonSoFar = null;
            for (Map.Entry<Integer, Integer> entry : mostCommon.entrySet()) {
                if (mostCommonSoFar == null) {
                    mostCommonSoFar = entry;
                } else {
                    if (mostCommonSoFar.getValue() > entry.getValue()) {
                        mostCommonSoFar = entry;
                    }
                }
            }
            return mostCommonSoFar.getKey();
        }

        public Integer leastCommon() {
            Map.Entry<Integer, Integer> leastCommonSoFar = null;
            for (Map.Entry<Integer, Integer> entry : mostCommon.entrySet()) {
                if (leastCommonSoFar == null) {
                    leastCommonSoFar = entry;
                } else {
                    if (leastCommonSoFar.getValue() < entry.getValue()) {
                        leastCommonSoFar = entry;
                    }
                }
            }
            return leastCommonSoFar.getKey();
        }
    }
}
