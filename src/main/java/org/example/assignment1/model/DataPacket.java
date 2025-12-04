package org.example.assignment1.model;

import lombok.NonNull;

/**
 * Represents the data being transferred to the common channel.
 */
public class DataPacket {
    private final int id;
    private final String content;

    public DataPacket(@NonNull final int id, final String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Packet{" + "id=" + id + ", content='" + content + '\'' + '}';
    }
}
