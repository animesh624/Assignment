package assignment1.queue;

import org.example.assignment1.model.DataPacket;
import org.example.assignment1.queue.MonitorBlockingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Blocking Queue implementation.
 * Focuses on FIFO order and Blocking behavior.
 */
class MonitorBlockingQueueTest {

    @Test
    void testQueueFIFOOrder() throws InterruptedException {
        MonitorBlockingQueue queue = new MonitorBlockingQueue(5);
        DataPacket p1 = new DataPacket(1, "First");
        DataPacket p2 = new DataPacket(2, "Second");

        queue.send(p1);
        queue.send(p2);

        assertEquals(p1, queue.receive(), "First in should be first out");
        assertEquals(p2, queue.receive(), "Second in should be second out");
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS) // Fail if deadlocked
    void testProducerBlocksWhenFull() throws InterruptedException {
        int capacity = 1;
        MonitorBlockingQueue queue = new MonitorBlockingQueue(capacity);

        // Fill the queue
        queue.send(new DataPacket(1, "Filler"));

        // Create a separate thread to try and add a second item (this should block)
        Thread producerThread = new Thread(() -> {
            try {
                queue.send(new DataPacket(2, "Blocked Item"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();

        // Give the thread a moment to run and hit the lock
        Thread.sleep(100);

        // Verify the thread is waiting (BLOCKED or WAITING)
        assertTrue(producerThread.getState() == Thread.State.WAITING ||
                        producerThread.getState() == Thread.State.BLOCKED,
                "Producer thread should be waiting because queue is full");

        // Now consume the item to free space
        queue.receive();

        // Join the thread (it should finish now that space is available)
        producerThread.join(1000);

        // Ensure the thread finished successfully
        assertEquals(Thread.State.TERMINATED, producerThread.getState());
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testConsumerBlocksWhenEmpty() throws InterruptedException {
        MonitorBlockingQueue queue = new MonitorBlockingQueue(1);

        // Create a consumer thread (queue is empty, so it should block)
        Thread consumerThread = new Thread(() -> {
            try {
                queue.receive();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumerThread.start();

        // Give the thread a moment to start and hit the wait()
        Thread.sleep(100);

        assertTrue(consumerThread.getState() == Thread.State.WAITING ||
                        consumerThread.getState() == Thread.State.BLOCKED,
                "Consumer should wait when queue is empty");

        // Produce an item to wake up consumer
        queue.send(new DataPacket(1, "Wake Up"));

        consumerThread.join(1000);

        assertEquals(Thread.State.TERMINATED, consumerThread.getState());
    }
}