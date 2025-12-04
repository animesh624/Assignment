package org.example.assignment1.worker;

import lombok.NonNull;
import org.example.assignment1.model.DataPacket;
import org.example.assignment1.queue.DataChannel;


/**
 * This is consumer that will take packets from common data channel
 */
public class Consumer implements Runnable {
    private final DataChannel channel;
    private final int itemsToConsume;

    public Consumer(@NonNull final DataChannel channel, int itemsToConsume) {
        this.channel = channel;
        this.itemsToConsume = itemsToConsume;
    }


    /**
     * This will be called when we call thread
     * and it will try to consumer message from the common data channel
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < itemsToConsume; i++) {
                DataPacket packet = channel.receive();
                process(packet);
            }
            System.out.println("Consumer finished.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Consumer interrupted.");
        }
    }

    /**
     * This is just a code to have intuition that
     * consumer thread is having some logic like I/O task that is taking time
     * @param packet
     * @throws InterruptedException
     */
    private void process(final DataPacket packet) throws InterruptedException {
        Thread.sleep(500);
        System.out.println("Consumer Processed: " + packet + "\n");
    }
}
