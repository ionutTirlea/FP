package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ionut.tirlea on 18/11/2017.
 */
public class Graph {

    private String name;
    private List<Node> nodes;

    public Graph(String name){
        this.setName(name);
        this.setNodes(new ArrayList<>());
    }

    public void addNode(Node v){
        getNodes().add(v);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> vertices) {
        this.nodes = vertices;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "name='" + name + '\'' +
                ", nodes=\n" + getNodesToString() +
                '}';
    }

    private String getNodesToString(){
        String s = "";
        for(Node n: this.getNodes()){
            s += n.toString() + "\n";
        }
        return s;
    }

}
