package pl.ksitarski.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day1Part2 implements Solution {

    private static final int WINDOW_SIZE = 3;

    public static void main(String[] args) {
        System.out.println(new Day1Part2().solve(Utils.readFile("day1.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        List<Integer> integers = lines.stream().map(Integer::parseInt).collect(Collectors.toList());;

        Window currentWindow = new Window();
        Window previousWindow = new Window();

        int biggerThanPrevious = 0;
        for (Integer current : integers) {
            currentWindow.add(current);
            if (currentWindow.isValid() && previousWindow.isValid()) {
                if (currentWindow.getSum() > previousWindow.getSum()) {
                    biggerThanPrevious++;
                }
            }
            previousWindow.add(current);
        }

        return biggerThanPrevious;
    }

    private static class Window {
        private final List<Integer> elements = new ArrayList<>(WINDOW_SIZE + 1);

        public void add(Integer element) {
            elements.add(element);
            if (elements.size() > WINDOW_SIZE) {
                elements.remove(0);
            }
        }

        public int getSum() {
            int sum = 0;
            for (Integer element : elements) {
                sum += element;
            }
            return sum;
        }

        public boolean isValid() {
            return elements.size() == WINDOW_SIZE;
        }

    }
}
