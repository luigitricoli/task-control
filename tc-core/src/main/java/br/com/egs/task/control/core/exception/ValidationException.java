package br.com.egs.task.control.core.exception;

import br.com.egs.task.control.core.utils.Messages;

/**
 * Data validation exception.
 */
public class ValidationException extends Exception {
    private Messages.Keys userMessageKey;

    public ValidationException(String message, Messages.Keys userMessageKey) {
        super(message);
        this.userMessageKey = userMessageKey;
    }

    public Messages.Keys getUserMessageKey() {
        return userMessageKey;
    }
}
