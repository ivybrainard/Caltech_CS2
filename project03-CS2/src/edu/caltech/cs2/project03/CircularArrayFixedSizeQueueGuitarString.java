package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {

    private static final double SAMPLING_RATE = 44100.0;
    private static final double ENERGY_DECAY_FACTOR = 0.996;

    private static Random random = new Random();

    private IFixedSizeQueue<Double> sample;



    public CircularArrayFixedSizeQueueGuitarString(double frequency) {
        int size = (int) Math.ceil(SAMPLING_RATE / frequency);
        this.sample = new CircularArrayFixedSizeQueue<>(size);
        for (int i = 0; i < size; i++) {
            sample.enqueue(0.0);
        }

    }

    public int length() {
        return this.sample.size();
    }

    public void pluck() {
        for (int i = 0; i < this.length(); i++) {
            sample.dequeue();
            sample.enqueue(random.nextDouble() - 0.5);
        }

    }

    public void tic() {
        double first = sample.dequeue();
        double second = sample.peek();
        double tic = ((first + second) / 2) * ENERGY_DECAY_FACTOR;
        sample.enqueue(tic);

    }

    public double sample() {
        return this.sample.peek();
    }
}
