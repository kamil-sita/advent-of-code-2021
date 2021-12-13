package pl.ksitarski.adventofcode.aoc2021;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day12Part2Optimized implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day12Part2Optimized().solve(readFile("day12.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        return solveInternal(lines);
    }

    private int solveInternal(List<String> lines) {
        NodeMap nodeMap = initMap(lines);

        Path initialPath = new Path(nodeMap);
        initialPath.addNode(nodeMap.getStartingNode());
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        Instant now = Instant.now();
        int i = forkJoinPool.invoke(initialPath);
        System.out.println(Duration.between(Instant.now(), now).getNano());
        return i;
    }

    private NodeMap initMap(List<String> lines) {
        NodeMap nodeMap = new NodeMap(new Node("start"), new Node("end"));

        for (String line : lines) {
            String[] nodeNames = line.split("-");
            Node node1 = nodeMap.getNodeByName(nodeNames[0]);
            Node node2 = nodeMap.getNodeByName(nodeNames[1]);
            nodeMap.addConnection(node1, node2);
        }
        return nodeMap;
    }

    private static class Node {
        private final String name;

        public Node(String name) {
            this.name = name;
        }

        public boolean canBeVisitedMultipleTimes() {
            return name.charAt(0) >= 'A' && name.charAt(0) <= 'Z';
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private static class Path extends RecursiveTask<Integer> {
        private final List<Node> nodes;
        private final NodeMap nodeMap;
        private final int uniqueNodesVisitedCount;

        public Path(NodeMap nodeMap) {
            this.nodeMap = nodeMap;
            nodes = new ArrayList<>(4);
            uniqueNodesVisitedCount = 0;
        }

        private Path(NodeMap nodeMap, List<Node> nodes, Node nextNode, int uniqueNodesVisitedCount) {
            this.nodeMap = nodeMap;
            this.nodes = new ArrayList<>(nodes.size() + 1);
            this.uniqueNodesVisitedCount = uniqueNodesVisitedCount;
            this.nodes.addAll(nodes);
            this.nodes.add(nextNode);
        }

        public void addNode(Node node) {
            nodes.add(node);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                sb.append(node);
                if (i + 1 != nodes.size()) {
                    sb.append(',');
                }
            }
            return sb.toString();
        }

        @Override
        protected Integer compute() {
            Node lastNode = nodes.get(nodes.size() - 1);
            if (nodeMap.endingNode == lastNode) {
                return 1;
            }

            List<Node> nextNodes = nodeMap.connectedNodes(lastNode);

            List<Path> tasks = new ArrayList<>();
            for (Node nextNode : nextNodes) {
                if (nextNode.equals(nodeMap.getStartingNode())) {
                    continue;
                }
                int localUniqueNodes = uniqueNodesVisitedCount;
                if (!nextNode.canBeVisitedMultipleTimes()) {
                    if (nodes.contains(nextNode)) {
                        localUniqueNodes++;
                    }
                }
                if (localUniqueNodes >= 2) {
                    continue;
                }

                Path path = new Path(nodeMap, nodes, nextNode, localUniqueNodes);
                path.fork();
                tasks.add(path);
            }

            int sum = 0;

            for (Path task : tasks) {
                sum += task.join();
            }

            return sum;
        }
    }

    private static class NodeMap {
        private final Node startingNode;
        private final Node endingNode;
        private final Map<Node, List<Node>> possiblePaths = new HashMap<>();
        private final Map<String, Node> nodesByName = new HashMap<>();
        private final Set<Node> nodes = new HashSet<>();

        public NodeMap(Node startingNode, Node endingNode) {
            this.startingNode = startingNode;
            this.endingNode = endingNode;
            updateIndexes(startingNode);
            updateIndexes(endingNode);
        }

        public void addConnection(Node a, Node b) {
            possiblePaths.putIfAbsent(a, new ArrayList<>());
            possiblePaths.putIfAbsent(b, new ArrayList<>());

            possiblePaths.get(a).add(b);
            possiblePaths.get(b).add(a);

            updateIndexes(a);
            updateIndexes(b);
        }

        private void updateIndexes(Node node) {
            nodesByName.put(node.getName(), node);
            nodes.add(node);
        }

        public Node getStartingNode() {
            return startingNode;
        }

        public Node getEndingNode() {
            return endingNode;
        }

        public Node getNodeByName(String name) {
            nodesByName.putIfAbsent(name, new Node(name));
            updateIndexes(nodesByName.get(name));
            return nodesByName.get(name);
        }

        public List<Node> connectedNodes(Node node) {
            return possiblePaths.get(node);
        }
    }
}