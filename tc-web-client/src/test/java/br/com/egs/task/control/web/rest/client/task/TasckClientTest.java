package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
import br.com.egs.task.control.web.rest.client.Response;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
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

        TaskRepository repo = new TaskClient(new FilterFormat(), client, new User());
        List<Week> weeks = repo.weeksBy(1);

        assertThat(weeks.size(), is(6));

    }

}
