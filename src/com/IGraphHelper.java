package com;

import java.io.File;

/**
 * Created by ionut.tirlea on 26/11/2017.
 */
public interface IGraphHelper {
    boolean isSESEGraph(Graph g);
    Graph readGraphFromFile(File f);
    void displayPaths(Graph graph);
}
