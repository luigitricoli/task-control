package br.com.egs.task.control.web.model.exception;

public class AuthenticateException extends TaskControlWebClientException {

	private static final long serialVersionUID = 480738693712165447L;

	public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticateException(Throwable cause) {
        super(cause);
    }
    
}