package br.com.egs.task.control.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 */
public class Messages {
    private static final Logger logger = LoggerFactory.getLogger(Messages.class);

    private ResourceBundle bundle;

    public Messages() {
        bundle = ResourceBundle.getBundle("messages");
    }

    public String get(Keys key, Object... messageArguments) {
        String message;
        try {
            message = bundle.getString(key.name());
        } catch (MissingResourceException e) {
            logger.warn("Message key not found: " + key, e);
            return "??" + key + "??";
        }

        if (messageArguments != null && messageArguments.length > 0) {
            message = MessageFormat.format(message, messageArguments);
        }

        return message;
    }

    public static enum Keys {
        PARAMETER_USER_DATE_PATTERN,

        PARAMETER_TASK_LATE_POST_EXPRESSION,
        PARAMETER_TASK_WORKED_HOURS_POST_EXPRESSION,
        PARAMETER_TASK_WORKED_HOURS_POST_EXPRESSION_WITH_DATE,

        VALIDATION_GENERAL_REQUEST_BODY_CANNOT_BE_NULL,
        VALIDATION_GENERAL_MALFORMED_REQUEST,
        VALIDATION_GENERAL_MALFORMED_REQUEST_ARG,

        VALIDATION_LOGIN_MISSING_USERNAME,
        VALIDATION_LOGIN_MISSING_PASSWORD,
        VALIDATION_LOGIN_INVALID_CREDENTIALS,

        VALIDATION_USER_UNKNOWN_LOGIN,
        VALIDATION_USER_ALREADY_EXISTS,
        VALIDATION_USER_LOGIN_REQUIRED,
        VALIDATION_USER_NAME_REQUIRED,
        VALIDATION_USER_EMAIL_REQUIRED,
        VALIDATION_USER_TYPE_REQUIRED,
        VALIDATION_USER_PASSWORD_NOT_SET,
        VALIDATION_USER_APPLICATIONS_REQUIRED,
        VALIDATION_USER_APPLICATION_NAME_REQUIRED,

        VALIDATION_TASK_YEAR_AND_MONTH_REQUIRED,
        VALIDATION_TASK_YEAR_OR_MONTH_INVALID,
        VALIDATION_TASK_INVALID_ID,
        VALIDATION_TASK_ID_NOT_FOUND,
        VALIDATION_TASK_INVALID_STATUS,
        VALIDATION_TASK_CHANGE_NO_OPERATION_SELECTED,
        VALIDATION_TASK_LATE_TASK_WITH_NO_JUSTIFYING_POST,
        VALIDATION_TASK_INVALID_OWNER,

        VALIDATION_TASK_ID_MUST_NOT_BE_SET_ON_CREATE,
        VALIDATION_TASK_POSTS_MUST_NOT_BE_SET_ON_CREATE,
        VALIDATION_TASK_DESCRIPTION_REQUIRED,
        VALIDATION_TASK_START_REQUIRED,
        VALIDATION_TASK_FORESEEN_REQUIRED,
        VALIDATION_TASK_SOURCE_REQUIRED,
        VALIDATION_TASK_APPLICATION_REQUIRED,
        VALIDATION_TASK_OWNER_REQUIRED_AT_LEAST_ONE,
        VALIDATION_TASK_OWNER_LOGIN_REQUIRED,
        VALIDATION_TASK_OWNER_LOGIN_CONTAINS_SEPARATOR_CHAR,
        VALIDATION_TASK_FORESEEN_END_IN_THE_PAST_MUST_BE_FINISHED,
        VALIDATION_TASK_START_AFTER_END,
        VALIDATION_TASK_END_REQUIRED,
        VALIDATION_TASK_CANNOT_MODIFY_FINISHED,
        VALIDATION_TASK_CANNOT_CHANGE_START_ALREADY_STARTED,
        VALIDATION_TASK_CANNOT_RECORD_WORK_HOURS_USER_NOT_IN_TASK,
        VALIDATION_TASK_POST_BODY_DATE_INVALID,

    }
}