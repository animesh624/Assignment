package org.example.assignment1;

import org.example.assignment1.queue.DataChannel;
import org.example.assignment1.queue.MonitorBlockingQueue;
import org.example.assignment1.worker.Consumer;
import org.example.assignment1.worker.Producer;

public class ConcurrencyApp {

    public static void main(String[] args) {
        // 1. Setup shared resources
        final DataChannel sharedChannel = new MonitorBlockingQueue(2);

        // 2. Define Items to produce/consume
        int workLoad = 10;

        // 3. Create workers i.e the producer and consumer threads
        Thread producerThread = new Thread(new Producer(sharedChannel, workLoad), "Producer-Thread");
        Thread consumerThread = new Thread(new Consumer(sharedChannel, workLoad), "Consumer-Thread");

        // 4. Start concurrent execution
        System.out.println("Starting Producer-Consumer Demo...");
        producerThread.start();
        consumerThread.start();
    }
}
