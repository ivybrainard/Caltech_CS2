package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;


import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;


    public LinkedDeque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private static class Node<E> {
        public final E data;
        public Node<E> next;
        public Node<E> before;

        public Node(E data, Node<E> front, Node<E> back) {
            this.data = data;
            this.next = front;
            this.before = back;
        }
        public Node(E data) {
            this(data, null, null);
        }
    }

    @Override
    public void addFront(E e) {
        Node<E> node = new Node<>(e);
        if(size!= 0) {
            this.head.before = node;
            node.next = this.head;
            this.head = node;
        } else {
            this.head = node;
            this.tail = node;
        }
        size+=1;

    }

    @Override
    public void addBack(E e) {
        Node<E> node = new Node<>(e);
        if(this.size != 0) {
            this.tail.next = node;
            node.before = this.tail;
            this.tail = node;
        } else {
            this.head = node;
            this.tail = node;
        }
        size += 1;


    }

    @Override
    public E removeFront() {
        if(size == 0) {
            return null;
        }
        if(this.head == null) {
            return null;
        }
        size -= 1;
        E data = this.head.data;
        if(head != this.tail) {
            head = head.next;
            head.before = null;
        } else {
            this.head = null;
            this.tail = null;
        }

        return data;
    }

    @Override
    public E removeBack() {
        if(size == 0) {
            return null;
        }
        if(this.tail == null) {
            return null;
        }
        size -=1;
        E data = this.tail.data;
        if(tail != this.head) {
            tail = tail.before;
            tail.next = null;
        } else {
            this.tail = null;
            this.head = null;
        }
        return data;
    }

    @Override
    public boolean enqueue(E e) {
        addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        return removeBack();
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        return removeBack();
    }

    @Override
    public E peekFront() {
        if (this.size == 0) {
            return null;
        }
        return head.data;
    }

    @Override
    public E peekBack() {
        if (this.size == 0) {
            return null;
        }
        return this.tail.data;
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() {
        return new linkedDequeIterator();
    }

    private class linkedDequeIterator implements Iterator<E> {

        private LinkedDeque.Node<E> currentNode;
        private int idx;

        public linkedDequeIterator() {
            this.currentNode = head;
            this.idx = 0;
        }

        public boolean hasNext() {
            return idx < size;
        }

        public E next() {
            E toReturn = this.currentNode.data;
            this.currentNode = this.currentNode.next;
            this.idx += 1;
            return toReturn;
        }


    }

    @Override
    public int size() {
        return this.size;
    }


    public String toString() {

        if (this.size == 0) {
            return "[]";
        }

        String result = "[";
        Node<E> currentNode = this.head;
        while(currentNode != null) {
            result += currentNode.data + ", ";
            currentNode = currentNode.next;
        }

        result = result.substring(0, result.length() - 2);
        return result + "]";
    }



}

