package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.SimpleTaskData;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.filter.Applications;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import br.com.egs.task.control.web.rest.client.user.CoreUser;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasckClientTest {

    @Test
    public void numberOfWeeks() {
        String json = "[]";
        JsonClient client = mock(JsonClient.class);
        when(client.at("tasks")).thenReturn(client);
        when(client.addUrlParam("year", "2014")).thenReturn(client);
        when(client.addUrlParam("month", "1")).thenReturn(client);

        Response resp = mock(Response.class);
        when(resp.getContent()).thenReturn(json);

        when(client.getAsJson()).thenReturn(resp);

        User mockUser = mock(User.class);
        when(mockUser.getNickname()).thenReturn("john");

        SessionUser mockSession = mock(SessionUser.class);
        when(mockSession.isAdmin()).thenReturn(false);
        when(mockSession.getUser()).thenReturn(mockUser);

        TaskRepository repo = new TaskClient(new FilterFormat(), client, mockSession);
        List<Week> weeks = repo.weeksBy(1);

        assertThat(weeks.size(), is(6));
    }

    @Test
    public void convertCoreTasksToSimpleTaskData() throws Exception {
        TaskClient repo = new TaskClient(null, null, null);
        
        List<CoreTask> inputTasks = new ArrayList<>();
        
        inputTasks.add(new CoreTask(
                new TaskDate("2014-01-05"), 
                new TaskDate("2014-01-07"), 
                30, 
                "A Conversion Test Task", 
                "CCC", 
                "task_control", 
                Arrays.asList(new CoreUser("john", "John Programmer", null, null))));
        
        final List<SimpleTaskData> result = repo.convertCoreTasksToSimpleTaskData(inputTasks);
        
        assertThat(result.size(), is(1));
        
        assertThat(result.get(0).getDescription(), is("A Conversion Test Task"));
        assertThat(result.get(0).getStartDate(), is("05/01/14"));
        assertThat(result.get(0).getForeseenEndDate(), is("07/01/14"));
        assertThat(result.get(0).getForeseenWorkHours(), is(30));
        assertThat(result.get(0).getSource(), is("CCC"));
        assertThat(result.get(0).getApplication(), is("task_control"));
        assertThat(result.get(0).getOwners(), is("John Programmer"));
    }

    @Test
    public void convertCoreTasksToSimpleTaskData_multipleOwners() throws Exception {
        TaskClient repo = new TaskClient(null, null, null);
        
        List<CoreTask> inputTasks = new ArrayList<>();
        
        inputTasks.add(new CoreTask(
                new TaskDate("2014-01-05"), 
                new TaskDate("2014-01-07"), 
                30, 
                "A Conversion Test Task", 
                "CCC", 
                "task_control", 
                Arrays.asList(new CoreUser("john", "John Programmer", null, null),
                                new CoreUser("mary", "Mary Developer", null, null))));
        
        final List<SimpleTaskData> result = repo.convertCoreTasksToSimpleTaskData(inputTasks);
        
        assertThat(result.size(), is(1));
        
        assertThat(result.get(0).getDescription(), is("A Conversion Test Task"));
        assertThat(result.get(0).getStartDate(), is("05/01/14"));
        assertThat(result.get(0).getForeseenEndDate(), is("07/01/14"));
        assertThat(result.get(0).getForeseenWorkHours(), is(30));
        assertThat(result.get(0).getSource(), is("CCC"));
        assertThat(result.get(0).getApplication(), is("task_control"));
        assertThat(result.get(0).getOwners(), is("John Programmer, Mary Developer"));
    }
}
