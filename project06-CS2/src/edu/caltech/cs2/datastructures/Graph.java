package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.ISet;


public class Graph<V, E> implements IGraph<V, E> {


    private int size;

    private ChainingHashDictionary<V, ChainingHashDictionary<V, E>> map;


    private ISet<V> listV;






    public Graph() {
        size = 0;
        listV = new ChainingHashSet<>();
        map = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }


    /**
     * Add a vertex to the graph.
     * @param vertex The vertex to add
     * @return true if vertex was not present already.
     */


    @Override
    public boolean addVertex(V vertex) {
        if(listV.contains(vertex)) {
            return false;
        }
        listV.add(vertex);
        map.put(vertex, new ChainingHashDictionary<>(MoveToFrontDictionary::new));
        return true;
    }


    /**
     * Adds a directed edge e to the graph.
     *
     * @param e The edge to add.
     * @throws //IllegalArgumentException
     *             If src and dest are not valid vertices (eg. refers to vertices not in the graph).
     * @return true if an edge from src to dest did not exist previously.
     *         false if an edge from src to dest did exist previously, in which case the edge value is updated
     */

    @Override
    public boolean addEdge(V src, V dest, E e) {
//
        if(!map.containsKey(src) || !map.containsKey(dest)) {
            throw new IllegalArgumentException(("Wrong"));
        }
        if(map.get(src).containsKey(dest)) {
            map.get(src).put(dest, e);
            return false;
        }

        map.get(src).put(dest, e);
        return true;
        //return false;
    }

    /**
     * Adds edge e to the graph in both directionns.
     *
     * @param e The edge to add.
     * @throws IllegalArgumentException
     *    If v1 and v2 are not valid vertices (eg. refers to vertices not in the graph).
     * @return true if an edge between src and dest did not exist in either direction.
     *         false if an edge between src and dest existed in either direction, in which case the edge value is
     *         updated.
     */

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {
        if(!map.containsKey(n1) || !map.containsKey(n2)) {
            throw new IllegalArgumentException(("Wrong"));
        }
        boolean check = addEdge(n1, n2, e);
        boolean check2 = addEdge(n2, n1, e);


        return check && check2;
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        if(!map.containsKey(src) || !map.containsKey(dest)) {
            //return false;
            throw new IllegalArgumentException(("Wrong"));
        }
        if(map.get(src).remove(dest) != null) {
            return true;
        }

        return false;
    }

    @Override
    public ISet<V> vertices() {
        return listV;
    }


    /**
     * Tests if vertices i and j are adjacent, returning the edge between
     * them if so.
     *
     * @throws IllegalArgumentException if i or j are not vertices in the graph.
     * @return The edge from i to j if it exists in the graph;
     * 		   null otherwise.
     */

    @Override
    public E adjacent(V i, V j) {
        if(!map.containsKey(i) || !map.containsKey(j)) {
            return null;
        }
        E edge = map.get(i).get(j);
        return edge;
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        ISet<V> neighbors = new ChainingHashSet<>();
        if(!map.containsKey(vertex)) {
            //return false;
            throw new IllegalArgumentException(("Wrong"));
        }

        for(V vertices : map.get(vertex)) {
            neighbors.add(vertices);
        }
        return neighbors;
    }
}