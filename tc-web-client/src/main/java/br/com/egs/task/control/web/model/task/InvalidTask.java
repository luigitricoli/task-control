package br.com.egs.task.control.web.model.task;

public class InvalidTask extends Exception{

    private static final long serialVersionUID = -1988100648095402168L;

    public InvalidTask(String message) {
        super(message);
    }

    public InvalidTask(String message, Throwable cause) {
        super(message, cause);
    }
}
