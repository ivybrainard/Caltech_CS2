package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROW_FACTOR = 2;

    private int size;
    private E[] data;




    public ArrayDeque() {
        this(DEFAULT_CAPACITY);
    }


    public ArrayDeque(int initialCapacity) {
        this.data = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    private void ensureCapacity(){
        if (this.size == this.data.length){
            E[] newData = (E[])new Object[(int)(this.data.length*GROW_FACTOR)];
            for (int i = 0; i < this.size; i++){
                newData[i] = this.data[i];
            }
            this.data = newData;
        }
    }

    // add consuctor
    @Override
    public void addFront(E e) {
        ensureCapacity();
        for (int i = this.size; i > 0; i--){
            this.data[i] = this.data[i - 1];
        }
        data[0] = e;
        size+= 1;

    }

    @Override
    public void addBack(E e) {
        ensureCapacity();
        this.data[size] = e;
        size += 1;
    }

    @Override
    public E removeFront() {
        if(this.size == 0) {
            return null;
        }
        this.size -= 1;
        E removedFrontElement = this.data[0];
        for(int i = 0; i < size; i++) {
            data[i] = data[i+1];
        }
        return removedFrontElement;
    }

    @Override
    public E removeBack() {
        if(this.size == 0) {
            return null;
        }
        size -= 1;
        E removedBackElement = this.data[this.size];
        return removedBackElement;

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
        if(this.size == 0) {
            return null;
        }
        return this.data[0];
    }

    @Override
    public E peekBack() {
        if(this.size == 0) {
            return null;
        }
        return this.data[size - 1];
    }

    @Override
    public E peek() {
        E peeks = peekBack();
        return peeks;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArraySetIterator();
    }

    private class ArraySetIterator implements Iterator<E> {

        private int idx;
        public ArraySetIterator() {
            this.idx = 0;
        }

        @Override
        public boolean hasNext() {
            return this.idx < size;
        }

        @Override
        public E next() {
            E toReturn = data[this.idx];
            this.idx++;
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
        for (int i = 0; i < this.size; i++) {
            result += this.data[i] + ", ";
        }

        result = result.substring(0, result.length() - 2);
        return result + "]";
    }





}