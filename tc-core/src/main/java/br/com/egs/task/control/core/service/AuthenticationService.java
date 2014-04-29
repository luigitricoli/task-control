package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.UsersRepository;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Path("authentication")
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private UsersRepository repository;

    @Inject
    public AuthenticationService(UsersRepository repository) {
        this.repository = repository;
    }

    @POST
    @Produces("application/json;charset=UTF-8")
    @Consumes("application/json;charset=UTF-8")
    public String authenticate(String body) {
        if (StringUtils.isBlank(body)) {
            HttpResponseUtils.throwBadRequestException("Request body cannot by null");
        }

        AuthenticationData authData = null;
        try {
            authData = new Gson().fromJson(body, AuthenticationData.class);
        } catch (JsonSyntaxException e) {
            HttpResponseUtils.throwBadRequestException("Malformed authentication data");
        }

        if (StringUtils.isBlank(authData.username)) {
            HttpResponseUtils.throwBadRequestException("The 'username' attribute is missing");
        }
        if (StringUtils.isBlank(authData.password)) {
            HttpResponseUtils.throwBadRequestException("The 'password' attribute is missing");
        }

        User user = repository.get(authData.username);

        if (user == null) {
            HttpResponseUtils.throwRecoverableBusinessException("Invalid username and/or password");
        }

        if (!user.checkPassword(authData.password)) {
            HttpResponseUtils.throwRecoverableBusinessException("Invalid username and/or password");
        }

        return user.toJson();
    }

    public static class AuthenticationData {
        public String username;
        public String password;
    }
}
