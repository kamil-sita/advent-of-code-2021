package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day16Part1 implements Solution {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        System.out.println(new Day16Part1().solve(readFile("day16.txt")));
    }

    @Override
    public Object solve(List<String> lines) {
        String hex = lines.get(0);
        String bin = hexToBin(hex);

        if (DEBUG) {
            System.out.println(hexExpand(hex));
            System.out.println(bin);
        }

        PacketAnalyzer packetAnalyzer = new PacketAnalyzer(bin);

        Context context = new Context();

        boolean readMore = true;

        while (readMore) {
            readMore = parsePacket(packetAnalyzer, context);
        }

        return context.sumOfHeaders;
    }

    private boolean parsePacket(PacketAnalyzer packetAnalyzer, Context context) {
        Header header = packetAnalyzer.readHeader();
        if (header == null) {
            return false;
        }
        if (DEBUG) {
            System.out.println("header version: " + header.getVersion());
            System.out.println("header type: " + header.getType());
        }
        context.sumOfHeaders += header.getVersion();
        if (header.isLiteralValue()) {
            StringBuilder sb = new StringBuilder();
            boolean hasMoreData = true;
            while (hasMoreData) {
                PartialValue partialValue = packetAnalyzer.readV();
                hasMoreData = partialValue.hasNext();
                sb.append(partialValue.getStrValue());
            }
        }
        if (header.isOperator()) {
            char typeId = packetAnalyzer.readLengthTypeId();
            if (typeId == '0') {
                int length = packetAnalyzer.readLengthInBitsOfSubpackets();
                int startedAt = packetAnalyzer.getPtr();
                while (startedAt + length > packetAnalyzer.getPtr()) {
                    parsePacket(packetAnalyzer, context);
                }
            } else {
                int number = packetAnalyzer.readNumberOfSubpackets();
                for (int i = 0; i < number; i++) {
                    parsePacket(packetAnalyzer, context);
                }
            }
        }
        return true;
    }

    private static class Context {
        int sumOfHeaders = 0;
    }

    private static class PacketAnalyzer {
        private int ptr = 0;
        private final String str;

        private PacketAnalyzer(String str) {
            this.str = str;
        }

        public Header readHeader() {
            if (DEBUG) {
                System.out.println("reading header");
            }
            String str = read(6);
            if (str == null) {
                return null;
            }
            return new Header(str);
        }

        public PartialValue readV() {
            if (DEBUG) {
                System.out.println("reading value");
            }
            return new PartialValue(read(5));
        }

        public char readLengthTypeId() {
            if (DEBUG) {
                System.out.println("reading typeID");
            }
            char c = str.charAt(ptr);
            ptr++;
            if (DEBUG) {
                System.out.println("typeID: " + c);
            }
            return c;
        }

        public int readLengthInBitsOfSubpackets() {
            if (DEBUG) {
                System.out.println("reading length of subpackets");
            }
            String str = read(15);
            return Integer.parseInt(str, 2);
        }

        public int getPtr() {
            return ptr;
        }

        public int readNumberOfSubpackets() {
            if (DEBUG) {
                System.out.println("reading number of subpackets");
            }
            String str = read(11);
            return Integer.parseInt(str, 2);
        }

        private String read(int i) {
            if (str.length() < ptr + i) {
                return null;
            }
            String read = str.substring(ptr, ptr + i);
            ptr += i;
            if (DEBUG) {
                System.out.println("v: " + read);
            }
            return read;
        }
    }

    private static class Header {
        private final String string;

        public Header(String string) {
            this.string = string;
        }

        public int getVersion() {
            return Integer.parseInt(string.substring(0, 3), 2);
        }

        public int getType() {
            return Integer.parseInt(string.substring(3, 6), 2);
        }

        public boolean isLiteralValue() {
            return getType() == 4;
        }
        
        public boolean isOperator() {
            return !isLiteralValue();
        }
    }

    private static class PartialValue {
        private final String string;

        public PartialValue(String string) {
            this.string = string;
        }

        public int getValue() {
            return Integer.parseInt(string.substring(1, 5), 2);
        }
        
        public String getStrValue() {
            return string.substring(1, 5);
        }

        public boolean hasNext() {
            return string.charAt(0) == '1';
        }
    }

    private String hexExpand(String hex){
        hex = hex.replaceAll("0", "0   ");
        hex = hex.replaceAll("1", "1   ");
        hex = hex.replaceAll("2", "2   ");
        hex = hex.replaceAll("3", "3   ");
        hex = hex.replaceAll("4", "4   ");
        hex = hex.replaceAll("5", "5   ");
        hex = hex.replaceAll("6", "6   ");
        hex = hex.replaceAll("7", "7   ");
        hex = hex.replaceAll("8", "8   ");
        hex = hex.replaceAll("9", "9   ");
        hex = hex.replaceAll("A", "A   ");
        hex = hex.replaceAll("B", "B   ");
        hex = hex.replaceAll("C", "C   ");
        hex = hex.replaceAll("D", "D   ");
        hex = hex.replaceAll("E", "E   ");
        hex = hex.replaceAll("F", "F   ");
        return hex;
    }

    private String hexToBin(String hex){
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }
}