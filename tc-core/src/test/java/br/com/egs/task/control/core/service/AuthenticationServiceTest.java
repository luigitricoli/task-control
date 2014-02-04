package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.Users;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the authentication service implementation
 */
public class AuthenticationServiceTest {

    private AuthenticationService service;

    @Before
    public void setUp() {
        service = new AuthenticationService(new MockUserRepository());
    }

    @Test
    public void emptyRequest() {
        try {
            service.authenticate("");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }
    @Test
    public void invalidJsonData() {
        try {
            service.authenticate("{A INVALID JSON STRING}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void missingUsername() {
        try {
            service.authenticate("{password: 'ABCD1234'}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }
    @Test
    public void missingPassword() {
        try {
            service.authenticate("{username: 'mylogin'}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void invalidLogin() {
        try {
            service.authenticate("{username: 'wrong', password: 'ABCD1234'}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(420, e.getResponse().getStatus());
        }
    }

    @Test
    public void invalidPassword() {
        try {
            service.authenticate("{username: 'mylogin', password: 'wrong'}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(420, e.getResponse().getStatus());
        }
    }

    @Test
    public void sucessfullLogin() throws JSONException {
        String result = service.authenticate("{username: 'mylogin', password: 'ABCD1234'}");

        String userJson = "{" +
                "'login':'mylogin'," +
                "'name':'Authenticated User'," +
                "'email':'u@ser.com'," +
                "'applications':[{'name':'OLM'}]" +
                "}";

        JSONAssert.assertEquals(userJson, result, true);
    }

    /**
     *
     */
    private class MockUserRepository implements Users {

        @Override
        public User get(String login) {
            if ("mylogin".equals(login)) {
                User u = new User(login);
                u.setName("Authenticated User");
                u.setEmail("u@ser.com");
                u.setApplications(Arrays.asList(new Application("OLM")));

                u.setPasswordAsText("ABCD1234");

                return u;

            } else {
                return null;
            }
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            throw new UnsupportedOperationException();
        }
    }
}
