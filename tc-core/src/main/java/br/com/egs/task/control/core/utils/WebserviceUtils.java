package br.com.egs.task.control.core.utils;

import com.google.gson.JsonObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 */
public class WebserviceUtils {

    /**
     * Throw a WebApplicationException, which allows the service to return a specific
     * status code and response body
     * @param st
     * @param message
     */
    public static void throwWebApplicationException(Response.Status st, String message) {
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
    public static void throwWebApplicationException(int st, String message) {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        Response response = Response
                .status(st)
                .entity(json.toString())
                .build();
        throw new WebApplicationException(message, response);
    }
}
