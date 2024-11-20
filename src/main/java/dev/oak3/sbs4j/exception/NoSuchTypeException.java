package dev.oak3.sbs4j.exception;

/**
 * Thrown in case of a type which does not exist being requested
 *
 * @since 0.1.0
 */
public class NoSuchTypeException extends Exception {
    public NoSuchTypeException(String message) {
        super(message);
    }
}
