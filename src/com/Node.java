package com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ionut.tirlea on 18/11/2017.
 */
public class Node {

    private int id;
    private List<Node> adjacentNodes;

    public Node(int id){
        this.setId(id);
        setAdjacentNodes(new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addAdjacentNode(Node v){
        getAdjacentNodes().add(v);
    }

    public List<Node> getAdjacentNodes(){
        return this.adjacentNodes;
    }

    public void setAdjacentNodes(List<Node> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    @Override
    public boolean equals (Object o) {
        Node node = (Node) o;
        if (node.getId() == this.getId()) return true;
        return false;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + getId() +
                ", adjacentNodes=" + getAdjacentNodesToString() +
                '}';
    }

    private String getAdjacentNodesToString(){
        String s = "";
        for(Node node:getAdjacentNodes()){
            if(!s.isEmpty()){
                s+="," + node.getId();
            }
            else{
                s+=node.getId();
            }
        }
        if(s.isEmpty()){
            return "none";
        }
        return s;
    }
}
