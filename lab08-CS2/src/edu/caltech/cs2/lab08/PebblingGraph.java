package edu.caltech.cs2.lab08;

import java.util.*;
import java.util.stream.Collectors;

public class PebblingGraph {
    public final Set<PebblingNode> vertices;

    public static class PebblingNode {
        public final int id;
        public final Set<PebblingNode> neighbors;
        public int inDegree;

        public PebblingNode(int id) {
            this.id = id;
            this.neighbors = new HashSet<>();
            this.inDegree = 0;
        }

        public String toString() {
            return this.id + " -> {" + this.neighbors.stream().map(v -> "" + v.id).sorted().collect(Collectors.joining(", ")) + "}";
        }
    }

    public PebblingGraph() {
        this.vertices = new HashSet<>();
    }

    public PebblingNode addVertex(int id) {
        PebblingNode v = new PebblingNode(id);
        this.vertices.add(v);
        return v;
    }

    public void addEdge(PebblingNode fromVertex, PebblingNode toVertex) {
        if (!this.vertices.contains(fromVertex) || !this.vertices.contains(toVertex)) {
            throw new IllegalArgumentException("Vertices don't exist in graph");
        }

        fromVertex.neighbors.add(toVertex);
        toVertex.inDegree++;
    }

    public List<Integer> toposort() {
        ArrayList<PebblingNode> sort = new ArrayList<>();
        int[] deps = new int[vertices.size()];
        ArrayList<Integer> output = new ArrayList<>();

        for(PebblingNode v : vertices) {
            deps[v.id] = v.inDegree;
            if(deps[v.id] == 0) {
                sort.add(v);
            }
        }

        int idx = 0;


        for (int i = 0; i < sort.size(); i++) {
            //PebblingNode vi = vertices[idx];
            PebblingNode v = sort.get(i);
            output.add(v.id);
            for (PebblingNode w : v.neighbors) {
                deps[w.id] -= 1;
                if (deps[w.id] == 0) {
                    sort.add(w);
                }
            }
        }

        return output;
    }
}