package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day14Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day14Part1().solve(readFile("day14.txt")));
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

        for (int i = 0; i < 10; i++) {
            polymer.applyRules(expandingRules);
        }

        Map<Character, Integer> countMap = polymer.countMap();

        List<Map.Entry<Character, Integer>> countMapList = new ArrayList<>(countMap.entrySet());
        countMapList.sort(Comparator.comparingInt(Map.Entry::getValue));

        return countMapList.get(countMapList.size() - 1).getValue() - countMapList.get(0).getValue();
    }

    private static class Polymer {
        private String polymer;

        public Polymer(String polymer) {
            this.polymer = polymer;
        }

        public void applyRules(List<ExpandingRule> expandingRules) {
            System.out.println("before: " + polymer);
            outer: for (int i = 0; i < polymer.length() - 1; i++) {
                System.out.println(i);
                String s = "" + polymer.charAt(i) + polymer.charAt(i + 1);
                for (ExpandingRule expandingRule : expandingRules) {
                    if (expandingRule.matches(s)) {
                        System.out.println("rule matches for: " + s + ", replaces with: " + expandingRule.replace());
                        String repl = expandingRule.replace();
                        String sBefore = polymer.substring(0, i);
                        String sAfter = polymer.substring(i + 2);
                        polymer = sBefore + repl + sAfter;
                        i++;
                        continue outer;
                    }
                }
            }
            System.out.println("after: " + polymer);
        }

        public Map<Character, Integer> countMap() {
            Map<Character, Integer> map = new HashMap<>();
            for (Character c : polymer.toCharArray()) {
                Integer count = map.getOrDefault(c, 0);
                map.put(c, count + 1);
            }
            return map;
        }

        public long size() {
            return polymer.length();
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