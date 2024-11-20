package dev.oak3.sbs4j.model;

import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
import dev.oak3.sbs4j.interfaces.SerializableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon implements SerializableObject, DeserializableObject {
    private List<Point> vertices;

    public List<Point> getVertices() {
        return vertices;
    }

    public void setVertices(List<Point> vertices) {
        this.vertices = vertices;
    }

    public Polygon() {
    }

    public Polygon(List<Point> vertices) {
        this.vertices = vertices;
    }

    @Override
    public void serialize(SerializerBuffer serializerBuffer) {
        serializerBuffer.writeI32(vertices.size());
        for (Point vertex : vertices) {
            vertex.serialize(serializerBuffer);
        }
    }

    @Override
    public void deserialize(DeserializerBuffer deserializerBuffer) throws ValueDeserializationException {
        int length = deserializerBuffer.readI32();
        this.vertices = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Point point = new Point();
            point.deserialize(deserializerBuffer);
            this.vertices.add(point);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polygon polygon = (Polygon) o;

        return Objects.equals(vertices, polygon.vertices);
    }

    @Override
    public int hashCode() {
        return vertices != null ? vertices.hashCode() : 0;
    }
}
