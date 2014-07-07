package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.*;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.TasksRepository;
import br.com.egs.task.control.core.repository.UsersRepository;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the Tasks Service implementation.
 */
public class TasksServiceTest {

    public static final String DEFAULT_TASK_ID = "111122223333aaaabbbbcccc";
    DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private TasksRepository taskRepository;
    private UsersRepository userRepository;
    private TasksService service;

    @Before
    public void setUp() {
        taskRepository = Mockito.mock(TasksRepository.class);
        userRepository = Mockito.mock(UsersRepository.class);
        service = new TasksService(taskRepository, userRepository);
    }

    @Test
    public void createTask() throws Exception {
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-02 00:00:00.000"));

        User owner1 = new User("john");
        owner1.setName("John Dev1");
        owner1.setType("N1");
        User owner2 = new User("mary");
        owner2.setName("Mary Dev2");
        owner2.setType("N2");

        Task generatedTask = new Task(
                DEFAULT_TASK_ID,
                "Test the Task Implementation",
                timestampFormat.parse("2014-01-02 00:00:00.000"),
                timestampFormat.parse("2014-01-10 23:59:59.999"),
                null,
                50,
                "Sup.Producao",
                new Application("OLM"),
                Arrays.asList(
                        new TaskOwner("john", "John Dev1", "N1"),
                        new TaskOwner("mary", "Mary Dev2", "N2")
                )
        );

        Mockito.when(userRepository.get("john")).thenReturn(owner1);
        Mockito.when(userRepository.get("mary")).thenReturn(owner2);
        Mockito.when(taskRepository.add(Mockito.any(Task.class)))
                .thenReturn(generatedTask);

        String inputString = "{task: {" +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "foreseenWorkHours: 50," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'}," +
                "           {login: 'mary'}" +
                "]" +
                "}}";

        String result = service.create(inputString);

        String expectedReturn = "{" +
                "id: '" + DEFAULT_TASK_ID + "'," +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "foreseenWorkHours: 50," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john', name: 'John Dev1', type:'N1', workDays: []}," +
                "           {login: 'mary', name: 'Mary Dev2', type:'N2', workDays: []}" +
                "]" +
                "}";

        JSONAssert.assertEquals(expectedReturn, result, true);
    }

    @Test
    public void createTask_calculateWorkHoursAutomatically() throws Exception {
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-02 00:00:00.000"));

        User owner1 = new User("john");
        owner1.setName("John Dev1");
        owner1.setType("N1");

        Task returnedTask = new Task(
                DEFAULT_TASK_ID,
                "Test the Task Implementation",
                timestampFormat.parse("2014-01-02 00:00:00.000"),
                timestampFormat.parse("2014-01-03 23:59:59.999"),
                null,
                16,
                "Sup.Producao",
                new Application("OLM"),
                Arrays.asList(
                        new TaskOwner("john", "John Dev1", "N1"),
                        new TaskOwner("mary", "Mary Dev2", "N2")
                )
        );

        Mockito.when(userRepository.get("john")).thenReturn(owner1);

        ArgumentCaptor<Task> savedTask = ArgumentCaptor.forClass(Task.class);
        Mockito.when(taskRepository.add(savedTask.capture()))
                .thenReturn(returnedTask);

        String inputString = "{task: {" +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-03'," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'}" +
                "]" +
                "}}";

        service.create(inputString);

        assertEquals(16, savedTask.getValue().getForeseenWorkHours().intValue());
    }

    @Test
    public void createTask_unknownUser() throws Exception {
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-02 00:00:00.000"));

        Mockito.when(userRepository.get("the_non-existing_user")).thenReturn(null);

        String inputString = "{task: {" +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'the_non-existing_user'}" +
                "]" +
                "}}";

        try {
            service.create(inputString);
        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_nullMonth() {
        try {
            service.searchTasks("2014", null, null, null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_nullYear() {
        try {
            service.searchTasks(null, "01", null, null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidMonth() {
        try {
            service.searchTasks("2014", "x", null, null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidMonthValue() {
        try {
            service.searchTasks("2014", "13", null, null, null, null, null, null);
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getResponse().getStatus());
        }
    }

    @Test
    public void searchTasks_invalidYear() {
        try {
            service.searchTasks("a", "1", null, null, null, null, null, null);
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

        String result = service.searchTasks("2014", "1", null, null, null, null, null, null);

        JSONAssert.assertEquals("[]", result, true);
    }

    @Test
    public void searchTasks_byMonthAndYear_success() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria().month(2014, 1);

        Task t1 = createTestTask(null, false, false);

        Task t2 = createTestTask("111122223333aaaabbbbXXXX", false, false);

        List<Task> taskList = Arrays.asList(t1, t2);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", null, null, null, null, null, null);

        Mockito.verify(taskRepository).searchTasks(generatedCriteria);

        JSONAssert.assertEquals("[" + t1.toJson() + "," + t2.toJson() + "]", result, true);
    }

    @Test
    public void searchTasks_byMonthYearAndOwner() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .ownerLogins("john");

        Task t1 = createTestTask(DEFAULT_TASK_ID, false, false);

        Task t2 = createTestTask("111122223333aaaabbbbXXXX", false, false);

        List<Task> taskList = Arrays.asList(t1, t2);
        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(taskList);

        String result = service.searchTasks("2014", "1", "john", null, null, null, null, null);

        Mockito.verify(taskRepository).searchTasks(generatedCriteria);

        JSONAssert.assertEquals("[" + t1.toJson() + "," + t2.toJson() + "]", result, true);
    }

    @Test
    public void searchTasks_byApplication() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .applications("OLM");

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", null, "OLM", null, null, null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_byApplication_multiple() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .applications("OLM", "EMM");

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", null, "OLM,EMM", null, null, null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_byOwner_multiple() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .ownerLogins("john", "mary");

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", "john,mary", null, null, null, null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_byStatus() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .status(TaskSearchCriteria.Status.FINISHED);

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", null, null, "finished", null, null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_byStatus_invalid() throws Exception {
        try {
            service.searchTasks("2014", "1", null, null, "crazyStatus", null, null, null);
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

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", null, null, "doing,waiting", null, null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_bySource() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .sources("CCC", "Internal");

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", null, null, null, "CCC,Internal", null, null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void searchTasks_excludePosts() throws Exception {
        TaskSearchCriteria generatedCriteria = new TaskSearchCriteria()
                .month(2014, 1)
                .excludePosts();

        Mockito.when(taskRepository.searchTasks(generatedCriteria)).thenReturn(new ArrayList<Task>());

        String result = service.searchTasks("2014", "1", null, null, null, null, "true", null);

        // Other tests check the resulting data. Here we only ensure that the repository
        // was called with the appropriate Criteria object.
        Mockito.verify(taskRepository).searchTasks(generatedCriteria);
    }

    @Test
    public void modifyTask_badRequest() throws Exception {
        Task testTask = createTestTask(DEFAULT_TASK_ID, true, false);
        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(testTask);

        try {
            service.modifyTask(DEFAULT_TASK_ID, "{}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }

    @Test
    public void modifyTask_finish_ok() throws Exception {
        Task storedTask = createTestTask(DEFAULT_TASK_ID, true, false);

        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(storedTask);

        service.modifyTask(DEFAULT_TASK_ID, "{endDate: '2014-01-08'}");

        // Ensure that the Task was saved, with the new endDate
        ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
        Mockito.verify(taskRepository).update(argument.capture());
        assertEquals(timestampFormat.parse("2014-01-08 23:59:59.999"), argument.getValue().getEndDate());
    }

    @Test
    public void modifyTask_finish_lateTaskError() throws Exception {
        Task storedTask = createTestTask(DEFAULT_TASK_ID, true, false);

        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(storedTask);

        try {
            service.modifyTask(DEFAULT_TASK_ID, "{endDate: '2014-01-20'}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.RECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
        }
    }

    @Test
    public void modifyTask_finish_alreadyFinished() throws Exception {
        Task storedTask = createTestTask(DEFAULT_TASK_ID, false, false);

        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(storedTask);

        try {
            service.modifyTask(DEFAULT_TASK_ID, "{endDate: '2014-01-09'}");
            fail("Exception was expected");
        } catch (WebApplicationException e) {
            assertEquals(HttpResponseUtils.UNRECOVERABLE_BUSINESS_EXCEPTION_STATUS_CODE, e.getResponse().getStatus());
        }
    }

    @Test
    public void modifyTask_changeStartDate() throws Exception {

        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-02 00:00:00.000"));

        Task storedTask = createTestTask(DEFAULT_TASK_ID, true, false);

        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(storedTask);

        service.modifyTask(DEFAULT_TASK_ID, "{startDate: '2014-01-05'}");

        // Ensure that the Task was saved, with the new date
        ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
        Mockito.verify(taskRepository).update(argument.capture());
        assertEquals(timestampFormat.parse("2014-01-05 00:00:00.000"), argument.getValue().getStartDate());
    }


    @Test
    public void modifyTask_changeForeseenEndDate() throws Exception {
        Task storedTask = createTestTask(null, true, false);

        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(storedTask);

        service.modifyTask(DEFAULT_TASK_ID, "{foreseenEndDate: '2014-01-12'}");

        // Ensure that the Task was saved, with the new date
        ArgumentCaptor<Task> argument = ArgumentCaptor.forClass(Task.class);
        Mockito.verify(taskRepository).update(argument.capture());
        assertEquals(timestampFormat.parse("2014-01-12 23:59:59.999"), argument.getValue().getForeseenEndDate());
    }

    @Test
    public void findById() throws Exception {
        Task t1 = createTestTask(null, false, false);

        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(t1);

        String result = service.findById(DEFAULT_TASK_ID);

        JSONAssert.assertEquals(t1.toJson(), result, true);
    }

    @Test
    public void findById_notFound() throws Exception {
        Mockito.when(taskRepository.get(DEFAULT_TASK_ID)).thenReturn(null);

        try {
            service.findById(DEFAULT_TASK_ID);
            fail("Exception expected");
        } catch (WebApplicationException e) {
            assertEquals(404, e.getResponse().getStatus());
        }
    }

    private Task createTestTask(String id, boolean nullEndDate, boolean excludePosts) throws Exception {
        Task t = new Task(
                    id != null ? id : DEFAULT_TASK_ID,
                    "Test the Task Implementation",

                    timestampFormat.parse("2014-01-02 00:00:00.000"),
                    timestampFormat.parse("2014-01-10 23:59:59.000"),
                    nullEndDate ? null : timestampFormat.parse("2014-01-09 23:59:59.000"),
                    50,

                    "Sup.Producao",
                    new Application("OLM"),

                    Arrays.asList(new TaskOwner("john", "John The Programmer", "N1"),
                                    new TaskOwner("mary", "Mary Developer", "N2")));

        if (!excludePosts) {
            Post p1 = new Post("john", "John The Programmer", "Scope changed. No re-scheduling will be necessary",
                    timestampFormat.parse("2014-01-03 09:15:30.700"));
            t.addPost(p1);

            Post p2 = new Post("john", "John The Programmer", "Doing #overtime to finish it sooner",
                    timestampFormat.parse("2014-01-08 18:20:49.150"));
            t.addPost(p2);
        }

        return t;
    }
}
