package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Day6Part1 implements Solution {
    private static final int BASE_TIME = 6;
    private static final int NEWBORN_TIME = 8;

    public static void main(String[] args) {
        System.out.println(new Day6Part1().solve(Utils.readFile("day6.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        Colony colony = new Colony();

        String s = lines.get(0);
        String[] agesString = s.split(",");

        for (String ageString : agesString) {
            colony.add(Integer.parseInt(ageString));
        }

        for (int i = 0; i < 80; i++) {
            colony.iterate();
        }

        return colony.getCount();
    }

    private static class Colony {
        private final List<Integer> ages = new ArrayList<>();

        public void add(int age) {
            ages.add(age);
        }

        public void iterate() {
            int initialCount = ages.size();
            for (int i = 0; i < initialCount; i++) {
                int age = ages.get(i);
                age--;
                if (age < 0) {
                    ages.add(NEWBORN_TIME);
                    age = BASE_TIME;
                }
                ages.set(i, age);
            }
        }

        public int getCount() {
            return ages.size();
        }

    }

}
