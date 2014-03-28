package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.Users;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the authentication service implementation
 */
public class AuthenticationServiceTest {

    private Users userRepository;
    private AuthenticationService service;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(Users.class);
        service = new AuthenticationService(userRepository);
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
        Mockito.when(userRepository.get("wrong")).thenReturn(null);

        try {
            service.authenticate("{username: 'wrong', password: 'ABCD1234'}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
            Mockito.verify(userRepository, Mockito.only()).get("wrong");
        }
    }

    @Test
    public void invalidPassword() {
        Mockito.when(userRepository.get("mylogin")).thenReturn(testUser());

        try {
            service.authenticate("{username: 'mylogin', password: 'wrong'}");
            fail("Exception was expected");

        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
            Mockito.verify(userRepository, Mockito.only()).get("mylogin");
        }
    }

    @Test
    public void sucessfullLogin() throws JSONException {
        Mockito.when(userRepository.get("mylogin")).thenReturn(testUser());

        String result = service.authenticate("{username: 'mylogin', password: 'ABCD1234'}");

        String userJson = "{" +
                "'login':'mylogin'," +
                "'name':'Authenticated User'," +
                "'email':'u@ser.com'," +
                "'type':'N2'," +
                "'applications':[{'name':'OLM'}]" +
                "}";

        JSONAssert.assertEquals(userJson, result, true);
        Mockito.verify(userRepository, Mockito.only()).get("mylogin");
    }

    private User testUser() {
        User u = new User("mylogin");
        u.setName("Authenticated User");
        u.setEmail("u@ser.com");
        u.setType("N2");
        u.setApplications(Arrays.asList(new Application("OLM")));

        u.setPasswordAsText("ABCD1234");

        return u;
    }
}
