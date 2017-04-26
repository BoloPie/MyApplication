package com.example.popbar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/3/14.
 */

public class GroupsResponse implements Serializable {


    List<Nodes> nodes;

    public List<Nodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodes> nodes) {
        this.nodes = nodes;
    }

    public static class Nodes {
        private int id=0;
        private String name="";

        List<Nodes> nodes = new ArrayList<>();


        public List<Nodes> getNodes() {
            return nodes;
        }

        public void setNodes(List<Nodes> nodes) {
            this.nodes = nodes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
