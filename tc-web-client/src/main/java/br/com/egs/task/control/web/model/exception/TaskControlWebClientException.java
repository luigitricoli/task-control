package br.com.egs.task.control.web.model.exception;

public class TaskControlWebClientException extends Exception{

    private static final long serialVersionUID = -8813263354084773433L;

    public TaskControlWebClientException(String message) {
        super(message);
    }

    public TaskControlWebClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskControlWebClientException(Throwable cause) {
        super(cause);
    }
}
