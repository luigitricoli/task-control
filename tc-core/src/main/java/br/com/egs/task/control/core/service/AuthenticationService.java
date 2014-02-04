package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.Users;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("authentication")
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    /** A custom status code that indicates a login failure */
    private static final int LOGIN_FAILURE_STATUS = 420;

    private Users repository;

    @Inject
    public AuthenticationService(Users repository) {
        this.repository = repository;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String authenticate(String body) {
        if (StringUtils.isBlank(body)) {
            throw new WebApplicationException("Request body cannot by null", Response.Status.BAD_REQUEST);
        }

        AuthenticationData authData;
        try {
            authData = new Gson().fromJson(body, AuthenticationData.class);
        } catch (JsonSyntaxException e) {
            throw new WebApplicationException("Malformed authentication data", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(authData.username)) {
            throw new WebApplicationException("The 'username' attribute is missing", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(authData.password)) {
            throw new WebApplicationException("The 'password' attribute is missing", Response.Status.BAD_REQUEST);
        }

        User user = repository.get(authData.username);

        if (user == null) {
            throw new WebApplicationException("Invalid username and/or password", LOGIN_FAILURE_STATUS);
        }

        if (!user.checkPassword(authData.password)) {
            throw new WebApplicationException("Invalid username and/or password", LOGIN_FAILURE_STATUS);
        }

        return user.toJson();
    }

    public static class AuthenticationData {
        public String username;
        public String password;
    }
}
