package br.com.egs.task.control.core.exception;

/**
 * Indicates that a given operation cannot be completed on a late task.
 */
public class LateTaskException extends ValidationException {
    public LateTaskException(String message) {
        super(message);
    }
}
