package org.example.assignment1.worker;

import lombok.NonNull;
import org.example.assignment1.model.DataPacket;
import org.example.assignment1.queue.DataChannel;


/**
 * This is producer that will send packets to common data channel
 */
public class Producer implements Runnable {
    private final DataChannel channel;
    private final int itemsToProduce;

    public Producer(@NonNull final DataChannel channel, int itemsToProduce) {
        this.channel = channel;
        this.itemsToProduce = itemsToProduce;
    }



    /**
     * This will be called when we call thread
     * and it will try to consumer message from the common data channel
     */
    @Override
    public void run() {
        try {
            for (int i = 1; i <= itemsToProduce; i++) {
                DataPacket packet = new DataPacket(i, "Data-" + i);
                Thread.sleep(200); // Some delay added as to some operation is happening in between
                System.out.println("Producer attempting to send: " + packet);
                channel.send(packet);
            }
            System.out.println("Producer finished.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Producer interrupted.");
        }
    }
}
