package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.repository.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
            throw new WebApplicationException("User [" + login + "] not found", Response.Status.NOT_FOUND);
        }

        return user.toJson();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String body) {
        if (StringUtils.isBlank(body)) {
            throw new WebApplicationException("Request body cannot by null", Response.Status.BAD_REQUEST);
        }

        User user = null;
        try {
            user = new Gson().fromJson(body, User.class);
        } catch (JsonSyntaxException e) {
            throw new WebApplicationException("Invalid request data", Response.Status.BAD_REQUEST);
        }

        String generatedPassword = user.generateRandomPassword();

        try {
            user.validate();
        } catch (ValidationException ve) {
            throw new WebApplicationException("Error validating user: " + ve.getMessage(), Response.Status.BAD_REQUEST);
        }

        repository.add(user);

        return "{\"generatedPassword\" : \"" + generatedPassword + "\"}";
    }
}
