package com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ionut.tirlea on 26/11/2017.
 */
public class Path implements Cloneable {

    private List<Node> path;
    private boolean isPrimePath = false;

    public Path(){
        path = new ArrayList<>();
    }

    public Path(Node startNode){
        path = new ArrayList<>();
        path.add(startNode);
    }

    public void addNode(Node node){
        path.add(node);
    }

    public void removeLastNode(){
        path.remove(path.size() - 1);
    }

    public int getLength(){
        return path.size() - 1;
    }

    public boolean contains(Node n){
        return path.contains(n);
    }
    public int indexOf(Node n){
        return path.indexOf(n);
    }

    public boolean isCycle(){
        if (path.size() <= 1)
            return false;
        return path.get(0).equals(path.get(path.size() - 1));
    }

    public boolean isComplete(){

        if(path.size() < 1) return false;

        if (isCycle())
            return true;

        boolean isComplete = path.get(path.size() - 1).getAdjacentNodes().isEmpty();

        if(!isComplete){
            for(Node n: path.get(path.size() - 1).getAdjacentNodes()){
                if(!path.contains(n) || n.equals(path.get(0)))
                    return false;
            }
        }

        return true;
    }

    public boolean isPrimePath(){

        return this.isPrimePath;

    }

    public void setIsPrimePath(boolean isPrimePath) {
        this.isPrimePath = isPrimePath;
    }

    @Override
    public String toString() {
        String s = "";
        for(Node n: path){
            s += s.isEmpty() ? n.getId() : "," + n.getId();
        }
        String special = "";
        if(isComplete()){
            special = "!";
        }
        if(isCycle()){
            special = "*";
        }
        return  "[" + s + "]" + special;
    }

    public Path clone(){
        Path clonedPath = new Path();
        for(Node n: path){
            clonedPath.path.add(n);
        }
        return clonedPath;
    }

    public boolean containsSubpath(Path subpath){
        return Collections.indexOfSubList(path , subpath.path) != -1;
    }
}
