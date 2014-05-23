package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.UsersRepository;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import br.com.egs.task.control.core.utils.Messages;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 */
@Path("authentication")
public class AuthenticationService {

    private UsersRepository repository;
    private HttpResponseUtils responseUtils;

    @Inject
    public AuthenticationService(UsersRepository repository) {
        this.repository = repository;
        this.responseUtils = new HttpResponseUtils();
    }

    @POST
    @Produces("application/json;charset=UTF-8")
    @Consumes("application/json;charset=UTF-8")
    public String authenticate(String body) {
        if (StringUtils.isBlank(body)) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_GENERAL_REQUEST_BODY_CANNOT_BE_NULL);
        }

        AuthenticationData authData;
        try {
            authData = new Gson().fromJson(body, AuthenticationData.class);
        } catch (JsonSyntaxException e) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_GENERAL_MALFORMED_REQUEST);
        }

        if (StringUtils.isBlank(authData.username)) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_LOGIN_MISSING_USERNAME);
        }
        if (StringUtils.isBlank(authData.password)) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_LOGIN_MISSING_PASSWORD);
        }

        User user = repository.get(authData.username);

        if (user == null) {
            throw responseUtils.buildRecoverableBusinessException(Messages.Keys.VALIDATION_LOGIN_INVALID_CREDENTIALS);
        }

        if (!user.checkPassword(authData.password)) {
            throw responseUtils.buildRecoverableBusinessException(Messages.Keys.VALIDATION_LOGIN_INVALID_CREDENTIALS);
        }

        return user.toJson();
    }

    public static class AuthenticationData {
        public String username;
        public String password;
    }
}
