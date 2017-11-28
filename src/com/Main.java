package com;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        System.out.print("\nEnter input graph file path: ");
        String filename = s.next();
        File file = new File(filename);

        IGraphHelper graphHelper = new GraphHelper();

        while (!file.isFile() || !file.canRead()) {
            System.out.println("File <" + file.getAbsolutePath() + "> can not be read. The filename, directory name, or volume label syntax may be incorrect or the file does not have read rights!");
            System.out.print("Enter input graph file path: ");
            filename = s.next();
            file = new File(filename);
        }

        Graph graph = graphHelper.readGraphFromFile(file);
        if (graph != null) {
            System.out.println(graph.toString());
        }
        if (graphHelper.isSESEGraph(graph)) {
            System.out.println("The read graph is a SESE graph!");
            graphHelper.displayPaths(graph);
        } else {
            System.out.println("The read graph is NOT a SESE graph!");
        }

        System.out.println("Close console to exit!");
        while(s.hasNext()){

        }

    }
}
