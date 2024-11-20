package dev.oak3.sbs4j.exception;

/**
 * Thrown when type could not be deserialized
 *
 * @since 0.1.0
 */
public class ValueDeserializationException extends Exception {
    public ValueDeserializationException(String message) {
        super(message);
    }

    public ValueDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
