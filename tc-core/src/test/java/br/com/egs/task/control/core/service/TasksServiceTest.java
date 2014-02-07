package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the Tasks Service implementation.
 */
public class TasksServiceTest {

    private Tasks taskRepository;
    private TasksService service;

    @Before
    public void setUp() {
        taskRepository = Mockito.mock(Tasks.class);
        service = new TasksService(taskRepository);
    }

    @Test
    public void searchTasks_nullMonth() {
        try {
            service.searchTasks("2014", null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_nullYear() {
        try {
            service.searchTasks(null, "01");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidMonth() {
        try {
            service.searchTasks("2014", "x");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidMonthValue() {
        try {
            service.searchTasks("2014", "13");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidYear() {
        try {
            service.searchTasks("a", "1");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_noRecordFound() throws JSONException {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria().month(2014, 1);

        List<Task> empty = Collections.emptyList();
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(empty);

        String result = service.searchTasks("2014", "1");

        JSONAssert.assertEquals("[]", result, true);
    }

    @Test
    public void searchTasks_byMonthAndYear_success() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria().month(2014, 1);

        Task t1 = createTestTask();

        Task t2 = createTestTask();
        t2.setId("111122223333aaaabbbbXXXX");

        List<Task> taskList = Arrays.asList(t1, t2);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1");

        System.out.println(result);

        JSONAssert.assertEquals("[" + t1.toJson() + "," + t2.toJson() + "]", result, true);
    }

    private Task createTestTask() throws ParseException {
        DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Task t = new Task();
        t.setId("111122223333aaaabbbbcccc");
        t.setDescription("Test the Task Implementation");

        t.setStartDate(timestampFormat.parse("2014-01-02 00:00:00.000"));
        t.setForeseenEndDate(timestampFormat.parse("2014-01-10 23:59:59.000"));
        t.setEndDate(timestampFormat.parse("2014-01-09 23:59:59.000"));

        t.setSource("Sup.Producao");
        t.setApplication(new Application("OLM"));

        t.setOwners(Arrays.asList(new TaskOwner("john"), new TaskOwner("mary")));

        List<Post> posts = new ArrayList<>();

        Post p1 = new Post();
        p1.setTimestamp(timestampFormat.parse("2014-01-03 09:15:30.700"));
        p1.setUser("john");
        p1.setText("Scope changed. No re-scheduling will be necessary");
        posts.add(p1);

        Post p2 = new Post();
        p2.setTimestamp(timestampFormat.parse("2014-01-08 18:20:49.150"));
        p2.setUser("john");
        p2.setText("Doing #overtime to finish it sooner");
        posts.add(p2);

        t.setPosts(posts);

        return t;
    }
}
