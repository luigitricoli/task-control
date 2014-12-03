package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.repository.UsersRepository;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import br.com.egs.task.control.core.utils.Messages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

/**
 *
 */
@Path("users")
public class UsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    private UsersRepository repository;

    private HttpResponseUtils responseUtils;

    @Inject
    public UsersService(UsersRepository repository) {
        this.repository = repository;
        this.responseUtils = new HttpResponseUtils();
    }

    @GET
    @Produces("application/json;charset=UTF-8")
    public String listUsers(
            @QueryParam("application") String application) {

        List<User> users;
        if (StringUtils.isBlank(application)) {
            users = repository.getAll();
        } else {
            users = repository.getByApplication(application);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new User.UserSerializer(true))
                .create();
        return gson.toJson(users);
    }

    @GET
    @Path("{login}")
    @Produces("application/json;charset=UTF-8")
    public String findUser(@PathParam("login") String login) {
        User user = repository.get(login);

        if (user == null) {
            throw responseUtils.buildNotFoundException(Messages.Keys.VALIDATION_USER_UNKNOWN_LOGIN, login);
        }

        return user.toJson();
    }

    @POST
    @Produces("application/json;charset=UTF-8")
    public String create(String body) {
        log.debug("UsersService::create. Request body:\n{}", body);

        if (StringUtils.isBlank(body)) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_GENERAL_REQUEST_BODY_CANNOT_BE_NULL);
        }

        User user;
        try {
            user = User.fromJson(body);
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            throw responseUtils.buildBadRequestException(
                    Messages.Keys.VALIDATION_GENERAL_MALFORMED_REQUEST_ARG, e.getMessage());
        }

        try {
            user.validate();
        } catch (ValidationException ve) {
            throw responseUtils.buildBadRequestException(ve.getUserMessageKey());
        }

        User existingUser = repository.get(user.getLogin());
        if (existingUser != null) {
            throw responseUtils.buildUnrecoverableBusinessException(
                    Messages.Keys.VALIDATION_USER_ALREADY_EXISTS, existingUser.getLogin());
        }

        repository.add(user);

        return user.toJson();
    }

    @PUT
    @Path("{login}")
    @Produces("application/json;charset=UTF-8")
    public String update(@PathParam("login") String login, String body) {
        log.debug("UsersService::update. Login=[ {} ] Request body:\n{}", login, body);

        if (StringUtils.isBlank(body)) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_GENERAL_REQUEST_BODY_CANNOT_BE_NULL);
        }

        User newUserData;
        try {
            newUserData = User.fromJson(body, login);
        } catch (JsonSyntaxException e) {
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_GENERAL_MALFORMED_REQUEST);
        }

        User currentlySavedUser = repository.get(login);
        if (currentlySavedUser == null) {
            throw responseUtils.buildNotFoundException(Messages.Keys.VALIDATION_USER_UNKNOWN_LOGIN, login);
        }

        boolean newPasswordInformed = StringUtils.isNotBlank(newUserData.getPasswordHash());
        boolean userDataInformed = StringUtils.isNotBlank(newUserData.getName())
                || StringUtils.isNotBlank(newUserData.getType())
                || StringUtils.isNotBlank(newUserData.getEmail())
                || newUserData.getApplications() != null;

        if (newPasswordInformed) {
            currentlySavedUser.copyPasswordFrom(newUserData);
        }

        if (userDataInformed) {
            currentlySavedUser.setName(newUserData.getName());
            currentlySavedUser.setEmail(newUserData.getEmail());
            currentlySavedUser.setType(newUserData.getType());
            currentlySavedUser.setApplications(newUserData.getApplications());
        }

        try {
            currentlySavedUser.validate();
        } catch (ValidationException ve) {
            throw responseUtils.buildBadRequestException(ve.getUserMessageKey());
        }

        repository.update(currentlySavedUser);

        return currentlySavedUser.toJson();
    }

    @DELETE
    @Path("{login}")
    public void cancelUser(@PathParam("login") String login) {
        log.info("Cancelling (DELETE) user. ID: [ {} ]", login);

        User user = repository.get(login);
        if (user == null) {
            throw responseUtils.buildNotFoundException(Messages.Keys.VALIDATION_USER_UNKNOWN_LOGIN, login);
        }

        repository.remove(user);
    }
}
