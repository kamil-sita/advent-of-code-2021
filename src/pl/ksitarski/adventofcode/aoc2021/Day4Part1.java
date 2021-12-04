package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;
import java.util.stream.Collectors;

public class Day4Part1 implements Solution {
    private static final int BINGO_SIZE = 5;

    public static void main(String[] args) {
        System.out.println(new Day4Part1().solve(Utils.readFile("day4.txt")));
    }

    @Override
    public int solve(List<String> lines) {
        String orderUnprocessed = lines.get(0);

        List<Bingo> bingos = generateBingos(lines);

        String[] vals = orderUnprocessed.split(",");

        Utils.Pair<Bingo, Integer> winningBoardAndLastVal = findWinningBoardAndLastVal(bingos, vals);

        return winningBoardAndLastVal.getT().sumOfUnmarkedVals() * winningBoardAndLastVal.getU();
    }

    private Utils.Pair<Bingo, Integer> findWinningBoardAndLastVal(List<Bingo> bingos, String[] vals) {
        for (String valAsString : vals) {
            int val = Integer.parseInt(valAsString);

            for (Bingo bingo : bingos) {
                if (bingo.updateBoardHasWon(val)) {
                    return new Utils.Pair<>(bingo, val);
                }
            }
        }

        throw new RuntimeException();
    }

    private List<Bingo> generateBingos(List<String> lines) {
        List<Bingo> bingos = new ArrayList<>();

        int lineId = 2;
        int currentBoardRow = 0;
        int[][] board = createBoard();

        while (lineId < lines.size()) {
            String line = lines.get(lineId);
            String[] vals = line.split(" ");
            vals = Arrays.stream(vals).filter(string -> !string.isEmpty()).collect(Collectors.toList()).toArray(new String[BINGO_SIZE]);
            if (vals.length != BINGO_SIZE) {
                throw new RuntimeException();
            }
            for (int i = 0; i < vals.length; i++) {
                String val = vals[i];
                board[currentBoardRow][i] = Integer.parseInt(val);
            }

            currentBoardRow++;
            if (currentBoardRow == BINGO_SIZE) {
                bingos.add(new Bingo(board));

                currentBoardRow = 0;
                lineId++;
                board = createBoard();
            }

            lineId++;
        }
        return bingos;
    }

    private int[][] createBoard() {
        return new int[BINGO_SIZE][BINGO_SIZE];
    }

    public static class Bingo {
        private final Map<Integer, Utils.Pair<Integer, Integer>> positionsOfValues = new HashMap<>();
        private final boolean[][] boardMarked = new boolean[BINGO_SIZE][BINGO_SIZE];
        private final int[][] board;

        public Bingo(int[][] board) {
            for (int y = 0; y < BINGO_SIZE; y++) {
                for (int x = 0; x < BINGO_SIZE; x++) {
                    positionsOfValues.put(board[y][x], new Utils.Pair<>(x, y));
                }
            }
            this.board = board;
        }

        public boolean updateBoardHasWon(int val) {
            if (!positionsOfValues.containsKey(val)) {
                return false;
            }

            Utils.Pair<Integer, Integer> pos = positionsOfValues.get(val);
            boardMarked[pos.getU()][pos.getT()] = true;

            for (int row = 0; row < BINGO_SIZE; row++) {
                if (validateRow(row)) {
                    return true;
                }
            }
            for (int col = 0; col < BINGO_SIZE; col++) {
                if (validateCol(col)) {
                    return true;
                }
            }
            return false;
        }

        private boolean validateRow(int rowId) {
            for (int i = 0; i < BINGO_SIZE; i++) {
                if (!boardMarked[rowId][i]) {
                    return false;
                }
            }

            return true;
        }

        private boolean validateCol(int rowId) {
            for (int i = 0; i < BINGO_SIZE; i++) {
                if (!boardMarked[i][rowId]) {
                    return false;
                }
            }

            return true;
        }

        private int sumOfUnmarkedVals() {
            int sum = 0;
            for (int x = 0; x < BINGO_SIZE; x++) {
                for (int y = 0; y < BINGO_SIZE; y++) {
                    if (!boardMarked[y][x]) {
                        sum += board[y][x];
                    }
                }
            }
            return sum;
        }
    }
}
