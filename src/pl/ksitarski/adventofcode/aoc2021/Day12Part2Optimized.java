package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day12Part2Optimized implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day12Part2Optimized().solve(readFile("day12.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return solveInternal(lines);
    }

    private int solveInternal(List<String> lines) {
        NodeMap nodeMap = initMap(lines);

        List<Path> analyzedPaths = new ArrayList<>();
        List<Path> finishedValidPaths = new ArrayList<>();
        {
            Path initialPath = new Path(nodeMap);
            initialPath.addNode(nodeMap.getStartingNode());
            analyzedPaths.add(initialPath);
        }

        pathsAnalyze(nodeMap, analyzedPaths, finishedValidPaths);

        return finishedValidPaths.size();
    }

    private void pathsAnalyze(NodeMap nodeMap, List<Path> analyzedPaths, List<Path> finishedValidPaths) {
        while (analyzedPaths.size() > 0) {
            Path path = analyzedPaths.get(0);

            if (endNow(analyzedPaths, finishedValidPaths, path)) continue;

            createNewPaths(nodeMap, analyzedPaths, path);
        }
    }

    private void createNewPaths(NodeMap nodeMap, List<Path> analyzedPaths, Path path) {
        List<Node> visitableNodes = nodeMap.connectedNodes(path.getNewestNode());

        if (visitableNodes.size() == 0) {
            analyzedPaths.remove(path);
        } else {
            for (int i = 1; i < visitableNodes.size(); i++) {
                Path newPath = path.copy();
                newPath.addNode(visitableNodes.get(i));
                analyzedPaths.add(newPath);
            }
            path.addNode(visitableNodes.get(0));
        }
    }

    private boolean endNow(List<Path> analyzedPaths, List<Path> finishedValidPaths, Path path) {
        if (!path.isValid()) {
            analyzedPaths.remove(path);
            return true;
        }

        if (path.hasFinished()) {
            finishedValidPaths.add(path);
            analyzedPaths.remove(path);
            return true;
        }
        return false;
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

    private static class Path {
        private final List<Node> nodes;
        private final NodeMap nodeMap;

        public Path(NodeMap nodeMap) {
            this.nodeMap = nodeMap;
            nodes = new ArrayList<>(4);
        }

        private Path(NodeMap nodeMap, List<Node> nodes) {
            this.nodeMap = nodeMap;
            this.nodes = new ArrayList<>(nodes.size() + 2);
            this.nodes.addAll(nodes);
        }

        public Path copy() {
            return new Path(nodeMap, nodes);
        }

        public void addNode(Node node) {
            nodes.add(node);
        }

        public boolean isValid() {
            int uniqueNodesVisitedCount = 0;
            Set<Node> uniqueNodes = new HashSet<>();
            for (Node node : nodes) {
                if (!node.canBeVisitedMultipleTimes()) {
                    if (uniqueNodes.contains(node)) {
                        if (node.equals(nodeMap.getStartingNode()) || node.equals(nodeMap.getEndingNode())) {
                            return false;
                        }
                        uniqueNodesVisitedCount++;
                        if (uniqueNodesVisitedCount >= 2) {
                            return false;
                        }
                    } else {
                        uniqueNodes.add(node);
                    }
                }
            }
            return true;
        }

        public boolean hasFinished() {
            return nodeMap.isEndingNode(getNewestNode());
        }

        public Node getNewestNode() {
            return nodes.get(nodes.size() - 1);
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

        public boolean isEndingNode(Node node) {
            return node == getEndingNode();
        }

        public List<Node> connectedNodes(Node node) {
            return possiblePaths.get(node);
        }
    }
}