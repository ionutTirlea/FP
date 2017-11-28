package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ionut.tirlea on 26/11/2017.
 */
public class GraphHelper implements IGraphHelper {

    private HashMap<Integer, List<Path>> pathHashMap;

    public boolean isSESEGraph(Graph graph) {

        int entries = 0;
        int exits = 0;

        for (Node node : graph.getNodes()) {

            boolean isExit = node.getAdjacentNodes().isEmpty();
            boolean isEntry = true;

            for (Node otherNode : graph.getNodes()) {
                if (otherNode.getAdjacentNodes().contains(node)) {
                    isEntry = false;
                }
            }

            if (isEntry)
                entries++;

            if (isExit)
                exits++;

            if (entries > 1 || exits > 1)
                return false;

        }

        return entries == 1 && exits == 1;

    }


    public Graph readGraphFromFile(File file) {
        return new GraphFileReader().readGraphFromFile(file);
    }

    public void displayPaths(Graph graph) {
        Scanner s = new Scanner(System.in);
        getSimplePaths(graph);
        int total = 0;
        System.out.println("\nCalculating simple paths:\nPress enter to continue to continue!");
        s.nextLine();
        for (Integer length : pathHashMap.keySet()) {
            System.out.println("Calculating all paths of length " + length + ":");
            for (Path p : pathHashMap.get(length)) {
                System.out.println(p.toString());
            }
            System.out.println("Press enter to continue to next step!");
            s.nextLine();
        }
        System.out.println("All paths are complete!");
        System.out.println("Press enter to continue to continue!");
        s.nextLine();
        for (Integer length : pathHashMap.keySet()) {
            System.out.println(pathHashMap.get(length).size() + "\tsimple paths of length " + length);
            total += pathHashMap.get(length).size();
        }
        System.out.println("\n" + total + "\t" + "total simple paths\n");
        System.out.println("Calculating prime paths:\nPress enter to continue to continue!");
        s.nextLine();
        calculatePrimePaths();
        pathHashMap.forEach(
                (Integer length, List<Path> paths) -> {
                    if (paths != null) {
                        paths.forEach(
                                path -> {
                                    if (path.isPrimePath()) {
                                        System.out.println(path.toString());
                                    }
                                }
                        );
                    }

                }
        );
        System.out.println();
        pathHashMap.forEach(
                (Integer length, List<Path> paths) -> {
                    if (paths != null) {
                        long totalPrimePaths = paths.stream().filter(path -> path.isPrimePath() == true).count();
                        if(totalPrimePaths > 0) System.out.println(totalPrimePaths + "\t" + " prime paths of length " + length);
                    }
                }
        );
        System.out.println("\n" + pathHashMap.values().stream().flatMap(paths -> paths.stream()).filter(path -> path.isPrimePath() == true).count() + "\t" + "total prime paths\n");
    }

    private void getSimplePaths(Graph graph) {
        pathHashMap = new HashMap<>();
        for (Node node : graph.getNodes()) {
            getPaths(node, new Path());
        }
    }

    private void calculatePrimePaths() {
        for (Integer length : pathHashMap.keySet()) {
            for (Path p : pathHashMap.get(length)) {
                if (!p.isComplete()) {
                    p.setIsPrimePath(false);
                } else {
                    if (p.isCycle()) {
                        p.setIsPrimePath(true);
                    } else {
                        p.setIsPrimePath(isPrimePath(p, length + 1));
                    }
                }
            }
        }
    }

    private boolean isPrimePath(Path p, int length) {
        if (!pathHashMap.keySet().contains(length)) {
            return true;
        }
        for (Path otherPath : pathHashMap.get(length)) {
            if (otherPath.containsSubpath(p))
                return false;
        }
        return isPrimePath(p, length + 1);
    }

    private void getPaths(Node node, Path path) {

        if (path.isCycle() || path.isComplete()) {
            return;
        }

        if (path.contains(node) && path.indexOf(node) != 0) {
            return;
        }

        path.addNode(node);

        List<Path> paths = pathHashMap.get(path.getLength());
        Path clonedPath = path.clone();
        if (paths != null) {
            paths.add(clonedPath);
        } else {
            paths = new ArrayList<>();
            paths.add(clonedPath);
        }

        pathHashMap.put(path.getLength(), paths);

        for (Node adjacentNode : node.getAdjacentNodes()) {
            getPaths(adjacentNode, path);
            if (path.getLength() > 0) path.removeLastNode();
        }

    }


    private class GraphFileReader {

        private Pattern pattern;
        private String validLineRegex = "\\([\\d]+,[\\d]+\\)";

        private Graph graph;

        public GraphFileReader() {
            pattern = Pattern.compile(validLineRegex);
        }

        public Graph readGraphFromFile(File file) {

            BufferedReader br = null;
            FileReader fr = null;

            String name = "";
            int pos = file.getName().lastIndexOf(".");
            if (pos != -1) {
                name = file.getName().substring(0, pos);
            }

            graph = new Graph(name);

            int lineNumber = 0;
            try {

                fr = new FileReader(file);
                br = new BufferedReader(fr);

                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    lineNumber++;
                    if (isValidLine(sCurrentLine)) {
                        Node node1 = getNodeFromLine(sCurrentLine, 1);
                        Node node2 = getNodeFromLine(sCurrentLine, 2);
                        if (node1 == null || node2 == null) {
                            System.out.println("Error: Parsing line <" + lineNumber + "> with content <" + sCurrentLine + "> failed!");
                            return null;
                        }
                        if (!graph.getNodes().contains(node1)) {
                            graph.addNode(node1);
                        }
                        if (!graph.getNodes().contains(node2)) {
                            graph.addNode(node2);
                        }
                        if (!graph.getNodes().get(graph.getNodes().indexOf(node1)).getAdjacentNodes().contains(node2)) {
                            graph.getNodes().get(graph.getNodes().indexOf(node1)).addAdjacentNode(node2);
                        }
                    } else {
                        System.out.println("Error: Line <" + lineNumber + "> with content <" + sCurrentLine + "> is not valid!");
                        return null;
                    }
                }
                /* ensure the adjacency list is correct */
                for (Node node : graph.getNodes()) {
                    for (Node otherNode : graph.getNodes()) {
                        if (!otherNode.equals(node) && otherNode.getAdjacentNodes().contains(node)) {
                            otherNode.getAdjacentNodes().get(otherNode.getAdjacentNodes().indexOf(node)).setAdjacentNodes(node.getAdjacentNodes());
                        }
                    }
                }

            } catch (IOException e) {

                e.printStackTrace();
                return null;

            } finally {

                try {

                    if (br != null)
                        br.close();

                    if (fr != null)
                        fr.close();

                } catch (IOException e) {

                    e.printStackTrace();
                    return null;

                }

            }
            return graph;
        }

        private Node getNodeFromLine(String line, int position) {
            if (isValidLine(line)) {
                Pattern numberPattern = Pattern.compile("[0-9]+");
                Matcher matcher = numberPattern.matcher(line);
                matcher.find();
                if (position == 1)
                    return new Node(Integer.parseInt(matcher.group()));
                else if (position == 2) {
                    matcher.find();
                    return new Node(Integer.parseInt(matcher.group()));
                } else
                    return null;
            }
            return null;
        }

        private boolean isValidLine(String line) {
            Matcher matcher = pattern.matcher(line);
            return matcher.matches();
        }
    }

}
