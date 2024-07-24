package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private int size;
    private PriorityQueue<PQElement<E>> data;
    private HashMap<E,PQElement<E>> keyMap;

    public class PQComparator implements Comparator<PQElement<E>>
    {
        public int compare( PQElement<E> first, PQElement<E> second )
        {
            if (first.priority < second.priority){
                return -1;
            }
            else if (first.priority > second.priority){
                return 1;
            }
            else return 0;
        }
    }

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PriorityQueue<PQElement<E>>(new PQComparator());
        this.keyMap = new HashMap<>();

    }

    @Override
    public void increaseKey(PQElement<E> key) {
        if (!this.keyMap.containsKey(key.data)){
            throw new IllegalArgumentException();
        }
        if (key.priority < this.keyMap.get(key.data).priority) {
            throw new IllegalArgumentException();
        }
        else{
            this.data.remove(this.keyMap.get(key.data));
            this.data.add(key);
            this.keyMap.put(key.data, key);
        }
    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        if (!this.keyMap.containsKey(key.data)){
            throw new IllegalArgumentException();
        }
        if (key.priority > this.keyMap.get(key.data).priority) {
            throw new IllegalArgumentException();
        }
        else{
            this.data.remove(this.keyMap.get(key.data));
            this.data.add(key);
            this.keyMap.put(key.data, key);
        }
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if (this.keyMap.containsKey(epqElement.data)){
            throw new IllegalArgumentException();
        }
        this.data.add(epqElement);
        this.keyMap.put(epqElement.data, epqElement);
        this.size++;
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if (this.size == 0) return null;
        PQElement<E> retVal = this.data.poll();
        this.keyMap.remove(retVal.data);
        this.size--;
        return retVal;
    }

    @Override
    public PQElement<E> peek() {
        return this.data.peek();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return this.data.iterator();
    }
}
