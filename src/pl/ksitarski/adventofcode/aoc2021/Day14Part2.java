package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day14Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day14Part2().solve(readFile("day14.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        String polymerStr = lines.get(0);
        Polymer polymer = new Polymer(polymerStr);

        List<ExpandingRule> expandingRules = new ArrayList<>();
        for (int i = 2; i < lines.size(); i++) {
            String[] lineSpl = lines.get(i).split(" -> ");
            expandingRules.add(ExpandingRule.create(lineSpl[0], lineSpl[1]));
        }

        for (int i = 0; i < 40; i++) {
            polymer.applyRules(expandingRules);
        }

        Map<Character, Long> countMap = polymer.countMap();

        List<Map.Entry<Character, Long>> countMapList = new ArrayList<>(countMap.entrySet());
        countMapList.sort(Comparator.comparingLong(Map.Entry::getValue));

        return countMapList.get(countMapList.size() - 1).getValue() - countMapList.get(0).getValue();
    }

    private static class Polymer {
        private Map<String, Long> connectionCount = new HashMap<>();
        private char lastChar;

        public Polymer(String polymer) {
            for (int i = 0; i < polymer.length() - 1; i++) {
                String key = "" + polymer.charAt(i) + polymer.charAt(i + 1);
                getAndIncrement(connectionCount, key, 1L);
            }
            lastChar = polymer.charAt(polymer.length() - 1);
        }

        public void applyRules(List<ExpandingRule> expandingRules) {
            Map<String, Long> newConnectionCount = new HashMap<>();

            for (Map.Entry<String, Long> entry : connectionCount.entrySet()) {
                String replacedBy = null;
                for (ExpandingRule expandingRule : expandingRules) {
                    if (expandingRule.matches(entry.getKey())) {
                        replacedBy = expandingRule.replace();
                        break;
                    }
                }

                if (replacedBy == null) {
                    getAndIncrement(newConnectionCount, entry.getKey(), entry.getValue());
                } else {
                    String replacedByFirstPart = replacedBy.substring(0, 2);
                    String replacedBySecondPart = replacedBy.substring(1, 3);
                    getAndIncrement(newConnectionCount, replacedByFirstPart, entry.getValue());
                    getAndIncrement(newConnectionCount, replacedBySecondPart, entry.getValue());
                }
            }

            connectionCount = newConnectionCount;
        }

        private <T> void getAndIncrement(Map<T, Long> newConnectionCount, T key, Long value) {
            Long count = newConnectionCount.getOrDefault(key, 0L);
            newConnectionCount.put(key, count + value);
        }

        public Map<Character, Long> countMap() {
            Map<Character, Long> map = new HashMap<>();
            for (Map.Entry<String, Long> entry : connectionCount.entrySet()) {
                Character s = entry.getKey().charAt(0);
                getAndIncrement(map, s, entry.getValue());
            }
            getAndIncrement(map, lastChar, 1L);
            return map;
        }
    }

    private static class ExpandingRule {
        private String takes;
        private String produces;

        public static ExpandingRule create(String takes, String produces) {
            ExpandingRule expandingRule = new ExpandingRule();
            expandingRule.takes = takes;
            expandingRule.produces = takes.charAt(0) + produces + takes.charAt(1);
            return expandingRule;
        }

        public boolean matches(String s) {
            return s.equals(takes);
        }

        public String replace() {
            return produces;
        }
    }
}