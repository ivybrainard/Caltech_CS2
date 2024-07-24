package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

// //
public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

  private Node<E> head;
  private Node<E> tail;
  private int size;

  private static class Node<E> {
    public final E data;
    public Node<E> next;
    public Node<E> previous;

    public Node(E data, Node<E> front, Node<E> back) {
      this.data = data;
      this.next = front;
      this.previous = back;
    }
    public Node(E data) {
      this(data, null, null);
    }
  }


  public LinkedDeque() {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  @Override
  public void addFront(E e) {
    Node<E> front = new Node<>(e);
    if(this.size == 0) {
      this.head = front;
      this.tail = front;
    } else {
      front.next = this.head;
      this.head.previous = front;
      this.head = front;
    }
    size++;
  }

  @Override
  public void addBack(E e) {
    Node<E> back = new Node<>(e);
    if(this.size == 0) {
      this.head = back;
      this.tail = back;
    } else {
      back.previous = this.tail;
      this.tail.next = back;
      this.tail = back;
    }
    size++;
  }

  @Override
  public E removeFront() {
    if (this.size == 0) {
      return null;
    }
    if (this.head == null) {
      return null;
    }
    size--;
    if (this.head == this.tail) {
      E element = this.head.data;
      this.head = null;
      this.tail = null;
      return element;
    }
    E element = this.head.data;
    this.head = this.head.next;
    this.head.previous = null;
    return element;
  }

  @Override
  public E removeBack() {
    if (this.size == 0) {
      return null;
    }
    if (this.tail == null) {
      return null;
    }
    size--;
    if (this.tail == this.head) {
      E element = this.tail.data;
      this.tail = null;
      this.head = null;
      return element;
    }
    E element = this.tail.data;
    this.tail = this.tail.previous;
    this.tail.next = null;
    return element;
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
    return this.head.data;
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
    if (this.size == 0) {
      return null;
    }
    return this.tail.data;
  }

  @Override
  public Iterator<E> iterator() {
    return new LinkedDequeIterator();
  }


  private class LinkedDequeIterator implements Iterator<E> {
    private int idx;
    private LinkedDeque.Node<E> iterator;
    public LinkedDequeIterator() {
      this.idx = 0;
      this.iterator = head;
    }


    public boolean hasNext() {
      return this.idx < size;
    }


    public E next() {
      E toReturn = this.iterator.data;
      this.iterator = this.iterator.next;
      this.idx++;
      return toReturn;
    }
  }


  public String toString() {
    if (this.size == 0) {
      return "[]";
    }

    String result = "[";
    Node<E> iterator = head;
    while (iterator != null) {
      result += iterator.data + ", ";
      iterator = iterator.next;
    }

    result = result.substring(0, result.length() - 2);
    return result + "]";
  }

  @Override
  public int size() {
    return this.size;
  }
}
