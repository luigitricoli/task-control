package br.com.egs.task.control.core.exception;

import br.com.egs.task.control.core.utils.Messages;

/**
 * Data validation exception.
 */
public class ValidationException extends Exception {
    private Messages.Keys userMessageKey;
    private Object[] userMessageArgs;

    public ValidationException(String message, Messages.Keys userMessageKey) {
        super(message);
        this.userMessageKey = userMessageKey;
    }

    public ValidationException(String message, Messages.Keys userMessageKey, Object ... userMessageArgs) {
        super(message);
        this.userMessageKey = userMessageKey;
        this.userMessageArgs = userMessageArgs;
    }

    public Messages.Keys getUserMessageKey() {
        return userMessageKey;
    }

    public Object[] getUserMessageArgs() {
        return userMessageArgs;
    }
}
