package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.repository.Users;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 *
 */
@Path("users")
public class UsersService {

    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    private Users repository;

    @Inject
    public UsersService(Users repository) {
        this.repository = repository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String allUsers() {
        List<User> allUsers = repository.getAll();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new User.UserSerializer(true))
                .create();
        return gson.toJson(allUsers);
    }

    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findUser(@PathParam("login") String login) {
        User user = repository.get(login);

        if (user == null) {
            HttpResponseUtils.throwNotFoundException("User [" + login + "] not found");
        }

        return user.toJson();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String body) {
        if (StringUtils.isBlank(body)) {
            HttpResponseUtils.throwBadRequestException("Request body cannot by null");
        }

        User user = null;
        try {
            user = new Gson().fromJson(body, User.class);
        } catch (JsonSyntaxException e) {
            HttpResponseUtils.throwBadRequestException("Invalid request data");
        }

        String generatedPassword = user.generateRandomPassword();

        try {
            user.validate();
        } catch (ValidationException ve) {
            HttpResponseUtils.throwBadRequestException("Error validating user: " + ve.getMessage());
        }

        repository.add(user);

        return "{\"generatedPassword\" : \"" + generatedPassword + "\"}";
    }

    @PUT
    @Path("{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@PathParam("login") String login, String body) {
        if (StringUtils.isBlank(body)) {
            HttpResponseUtils.throwBadRequestException("Request body cannot by null");
        }

        User user = null;
        try {
            user = new Gson().fromJson(body, User.class);
        } catch (JsonSyntaxException e) {
            HttpResponseUtils.throwBadRequestException("Invalid request data");
        }

        User currentlySavedUser = repository.get(login);
        if (currentlySavedUser == null) {
            HttpResponseUtils.throwNotFoundException("User does no exist: " + login);
        }

        // The identification attributes (login and password) will not be changed
        User updatedUser = new User(login);
        updatedUser.setPasswordHash(currentlySavedUser.getPasswordHash());
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setApplications(user.getApplications());

        try {
            updatedUser.validate();
        } catch (ValidationException ve) {
            HttpResponseUtils.throwBadRequestException("Error validating user: " + ve.getMessage());
        }

        repository.update(updatedUser);

        return updatedUser.toJson();
    }
}
