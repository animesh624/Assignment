package org.example.assignment1.queue;

import lombok.NonNull;
import org.example.assignment1.model.DataPacket;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A custom implementation of a blocking queue using Monitors.
 * Uses synchronized, wait(), and notifyAll().
 */
public class MonitorBlockingQueue implements DataChannel {
    private final Queue<DataPacket> queue;
    private final int capacity;
    private final Object lock = new Object(); // Explicit lock object

    public MonitorBlockingQueue(@NonNull final int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    /**
     * This function will be used to send packet to the channel (used by producer)
     * @param packet
     * @throws InterruptedException
     */

    @Override
    public void send(@NonNull final DataPacket packet) throws InterruptedException {
        synchronized (lock) {
            // While queue is full, producer must wait
            while (queue.size() == capacity) {
                System.out.println("[Queue] Full. Producer waiting...");
                lock.wait();
            }

            queue.add(packet);
            System.out.println("[Queue] Added: " + packet);

            // Notify consumers that data is available
            lock.notifyAll();
        }
    }

    /**
     * This function will be used to consumer packet from the channel (used by consumer)
     * @return
     * @throws InterruptedException
     */

    @Override
    public DataPacket receive() throws InterruptedException {
        synchronized (lock) {
            // While queue is empty, consumer must wait
            while (queue.isEmpty()) {
                System.out.println("[Queue] Empty. Consumer waiting...");
                lock.wait();
            }

            DataPacket packet = queue.poll();
            System.out.println("[Queue] Removed: " + packet);

            // Notify producers that space is available
            lock.notifyAll();

            return packet;
        }
    }
}
