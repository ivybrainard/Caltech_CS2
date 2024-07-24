package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;


import java.util.Iterator;


public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {


    private int size;


    private Node<K, V> head;



    public MoveToFrontDictionary() {
        this.size = 0;

        this.head = null;

        // student: TODO fill this in
    }


    private static class Node<K, V> {
        private K key;

        private V value;

        private Node<K, V> next;



        private Node(K key, V value, Node<K, V> back) {
            this.key = key;
            this.value = value;
            this.next = back;
        }


    }


    @Override
    public V remove(K key) {
        if(size == 0)  {
            return null;
        }

        V ele = get(key);

        if(ele == null) {
            return null;
        } else {
            this.head = this.head.next;
            size--;
        }

        return ele;

    }


    @Override
    public V put(K key, V value) {
        //if size == 0
        if(!containsKey(key)) {
            head = new Node<>(key, value, head);
            size++;
            return null;
        }
        V v = this.get(key);
        head.value = value;
        return v;

    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    @Override
    public int size() {

        return this.size;
    }

    @Override
    public ICollection<K> keys() {

        Node<K, V> curr = head;
        ArrayDeque<K> k = new ArrayDeque<>();
        while(curr != null) {
            k.add(curr.key);
            curr = curr.next;
        }

        return k;

    }

    @Override
    public ICollection<V> values() {
        Node<K, V> curr = this.head;
        ArrayDeque<V> v = new ArrayDeque<>();
        while(curr != null) {
            v.add(curr.value);
            curr = curr.next;
        }

        return v;

    }

    public V get(K key) {
        Node<K, V> curr = this.head;
        while(curr != null) {
            if(curr.key.equals(key)) {
                if(curr != head) {
                    Node<K, V> temp = head;
                    while(temp != null && temp.next != curr) {
                        temp = temp.next;
                    }
                    temp.next = curr.next;
                    curr.next = head;
                    head = curr;
                }
                return curr.value;
            }
            curr = curr.next;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new DictionaryIterator();
    }


    private class DictionaryIterator implements Iterator<K> {

        private Node<K, V> currentNode;
        private int idx;

        public DictionaryIterator() {
            this.currentNode = head;
            this.idx = 0;
        }

        public boolean hasNext() {
            return idx < size;
        }

        public K next() {
            if(!this.hasNext()) {
                return null;
            }
            K k = this.currentNode.key;
            currentNode = currentNode.next;
            idx++;
            return k;
        }


    }

    public String toString() {
        String result = "";
        Node<K, V> currentNode = this.head;
        while(currentNode != null ) {
            result += currentNode.key + ": " + currentNode.value + ", ";
            currentNode = currentNode.next;
        }

        result = result.substring(0, result.length() - 2);
        return result + "]";
    }


}
