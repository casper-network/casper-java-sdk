package dev.oak3.sbs4j.model;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
import dev.oak3.sbs4j.interfaces.SerializableObject;

import java.util.Objects;

public class Sphere implements SerializableObject, DeserializableObject {
    private Point center;
    private float radius;

    public Sphere() {
    }

    public Sphere(Point center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void serialize(SerializerBuffer serializerBuffer) {
        // The written order...
        this.center.serialize(serializerBuffer);
        serializerBuffer.writeF32(this.radius);
    }

    @Override
    public void deserialize(DeserializerBuffer deserializerBuffer) throws ValueDeserializationException {
        // ...defines the read order
        this.center = new Point();
        this.center.deserialize(deserializerBuffer);
        this.radius = deserializerBuffer.readF32();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sphere sphere = (Sphere) o;

        if (Float.compare(sphere.radius, radius) != 0) return false;
        return Objects.equals(center, sphere.center);
    }

    @Override
    public int hashCode() {
        int result = center != null ? center.hashCode() : 0;
        result = 31 * result + (radius != 0.0f ? Float.floatToIntBits(radius) : 0);
        return result;
    }
}