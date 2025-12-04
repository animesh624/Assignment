package org.example.assignment1.queue;

import org.example.assignment1.model.DataPacket;

/**
 * This represents the common channel that is used by producer and consumer.
 */
public interface DataChannel {
    /**
     * This function will be used to send packet to the channel (used by producer)
     * @param packet
     * @throws InterruptedException
     */
    void send(DataPacket packet) throws InterruptedException;

    /**
     * This channel will be used by consumer to received packet from channel (used by consumer)
     * @return
     * @throws InterruptedException
     */
    DataPacket receive() throws InterruptedException;
}
