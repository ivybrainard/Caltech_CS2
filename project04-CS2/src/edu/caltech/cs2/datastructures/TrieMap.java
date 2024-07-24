package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Iterator;
//
public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;


    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = null;
        this.collector = collector;
        this.size = 0;
    }


    @Override
    public boolean isPrefix(K key) {
        TrieNode<A, V> curr = helper(key);
        if(curr == null) {
            return false;
        }
        return true;
    }

    private void comHelper(TrieNode<A, V> node, IDeque<V> values) {
        if(node == null) {
            return;
        }
        if (node.value != null) {
            values.addBack(node.value);
        }
        for (TrieNode<A, V> child : node.pointers.values()) {
            comHelper(child, values);
        }

    }

    @Override
    public ICollection<V> getCompletions(K prefix) {
        IDeque<V> com = new LinkedDeque<>();
        TrieNode<A, V> curr = this.root;
        for(A letter : prefix) {
            if(curr == null) {
                return com;
            }
            if(!curr.pointers.containsKey(letter)) {
                return com;
            }
            curr = curr.pointers.get(letter);
        }
        comHelper(curr, com);
        return com;


    }



    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public V get(K key) {
        if(this.size == 0) {
            return null;
        }
        TrieNode<A, V> curr = helper(key);
        if(curr == null) {
            return null;
        }
        return curr.value;
    }

    private TrieNode<A, V> removeHelper(Iterator<A> iterator, TrieNode<A, V> node) {
        if (node == null) {
            return null;
        }
        if (iterator.hasNext()) {
            A letter = iterator.next();
            TrieNode<A, V> next = removeHelper(iterator, node.pointers.get(letter));
            //base case
            if (next == null) {
                node.pointers.remove(letter);
            } else {
                node.pointers.put(letter, next);
            }

            if (node.pointers.isEmpty()) {
                if(node.value == null) {
                    return null;
                }
            }
        } else {
            if (node.pointers.isEmpty()) {
                return null;
            }
            node.value = null;
        }
        return node;

    }

    @Override
    public V remove(K key) {
        if(!this.containsKey(key)) {
            return null;
        }
        V element = this.get(key);
        this.root = removeHelper(key.iterator(), this.root);
        if (this.root != null) {
            this.size--;
        } else {
            this.size = 0;
        }

        return element;
    }



    private TrieNode<A, V> helper(K key) {
        TrieNode<A, V> curr = this.root;

        if(this.root == null) {
            return null;
        }

        for (A letter : key) {
            if(curr.pointers.containsKey(letter)) {
                curr = curr.pointers.get(letter);
            } else {
                return null;

            }
        }
        return curr;
    }



    public V put(K key, V value) {
        TrieNode<A, V> curr = this.root;
        if(curr == null) {
            this.root = new TrieNode<>();
            curr = this.root;
        }
        for(A letter : key) {
            if(!curr.pointers.containsKey(letter)) {
                curr.pointers.put(letter, new TrieNode<>());
            }
            curr = curr.pointers.get(letter);

        }

        V ele = curr.value;
        if(curr.value == null) {
            size++;
        }
        curr.value = value;
        return ele;
    }

    @Override
    public boolean containsKey(K key) {
        TrieNode<A, V> curr = helper(key);

        if(curr == null) {
            return false;
        }
        if (curr.value == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean containsValue(V value) {

        return this.values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    private void keysHelper(TrieNode<A, V> node, IDeque<A> way, ICollection<K> keys) {
        if(node == null) {
            return;
        }
        if(node.value != null) {
            keys.add(this.collector.apply(way));
        }
        for(Map.Entry<A, TrieNode<A, V>> e : node.pointers.entrySet()) {
            way.addBack(e.getKey());
            keysHelper(e.getValue(), way, keys);
            way.removeBack();
        }
    }


    @Override
    public ICollection<K> keys() {
        TrieNode<A, V> curr = this.root;
        ICollection<K> keys = new LinkedDeque<>();
        IDeque<A> deque = new LinkedDeque<>();
        keysHelper(curr, deque, keys);
        return keys;
    }



    private void valuesHelper(TrieNode<A, V> node, ICollection<V> values) {
        //check
        if(node == null) {
            return;
        }
        if(node.value != null) {
            values.add(node.value);
        }
        for(TrieNode<A, V> children : node.pointers.values()) {
            valuesHelper(children, values);
        }
    }

    @Override
    public ICollection<V> values() {
        //check
        //recursion - make a new method
        //private
        ICollection<V> values = new LinkedDeque<>();
        valuesHelper(this.root, values);
        return values;
    }







    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }

    @Override
    public String toString() {
        return this.root.toString();
    }

    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}
