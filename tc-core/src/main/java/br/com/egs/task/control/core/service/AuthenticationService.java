package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.Users;
import br.com.egs.task.control.core.utils.WebserviceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Request body cannot by null");
        }

        AuthenticationData authData = null;
        try {
            authData = new Gson().fromJson(body, AuthenticationData.class);
        } catch (JsonSyntaxException e) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Malformed authentication data");
        }

        if (StringUtils.isBlank(authData.username)) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "The 'username' attribute is missing");
        }
        if (StringUtils.isBlank(authData.password)) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "The 'password' attribute is missing");
        }

        User user = repository.get(authData.username);

        if (user == null) {
            WebserviceUtils.throwWebApplicationException(LOGIN_FAILURE_STATUS, "Invalid username and/or password");
        }

        if (!user.checkPassword(authData.password)) {
            WebserviceUtils.throwWebApplicationException(LOGIN_FAILURE_STATUS, "Invalid username and/or password");
        }

        return user.toJson();
    }

    public static class AuthenticationData {
        public String username;
        public String password;
    }
}
