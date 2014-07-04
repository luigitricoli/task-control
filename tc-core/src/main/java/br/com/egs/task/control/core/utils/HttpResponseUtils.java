package br.com.egs.task.control.core.utils;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 *
 */
public class HttpResponseUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    /**
     * Recoverable business error, according to the application convention.
     */
    public static final int RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE = 480;

    /**
     * Unrecoverable business error, according to the application convention.
     */
    public static final int UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE = 481;

    private Messages messages;

    public HttpResponseUtils() {
        this.messages = new Messages();
    }

    /**
     *
     */
    public WebApplicationException buildBadRequestException(Messages.Keys key, Object... messageArguments) throws WebApplicationException {
        return buildWebApplicationException(Response.Status.BAD_REQUEST, key, messageArguments);
    }

    /**
     *
     */
    public WebApplicationException buildNotFoundException(Messages.Keys key, Object... messageArguments) throws WebApplicationException {
        return buildWebApplicationException(Response.Status.NOT_FOUND, key, messageArguments);
    }

    /**
     *
     */
    public WebApplicationException buildRecoverableBusinessException(Messages.Keys key, Object... messageArguments) throws WebApplicationException {
        return buildWebApplicationException(RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, key, messageArguments);
    }

    /**
     *
     */
    public WebApplicationException buildUnrecoverableBusinessException(Messages.Keys key, Object... messageArguments) throws WebApplicationException {
        return buildWebApplicationException(UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, key, messageArguments);
    }

    /**
     *
     */
    private WebApplicationException buildWebApplicationException(Response.Status st, Messages.Keys key, Object... messageArguments)
            throws WebApplicationException {

        String message = messages.get(key, messageArguments);

        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        Response response = Response
                .status(st)
                .entity(json.toString())
                .build();
        return new WebApplicationException(message, response);
    }

    /**
     *
     */
    private WebApplicationException buildWebApplicationException(int st, Messages.Keys key, Object... messageArguments)
            throws WebApplicationException {

        logger.debug("Building error response. Status: {}, Message: [{}], Args: {}",
                st, key, messageArguments == null ? "(none)" : Arrays.toString(messageArguments));

        String message = messages.get(key, messageArguments);

        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        Response response = Response
                .status(st)
                .entity(json.toString())
                .build();
        return new WebApplicationException(message, response);
    }
}
