package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.repository.UsersRepository;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
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

    @Inject
    public UsersService(UsersRepository repository) {
        this.repository = repository;
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
            HttpResponseUtils.throwNotFoundException("User [" + login + "] not found");
        }

        return user.toJson();
    }

    @POST
    @Produces("application/json;charset=UTF-8")
    public String create(String body) {
        if (StringUtils.isBlank(body)) {
            HttpResponseUtils.throwBadRequestException("Request body cannot by null");
        }

        User user = null;
        try {
            user = User.fromJson(body);
        } catch (JsonSyntaxException e) {
            HttpResponseUtils.throwBadRequestException("Invalid request data: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            HttpResponseUtils.throwBadRequestException("Invalid request data:" + e.getMessage());
        }


        try {
            user.validate();
        } catch (ValidationException ve) {
            HttpResponseUtils.throwBadRequestException("Error validating user: " + ve.getMessage());
        }

        User existingUser = repository.get(user.getLogin());
        if (existingUser != null) {
            HttpResponseUtils.throwUnrecoverableBusinessException("User already exists: " + existingUser.getLogin());
        }

        repository.add(user);

        return user.toJson();
    }

    @PUT
    @Path("{login}")
    @Produces("application/json;charset=UTF-8")
    public String update(@PathParam("login") String login, String body) {
        if (StringUtils.isBlank(body)) {
            HttpResponseUtils.throwBadRequestException("Request body cannot by null");
        }

        User newUserData = null;
        try {
            newUserData = User.fromJson(body, login);
        } catch (JsonSyntaxException e) {
            HttpResponseUtils.throwBadRequestException("Invalid request data");
        }

        User currentlySavedUser = repository.get(login);
        if (currentlySavedUser == null) {
            HttpResponseUtils.throwNotFoundException("User does no exist: " + login);
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
            HttpResponseUtils.throwBadRequestException("Error validating user: " + ve.getMessage());
        }

        repository.update(currentlySavedUser);

        return currentlySavedUser.toJson();
    }
}
