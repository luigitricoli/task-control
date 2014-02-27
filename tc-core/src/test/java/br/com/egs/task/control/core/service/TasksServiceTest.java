package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
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

        Task t1 = createTestTask(null, false);

        Task t2 = createTestTask("111122223333aaaabbbbXXXX", false);

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

        Task t1 = createTestTask(null, false);

        Task t2 = createTestTask("111122223333aaaabbbbXXXX", false);

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

        Task t1 = createTestTask(null, false);

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

        Task t1 = createTestTask(null, false);

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

        Task t1 = createTestTask(null, false);

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

        Task t1 = createTestTask(null, false);

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

        Task t1 = createTestTask(null, false);

        List<Task> taskList = Arrays.asList(t1);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, null, null, null, "true");

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void modifyTask_badRequest() throws Exception {
        Task testTask = createTestTask(null, true);
        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(testTask);

        try {
            service.modifyTask("111122223333aaaabbbbcccc", "{}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }

    @Test
    public void modifyTask_finish_ok() throws Exception {
        Task storedTask = createTestTask(null, true);

        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(storedTask);

        service.modifyTask("111122223333aaaabbbbcccc", "{endDate: '2014-01-08'}");

        // Ensure that the Task was saved, with the new endDate
        ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
        Mockito.verify(taskRepository).update(argument.capture());
        assertEquals(timestampFormat.parse("2014-01-08 23:59:59.999"), argument.getValue().getEndDate());
    }

    @Test
    public void modifyTask_finish_lateTaskError() throws Exception {
        Task storedTask = createTestTask(null, true);

        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(storedTask);

        try {
            service.modifyTask("111122223333aaaabbbbcccc", "{endDate: '2014-01-20'}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
        }
    }

    @Test
    public void modifyTask_finish_alreadyFinished() throws Exception {
        Task storedTask = createTestTask(null, false);

        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(storedTask);

        try {
            service.modifyTask("111122223333aaaabbbbcccc", "{endDate: '2014-01-09'}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
        }
    }

    @Test
    public void modifyTask_changeStartDate_ok() throws Exception {
        Task storedTask = createTestTask(null, true);

        Mockito.when(taskRepository.get("111122223333aaaabbbbcccc")).thenReturn(storedTask);

        service.modifyTask("111122223333aaaabbbbcccc", "{startDate: '2014-01-05'}");

        // Ensure that the Task was saved, with the new date
        //ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
        //Mockito.verify(taskRepository).update(argument.capture());
        //assertEquals(timestampFormat.parse("2014-01-05 00:00:00.000"), argument.getValue().getStartDate());
    }

    private Task createTestTask(String customId, boolean nullEndDate) throws ParseException {
        Task t = new Task(
                    customId != null ? customId : "111122223333aaaabbbbcccc",
                    "Test the Task Implementation",

                    timestampFormat.parse("2014-01-02 00:00:00.000"),
                    timestampFormat.parse("2014-01-10 23:59:59.000"),
                    nullEndDate ? null : timestampFormat.parse("2014-01-09 23:59:59.000"),

                    "Sup.Producao",
                    new Application("OLM"),

                    Arrays.asList(new TaskOwner("john"), new TaskOwner("mary")));

        Post p1 = new Post("john", "Scope changed. No re-scheduling will be necessary",
                timestampFormat.parse("2014-01-03 09:15:30.700"));
        t.addPost(p1);

        Post p2 = new Post("john", "Doing #overtime to finish it sooner",
                timestampFormat.parse("2014-01-08 18:20:49.150"));
        t.addPost(p2);

        return t;
    }
}
