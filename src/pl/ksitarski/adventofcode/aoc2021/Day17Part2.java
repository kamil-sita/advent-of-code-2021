package pl.ksitarski.adventofcode.aoc2021;

import java.util.ArrayList;
import java.util.List;

import static pl.ksitarski.adventofcode.aoc2021.utils.Utils.readFile;


public class Day17Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day17Part2().solve(readFile("day17.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        Area area = getArea(lines);

        int xMaxSpeed = area.xEnd;
        int xMinSpeed = calculateMinSpeed(area.xStart);

        List<Integer> possibleHorizontalSpeeds = new ArrayList<>();

        for (int speed = xMinSpeed; speed <= xMaxSpeed; speed++) {
            if (isPossibleHorizontal(speed, area.xStart, area.xEnd)) {
                possibleHorizontalSpeeds.add(speed);
            }
        }

        int yMinSpeed = -Math.abs(area.yEnd);
        int yMaxSpeed = Math.abs(area.yEnd) - 1;

        int inArea = 0;

        for (int x = xMinSpeed; x <= xMaxSpeed; x++) {
            if (!possibleHorizontalSpeeds.contains(x)) {
                continue;
            }
            for (int y = yMinSpeed; y <= yMaxSpeed; y++) {


                Probe probe = new Probe(x, y);

                while (true) {
                    probe.step();
                    if (probe.inArea(area)) {
                        inArea++;
                        break;
                    }
                    if (probe.belowArea(area)) {
                        break;
                    }
                }


            }
        }

        return inArea;
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