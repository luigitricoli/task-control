package br.com.egs.task.control.core.exception;

import br.com.egs.task.control.core.utils.Messages;

/**
 * Indicates that a given operation cannot be completed on a late task.
 */
public class LateTaskException extends ValidationException {
    public LateTaskException() {
        super("A late task can only be finished if a justifying message exists",
                Messages.Keys.VALIDATION_TASK_LATE_TASK_WITH_NO_JUSTIFYING_POST);
    }
}
