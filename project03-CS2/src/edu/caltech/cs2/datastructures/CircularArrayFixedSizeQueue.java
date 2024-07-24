package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {

  private E[] data;
  private int size;
  private int head;
  public CircularArrayFixedSizeQueue(int initialCapacity) {
    this.data = (E[]) new Object[initialCapacity];
    this.size = 0;
    this.head = 0;
  }


  @Override
  public boolean isFull() {
    if(capacity() == this.size) {
      return true;
    }
    return false;
  }

  @Override
  public int capacity() {
    return this.data.length;
  }

  @Override
  public boolean enqueue(E e) {
    if(isFull()) {
      return false;
    }
    if(size != 0) {
      this.head-=1;
      if (this.head < 0) {
        head += capacity();
      }
    } else {
        this.head = this.capacity() - 1;
    }

    size+=1;
    this.data[head] = e;
    return true;
  }

  private int previous() {
    int previous = this.head + this.size - 1;
    if (previous >= this.capacity()) {
      previous -= capacity();
    }
    return previous;
  }





  @Override
  public E dequeue() {
    if(this.size == 0) {
      return null;
    }
    E elemenet = this.data[previous()];
    this.size -= 1;
    return elemenet;
  }

  @Override
  public E peek() {
    if(this.size == 0) {
      return null;
    }
    E peek = this.data[previous()];
    return peek;
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public void add(E e) {
    enqueue(e);
  }

  @Override
  public void clear() {
    this.size = 0;
    this.head = 0;

  }

  @Override
  public Iterator<E> iterator() {
    return new CircularArrayIterator();
  }

  private class CircularArrayIterator implements Iterator<E> {
    private int idx;
    private int num;
    public CircularArrayIterator() {
      this.num = previous();
      this.idx = 0;
    }
    public boolean hasNext() {

      return idx < size;
    }

    public E next() {
      if (this.num < 0){
        this.num = capacity() - 1;
      }
      E toReturn = data[this.num];
      num -= 1;
      this.idx++;
      return toReturn;


    }


  }

  public String toString() {
    if (this.size == 0) {
      return "[]";
    }
    int idx = previous();

    String result = "[";
    for (int i = 0; i < this.size; i++) {
      result += this.data[idx] + ", ";
      idx--;
    }

    result = result.substring(0, result.length() - 2);
    return result + "]";
  }



}

