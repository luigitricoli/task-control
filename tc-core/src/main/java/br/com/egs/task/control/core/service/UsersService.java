package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.Users;
import com.google.gson.Gson;
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
        return new Gson().toJson(allUsers);
    }

    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findUser(@PathParam("login") String login) {
        User user = repository.get(login);

        if (user == null) {
            //throw new WebApplicationException("User [" + login + "] not found", Response.Status.NOT_FOUND);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return new Gson().toJson(user);
    }

    @POST
    public void create(String body) {
        if (StringUtils.isBlank(body)) {
            //throw new WebApplicationException("Request body cannot by null", Response.Status.BAD_REQUEST);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
