package pl.ksitarski.adventofcode.aoc2021;

import java.util.*;

import static pl.ksitarski.adventofcode.aoc2021.Utils.readFile;


public class Day12Part2 implements Solution {

    public static void main(String[] args) {
        System.out.println(new Day12Part2().solve(readFile("day12.txt")));
    }

    @Override
    public long solve(List<String> lines) {
        NodeMap nodeMap = new NodeMap(new Node("start"), new Node("end"));

        for (String line : lines) {
            String[] nodeNames = line.split("-");
            Node node1 = nodeMap.getNodeByName(nodeNames[0]);
            Node node2 = nodeMap.getNodeByName(nodeNames[1]);
            nodeMap.addConnection(node1, node2);
        }

        List<Path> analyzedPaths = new ArrayList<>();
        List<Path> finishedValidPaths = new ArrayList<>();
        {
            Path initialPath = new Path(nodeMap);
            initialPath.addNode(nodeMap.getStartingNode());
            analyzedPaths.add(initialPath);
        }

        while (analyzedPaths.size() > 0) {
            System.out.println(":::::");
            Path path = analyzedPaths.get(0);
            System.out.println("analyzing path: " + path);

            if (!path.isValid()) {
                System.out.println("path was not valid");
                analyzedPaths.remove(path);
                continue;
            }

            if (path.hasFinished()) {
                System.out.println("-> path was valid and finished");
                finishedValidPaths.add(path);
                analyzedPaths.remove(path);
                continue;
            }

            List<Node> visitableNodes = nodeMap.connectedNodes(path.getNewestNode());

            analyzedPaths.remove(path);
            List<Path> newPaths = new ArrayList<>();

            for (Node visitableNode : visitableNodes) {
                Path newPath = path.copy();
                newPath.addNode(visitableNode);
                newPaths.add(newPath);
                System.out.println("adding path that visits node: " + visitableNode);
            }

            analyzedPaths.addAll(newPaths);
        }

        return finishedValidPaths.size();
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
    }

    private static class Path {
        private final List<Node> nodes = new ArrayList<>();
        private final NodeMap nodeMap;

        public Path(NodeMap nodeMap) {
            this.nodeMap = nodeMap;
        }

        public Path copy() {
            Path clone = new Path(nodeMap);
            for (Node node : nodes) {
                clone.addNode(node);
            }
            return clone;
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