package dev.oak3.sbs4j.model;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
import dev.oak3.sbs4j.interfaces.SerializableObject;

public class Point implements SerializableObject, DeserializableObject {
    private int x;
    private int y;
    private int z;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Point() {
    }

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void serialize(SerializerBuffer serializer) {
        // The written order...
        serializer.writeI32(x);
        serializer.writeI32(y);
        serializer.writeI32(z);
    }

    @Override
    public void deserialize(DeserializerBuffer deserializer) throws ValueDeserializationException {
        // ...defines the read order
        this.x = deserializer.readI32();
        this.y = deserializer.readI32();
        this.z = deserializer.readI32();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        if (y != point.y) return false;
        return z == point.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}