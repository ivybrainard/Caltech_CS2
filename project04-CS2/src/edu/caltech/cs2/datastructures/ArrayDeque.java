package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

  private static final int DEFAULT_CAPACITY = 10;
  private static final int GROW_FACTOR = 2;
  private E[] data;
  private int size;


  public ArrayDeque() {
    this(DEFAULT_CAPACITY);
  }


  public ArrayDeque(int initialCapacity) {
    this.data = (E[])new Object[initialCapacity];
    this.size = 0;
  }


  private void ensureCapacity() {
    if (data.length <= size) {
      E[] newData = (E[])new Object[(int)(data.length*GROW_FACTOR)];
      for (int i = 0; i < this.size; i++) {
        newData[i] = this.data[i];
      }
      this.data = newData;
    }
  }


  @Override
  public void addFront(E e) {
    this.ensureCapacity();
    for (int i = this.size; i > 0; i--) {
      E element = data[i - 1];
      this.data[i] = element;
    }
    this.data[0] = e;
    this.size++;
  }

  @Override
  public void addBack(E e) {
    this.ensureCapacity();
    this.data[this.size] = e;
    this.size++;
  }

  @Override
  public E removeFront() {
    if (this.size == 0) {
      return null;
    }
    E eFirst = this.data[0];
    this.size--;
    for (int i = 0; i < this.size; i++) {
      E element = data[i + 1];
      this.data[i] = element;
    }
    return eFirst;
  }

  @Override
  public E removeBack() {
    if (this.size == 0) {
      return null;
    }
    size--;
    E eLast = this.data[this.size];
    return eLast;
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
    return this.data[0];
  }

  @Override
  public E peekBack() {
    if (this.size == 0) {
      return null;
    }
    return this.data[this.size - 1];
  }

  @Override
  public E peek() {
    if (this.size == 0) {
      return null;
    }
    return this.data[this.size - 1];
  }

  private class ArrayDequeIterator implements Iterator<E> {
    private int idx;
    public ArrayDequeIterator() {
      this.idx = 0;
    }

    @Override
    public boolean hasNext() {
      return this.idx < size();
    }

    @Override
    public E next() {
      E toReturn = data[this.idx];
      this.idx++;
      return toReturn;
    }
  }

  public String toString() {
    if (this.isEmpty()) {
      return "[]";
    }

    String result = "[";
    for (int i = 0; i < this.size; i++) {
      result += this.data[i] + ", ";
    }

    result = result.substring(0, result.length() - 2);
    return result + "]";
  }

  @Override
  public Iterator<E> iterator() {
    return new ArrayDequeIterator();
  }

  @Override
  public int size() {
    return this.size;
  }
}
