package br.com.egs.task.control.web.model.exception;

public class InvalidDateException extends TaskControlWebClientException {

    private static final long serialVersionUID = 7965763983812500617L;

    public InvalidDateException(String msg) {
        super(msg);
    }

    public InvalidDateException(String date, String pattern) {
        super(String.format("The value [%s] of date argument is invalid to the format [%s]", date, pattern));
    }

    public InvalidDateException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
