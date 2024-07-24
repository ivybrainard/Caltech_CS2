package edu.caltech.cs2.datastructures;

import com.sun.net.httpserver.Filter;
import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;

    private static final int[] PRIMES = {2, 5, 11, 23, 47, 103, 211, 431, 863, 1733, 3467, 6947, 13901,
            27803, 55609, 111227, 222461, 444929, 500009};

    private int size;

    private Object[] buckets;

    private int maxSize;





    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        this.size = 0;
        this.chain = chain;
        this.maxSize = 0;
        this.buckets = new Object[PRIMES[maxSize]];
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int num = Math.floorMod(key.hashCode(), PRIMES[this.maxSize]);
        IDictionary<K, V> b = (IDictionary<K, V>) buckets[num];
        if(b == null) {
            return null;
        }

        return b.get(key);
    }

    @Override
    public V remove(K key) {
        int num = Math.floorMod(key.hashCode(), PRIMES[this.maxSize]);
        IDictionary<K, V> b = (IDictionary<K, V>)this.buckets[num];
        if(b == null) {
            return null;
        }
        if(!b.containsKey(key)) {
            return  null;
        }
        this.size--;
        return b.remove(key);
    }

    @Override
    public V put(K key, V value) {
        if(this.size >= PRIMES[this.maxSize]) {
            rehash();
        }
        int num = Math.floorMod(key.hashCode(), PRIMES[this.maxSize]);
        if (this.buckets[num] == null) {
            this.buckets[num] = chain.get();
        }
        IDictionary<K, V> b = (IDictionary<K, V>) this.buckets[num];
        V v = b.get(key);
        if (v == null) {
            this.size++;
        }
        b.put(key, value);
        return v;


    }



    private void rehash() {
        int num = PRIMES[++this.maxSize];
        Object[] newBuckets = new Object[num];
        for (Object object : this.buckets) {
            if (object != null) {
                IDictionary<K, V> b = (IDictionary<K, V>) object;
                for (K key : b.keys()) {
                    int index = Math.floorMod(key.hashCode(), PRIMES[this.maxSize]);
                    if (newBuckets[index] == null) {
                        newBuckets[index] = chain.get();
                    }
                    ((IDictionary<K, V>) newBuckets[index]).put(key, b.get(key));
                }
            }
            this.buckets = newBuckets;
        }



        //ahhhh
    }
    @Override
    public boolean containsKey(K key) {
        if(get(key) != null) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        for(Object object : buckets) {
            IDictionary<K, V> v = (IDictionary<K, V>) object;
            if(object != null) {
                if(v.containsValue(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> key = new ArrayDeque<>();
        for(Object object : buckets) {
            if(object != null) {
                IDictionary<K, V> k = (IDictionary<K, V>) object;
                for(K ke : k.keys()) {
                    key.add(ke);
                }
            }
        }
        return key;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> value = new ArrayDeque<>();
        for(Object object : buckets) {
            if(object != null) {
                IDictionary<K, V> v = (IDictionary<K, V>) object;
                for(V va : v.values()) {
                    value.add(va);
                }
            }
        }
        return value;
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }

        String result = "[";

        for(Object obj : buckets) {
            IDictionary<K, V> b = (IDictionary<K, V>) obj;
            for (K k : b.keys()) {
                result += b.keys() + "=" + b.get(k) + ", ";
            }
        }

        result = result.substring(0, result.length() - 2);
        return result + "]";

    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }

}
