package pl.ksitarski.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.List;

import static pl.ksitarski.adventofcode.aoc2021.utils.Utils.readFile;


public class Day17Part1 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day17Part1().solve(readFile("day17.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        Area area = getArea(lines);

        //constraints and thoughts:
        // 1. we're probably not interested in probes shot downwards... unless we won't find any solution before
        // 2. the thing that stops us from shooting very, very high is gravity
        // 3. but not really - actually we can fire a probe that falls very fast, and just happens to be in the pickup zone
        // 4. but the vertical speed must be lower than the distance from 0 to xEnd - this one is for sure
        // 5. and it must be high enough to reach the spot in the first place!

        int xMaxSpeed = area.xEnd;
        int xMinSpeed = calculateMinSpeed(area.xStart);

        //6. we'll be able to quickly filter horizontal speeds that don't fit into the pickup zone as well
        List<Integer> possibleHorizontalSpeeds = new ArrayList<>();

        for (int speed = xMinSpeed; speed <= xMaxSpeed; speed++) {
            if (isPossibleHorizontal(speed, area.xStart, area.xEnd)) {
                possibleHorizontalSpeeds.add(speed);
            }
        }

        //7. we can use a very similar logic to reason about vertical speed (counting from the landing zone and
        // then whether it's possible to shoot that kind of value from 0,0)... but we could use some upper bound for
        // the problem

        //8. and now something I've overlooked for 1.5 hr...
        // every (upward) probe will pass through x = 0 with the vertical speed equal to -(initial_speed + 1)

        int yMinSpeed = 0; //not true, but why bother with negatives?
        int yMaxSpeed = Math.abs(area.yEnd) - 1;

        int maxHeight = 0;

        for (int x = xMinSpeed; x <= xMaxSpeed; x++) {
            if (!possibleHorizontalSpeeds.contains(x)) {
                continue;
            }
            for (int y = yMinSpeed; y <= yMaxSpeed; y++) {


                Probe probe = new Probe(x, y);

                while (true) {
                    probe.step();
                    if (probe.inArea(area)) {
                        int localMaxHeight = probe.maxHeight;
                        if (localMaxHeight > maxHeight) {
                            maxHeight = localMaxHeight;
                            break;
                        }
                    }
                    if (probe.belowArea(area)) {
                        break;
                    }
                }


            }
        }

        return maxHeight;
    }

    private boolean isPossibleHorizontal(int speed, int xStart, int xEnd) {
        int posX = 0;
        while (true) {
            posX += speed;
            if (speed > 0) {
                speed--;
            }
            if (xStart <= posX && posX <= xEnd) {
                return true;
            }
            if (posX > xEnd) {
                return false;
            }
        }
    }

    private int calculateMinSpeed(int toReach) { //crude solution, mathematical one should be possible
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            int sum = 0;
            for (int j = i; j > 0; j--) {
                sum += j;
            }
            if (sum >= toReach) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    private Area getArea(List<String> lines) {
        String[] spl = lines.get(0).split(" ");
        String xCoord = spl[2].split(",")[0].split("=")[1];
        String yCoord = spl[3].split("=")[1];
        int xStart = Integer.parseInt(xCoord.split("\\.\\.")[0]);
        int xEnd = Integer.parseInt(xCoord.split("\\.\\.")[1]);
        int yStart = Integer.parseInt(yCoord.split("\\.\\.")[0]);
        int yEnd = Integer.parseInt(yCoord.split("\\.\\.")[1]);
        return new Area(xStart, xEnd, yStart, yEnd);
    }

    private static class Probe {
        private int xPos = 0;
        private int yPos = 0;
        private int xSpeed;
        private int ySpeed;
        private int maxHeight = 0;

        public Probe(int xSpeed, int ySpeed) {
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
        }

        public void step() {
            xPos += xSpeed;
            yPos += ySpeed;
            if (xSpeed > 0) {
                xSpeed--;
            } else if (xSpeed <0) {
                xSpeed++;
            }
            ySpeed--;
            if (yPos > maxHeight) {
                maxHeight = yPos;
            }
        }
        
        public boolean inArea(Area area) {
            return area.xStart <= xPos && area.xEnd >= xPos && area.yStart >= yPos && area.yEnd <= yPos;
        }

        public boolean belowArea(Area area) {
            return yPos < area.yEnd;
        }
    }

    private static class Area {
        private int xStart;
        private int xEnd;
        private int yStart;
        private int yEnd;

        public Area(int x1, int x2, int y1, int y2) {
            this.xStart = Math.min(x1, x2);
            this.xEnd = Math.max(x1, x2);
            this.yStart = Math.max(y1, y2);
            this.yEnd = Math.min(y1, y2);
        }
    }
}