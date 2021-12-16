package pl.ksitarski.adventofcode.aoc2021;

import pl.ksitarski.adventofcode.aoc2021.utils.Utils;

import java.util.List;

public class Day6Part2 implements Solution {
    private static final int BASE_TIME = 6;
    private static final int NEWBORN_TIME = 8;

    public static void main(String[] args) {
        System.out.println(new Day6Part2().solve(Utils.readFile("day6.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        Colony colony = new Colony();

        String s = lines.get(0);
        String[] agesString = s.split(",");

        for (String ageString : agesString) {
            colony.add(Integer.parseInt(ageString));
        }

        for (int i = 0; i < 256; i++) {
            colony.iterate();
        }

        return (int) colony.getCount();
    }

    private static class Colony {
        private long[] countByAge = new long[NEWBORN_TIME + 1];

        public void add(int age) {
            countByAge[age]++;
        }

        public void iterate() {
            long[] countByAgeNextIter = new long[NEWBORN_TIME + 1];
            for (int i = countByAge.length - 1; i >= 0; i--) {
                if (i != 0) {
                    countByAgeNextIter[i - 1] = countByAge[i];
                } else {
                    countByAgeNextIter[NEWBORN_TIME] = countByAge[0];
                    countByAgeNextIter[BASE_TIME] = countByAgeNextIter[BASE_TIME] + countByAge[0];
                }
            }
            countByAge = countByAgeNextIter;
        }

        public long getCount() {
            long sum = 0;
            for (int i = 0; i < countByAge.length; i++) {
                sum += countByAge[i];
            }
            return sum;
        }

    }

}
