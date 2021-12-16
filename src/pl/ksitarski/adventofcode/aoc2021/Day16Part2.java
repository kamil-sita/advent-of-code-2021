package pl.ksitarski.adventofcode.aoc2021;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static pl.ksitarski.adventofcode.aoc2021.utils.Utils.readFile;


public class Day16Part2 implements Solution {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        System.out.println(new Day16Part2().solve(readFile("day16.txt")));
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

        return parsePacket(packetAnalyzer).longValueExact();
    }

    private BigInteger parsePacket(PacketAnalyzer packetAnalyzer) {
        Header header = packetAnalyzer.readHeader();
        if (DEBUG) {
            System.out.println("header version: " + header.getVersion());
            System.out.println("header type: " + header.getType());
        }
        if (header.isLiteralValue()) {
            StringBuilder sb = new StringBuilder();
            boolean hasMoreData = true;
            while (hasMoreData) {
                PartialValue partialValue = packetAnalyzer.readV();
                hasMoreData = partialValue.hasNext();
                sb.append(partialValue.getStrValue());
            }
            return new BigInteger(sb.toString(), 2);
        }
        if (header.isOperator()) {
            char typeId = packetAnalyzer.readLengthTypeId();
            List<BigInteger> values = new ArrayList<>();
            if (typeId == '0') {
                int length = packetAnalyzer.readLengthInBitsOfSubpackets();
                int startedAt = packetAnalyzer.getPtr();
                while (startedAt + length > packetAnalyzer.getPtr()) {
                    values.add(parsePacket(packetAnalyzer));
                }
            } else {
                int number = packetAnalyzer.readNumberOfSubpackets();
                for (int i = 0; i < number; i++) {
                    values.add(parsePacket(packetAnalyzer));
                }
            }
            return header.op(values);
        }
        throw new RuntimeException();
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
            return new Header(read(6));
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
                throw new AnalysisComplete();
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

        public BigInteger op(List<BigInteger> values) {
            if (!isOperator()) {
                throw new RuntimeException();
            }
            int type = getType();
            switch (type) {
                case 0 -> {
                    if (values.size() == 1) {
                        return values.get(0);
                    }
                    BigInteger sum = BigInteger.ZERO;
                    for (BigInteger val : values) {
                        sum = sum.add(val);
                    }
                    return sum;
                }
                case 1 -> {
                    if (values.size() == 1) {
                        return values.get(0);
                    }
                    BigInteger prod = BigInteger.ONE;
                    for (BigInteger val : values) {
                        prod = prod.multiply(val);
                    }
                    return prod;
                }
                case 2 -> {
                    if (values.size() == 1) {
                        return values.get(0);
                    }
                    BigInteger min = values.get(0);
                    for (BigInteger val : values) {
                        min = val.min(min);
                    }
                    return min;
                }
                case 3 -> {
                    if (values.size() == 1) {
                        return values.get(0);
                    }
                    BigInteger max = values.get(0);
                    for (BigInteger val : values) {
                        max = val.max(max);
                    }
                    return max;
                }
                case 5 -> {
                    return (values.get(0).compareTo(values.get(1))) > 0 ? BigInteger.ONE : BigInteger.ZERO;
                }
                case 6 -> {
                    return (values.get(0).compareTo(values.get(1))) < 0 ? BigInteger.ONE : BigInteger.ZERO;
                }
                case 7 -> {
                    return (values.get(0).compareTo(values.get(1))) == 0 ? BigInteger.ONE : BigInteger.ZERO;
                }
            }
            throw new RuntimeException();
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

    private static class AnalysisComplete extends RuntimeException { //top 10 fast haxs

    }
}