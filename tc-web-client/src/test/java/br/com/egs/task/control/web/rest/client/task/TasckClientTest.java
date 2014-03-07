package br.com.egs.task.control.web.rest.client.task;

import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.rest.client.JsonClient;
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
        when(client.getAsJson()).thenReturn(json);

        TaskRepository repo = new TaskClient(new FilterFormat(), client);
        List<Week> weeks = repo.weeksBy(1);

        assertThat(weeks.size(), is(6));

    }

}
