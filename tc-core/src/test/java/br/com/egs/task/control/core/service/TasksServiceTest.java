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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the Tasks Service implementation.
 */
public class TasksServiceTest {

    DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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
            service.searchTasks("2014", null, null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_nullYear() {
        try {
            service.searchTasks(null, "01", null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidMonth() {
        try {
            service.searchTasks("2014", "x", null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidMonthValue() {
        try {
            service.searchTasks("2014", "13", null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidYear() {
        try {
            service.searchTasks("a", "1", null, null, null, null, null);
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

        String result = service.searchTasks("2014", "1", null, null, null, null, null);

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

        String result = service.searchTasks("2014", "1", null, null, null, null, null);

        Mockito.verify(taskRepository).searchTasks(generatedCriteria);

        JSONAssert.assertEquals("[" + t1.toJson() + "," + t2.toJson() + "]", result, true);
    }

    @Test
    public void searchTasks_byMonthYearAndOwner() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .ownerLogin("john");

        Task t1 = createTestTask();

        Task t2 = createTestTask();
        t2.setId("111122223333aaaabbbbXXXX");

        List<Task> taskList = Arrays.asList(t1, t2);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", "john", null, null, null, null);

        Mockito.verify(taskRepository).searchTasks(generatedCriteria);

        JSONAssert.assertEquals("[" + t1.toJson() + "," + t2.toJson() + "]", result, true);
    }

    @Test
    public void searchTasks_byApplication() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .application("OLM");

        Task t1 = createTestTask();

        List<Task> taskList = Arrays.asList(t1);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, "OLM", null, null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_byStatus() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .status(TaskSearchCriteria.Status.FINISHED);

        Task t1 = createTestTask();

        List<Task> taskList = Arrays.asList(t1);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, null, "finished", null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_byStatus_invalid() throws Exception {
        try {
            service.searchTasks("2014", "1", null, null, "crazyStatus", null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_byStatus_multiple() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .status(TaskSearchCriteria.Status.DOING, TaskSearchCriteria.Status.WAITING);

        Task t1 = createTestTask();

        List<Task> taskList = Arrays.asList(t1);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, null, "doing,waiting", null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_bySource() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .sources("CCC", "Internal");

        Task t1 = createTestTask();

        List<Task> taskList = Arrays.asList(t1);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, null, null, "CCC,Internal", null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_excludePosts() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .excludePosts();

        Task t1 = createTestTask();

        List<Task> taskList = Arrays.asList(t1);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, null, null, null, "true");

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void modifyTask_finish() throws Exception {
        Task storedTask = createTestTask();
        storedTask.setEndDate(null);

        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(storedTask);

        service.modifyTask("111122223333aaaabbbbcccc", "{endDate: '2014-01-08 23:59:59'}");

        // Ensure that the Task was saved, with the new endDate
        ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
        Mockito.verify(taskRepository).update(argument.capture());
        assertEquals(timestampFormat.parse("2014-01-08 23:59:59.999"), argument.getValue().getEndDate());
    }

    @Test
    public void modifyTask_emptyRequest() throws Exception {
        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(createTestTask());

        try {
            service.modifyTask("111122223333aaaabbbbcccc", "{}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }

    public void modifyTask_finishLateTaskError() throws Exception {
        Task storedTask = createTestTask();
        storedTask.setEndDate(null);


    }

    private Task createTestTask() throws ParseException {
        Task t = new Task();
        t.setId("111122223333aaaabbbbcccc");
        t.setDescription("Test the Task Implementation");

        t.setStartDate(timestampFormat.parse("2014-01-02 00:00:00.000"));
        t.setForeseenEndDate(timestampFormat.parse("2014-01-10 23:59:59.000"));
        t.setEndDate(timestampFormat.parse("2014-01-09 23:59:59.000"));

        t.setSource("Sup.Producao");
        t.setApplication(new Application("OLM"));

        t.setOwners(Arrays.asList(new TaskOwner("john"), new TaskOwner("mary")));

        Post p1 = new Post("john", "Scope changed. No re-scheduling will be necessary",
                timestampFormat.parse("2014-01-03 09:15:30.700"));
        t.addPost(p1);

        Post p2 = new Post("john", "Doing #overtime to finish it sooner",
                timestampFormat.parse("2014-01-08 18:20:49.150"));
        t.addPost(p2);

        return t;
    }
}
