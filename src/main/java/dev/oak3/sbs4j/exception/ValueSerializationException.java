package dev.oak3.sbs4j.exception;

/**
 * Thrown when type could not be serialized
 *
 * @since 0.1.0
 */
public class ValueSerializationException extends Exception {
    public ValueSerializationException(String message) {
        super(message);
    }

    public ValueSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
