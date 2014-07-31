package br.com.egs.task.control.web.model.exception;

public class UpdateException extends TaskControlWebClientException {

	private static final long serialVersionUID = 2254341585953536915L;

	public UpdateException(String message) {
        super(message);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }
}
