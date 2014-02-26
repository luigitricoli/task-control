package br.com.egs.task.control.core.utils;

import com.google.gson.JsonObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 */
public class HttpResponseUtils {

    /**
     * Recoverable business error, according to the application convention.
     */
    public static final int RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE = 480;

    /**
     * Unrecoverable business error, according to the application convention.
     */
    public static final int UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE = 481;

    /**
     * End the current request with status 400, by throwing the appropriate
     * WebApplicationException
     * @param message
     */
    public static void throwBadRequestException(String message) throws WebApplicationException {
        throwWebApplicationException(Response.Status.BAD_REQUEST, message);
    }

    /**
     * End the current request with status 404, by throwing the appropriate
     * WebApplicationException
     * @param message
     */
    public static void throwNotFoundException(String message) throws WebApplicationException {
        throwWebApplicationException(Response.Status.NOT_FOUND, message);
    }

    /**
     * End the current request with status 'Recoverable Business Exception', by throwing the appropriate
     * WebApplicationException
     * @param message
     */
    public static void throwRecoverableBusinessException(String message) throws WebApplicationException {
        throwWebApplicationException(RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, message);
    }

    /**
     * End the current request with status 'Recoverable Business Exception', by throwing the appropriate
     * WebApplicationException
     * @param message
     */
    public static void throwUnrecoverableBusinessException(String message) throws WebApplicationException {
        throwWebApplicationException(UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, message);
    }

    /**
     * Throw a WebApplicationException, which allows the service to return a specific
     * status code and response body
     * @param st
     * @param message
     */
    private static void throwWebApplicationException(Response.Status st, String message)
            throws WebApplicationException {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        Response response = Response
                .status(st)
                .entity(json.toString())
                .build();
        throw new WebApplicationException(message, response);
    }

    /**
     * Throw a WebApplicationException, which allows the service to return a specific
     * status code and response body
     * @param st
     * @param message
     */
    private static void throwWebApplicationException(int st, String message)
            throws WebApplicationException {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        Response response = Response
                .status(st)
                .entity(json.toString())
                .build();
        throw new WebApplicationException(message, response);
    }
}
