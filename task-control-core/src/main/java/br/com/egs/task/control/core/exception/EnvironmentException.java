package br.com.egs.task.control.core.exception;

/**
 * Used to represent errors in the execution environment, such as: missing files,
 * unsuccessful connections, etc.
 *
 */
public class EnvironmentException extends RuntimeException {
	private static final long serialVersionUID = -23878731644800373L;

	public EnvironmentException(String message, Throwable cause) {
		super(message, cause);
	}

	public EnvironmentException(String message) {
		super(message);
	}

}
