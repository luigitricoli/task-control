package br.com.egs.task.control.web.rest.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import br.com.egs.task.control.web.model.Hashtags;
import br.com.egs.task.control.web.model.OneWeekTask;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.stage.Stage;
import br.com.egs.task.control.web.rest.client.task.TaskClient;

public class TasckClientTest {

    @Test
    public void numberOfWeeks(){
        String json = "[]";
        JsonClient client = mock(JsonClient.class);
        when(client.at("tasks")).thenReturn(client);
        when(client.addUrlParam("year", "2014")).thenReturn(client);
        when(client.addUrlParam("month", "1")).thenReturn(client);
        when(client.getAsJson()).thenReturn(json);

        TaskRepository repo = new TaskClient(client);
        List<Week> weeks = repo.weeksByMonth(1);

        assertThat(weeks.size(), is(6));

    }

    @Test
    public void oneWeekTaskDoing() throws ParseException{
        String json = "[{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}]";
        JsonClient client = mock(JsonClient.class);
        when(client.at("tasks")).thenReturn(client);
        when(client.addUrlParam("year", "2014")).thenReturn(client);
        when(client.addUrlParam("month", "1")).thenReturn(client);
        when(client.getAsJson()).thenReturn(json);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        today.setTime(format.parse("2014-01-08"));
        
        TaskRepository repo = new TaskClient(client, today);
        List<Week> weeks = repo.weeksByMonth(1);

        assertThat(weeks.get(1).size(), is(1));

        OneWeekTask expected =  new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 3, Stage.DOING, "My First CoreTask", new HashMap<Integer, Hashtags>());
        assertThat(weeks.get(1).iterator().next(), equalTo(expected));
    }
       
    @Test
    public void moreThaOneWeekTaskDoing(){
        String json = "[{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-10\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}]";
        JsonClient client = mock(JsonClient.class);
        when(client.at("tasks")).thenReturn(client);
        when(client.addUrlParam("year", "2014")).thenReturn(client);
        when(client.addUrlParam("month", "1")).thenReturn(client);
        when(client.getAsJson()).thenReturn(json);

        TaskRepository repo = new TaskClient(client);
        List<Week> weeks = repo.weeksByMonth(1);

        assertThat(weeks.get(1).size(), is(1));

        OneWeekTask expected =  new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>());
        assertThat(weeks.get(1).iterator().next(), equalTo(expected));
    }

    @Test
    public void oneWeekTaskFinishedEarly(){
        String json = "[{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-09\",\"foreseenEndDate\":\"2014-01-10\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}]";
        JsonClient client = mock(JsonClient.class);
        when(client.at("tasks")).thenReturn(client);
        when(client.addUrlParam("year", "2014")).thenReturn(client);
        when(client.addUrlParam("month", "1")).thenReturn(client);
        when(client.getAsJson()).thenReturn(json);

        TaskRepository repo = new TaskClient(client);
        List<Week> weeks = repo.weeksByMonth(1);

        assertThat(weeks.get(1).size(), is(1));

        OneWeekTask expected = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 4, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>());
        assertThat(weeks.get(1).iterator().next(), equalTo(expected));
    }

    @Test
    public void moreThaOneWeekTaskFinished(){
        String json = "[{\"id\":\"52f518377cf06f3be158a352\",\"description\":\"My First CoreTask\",\"startDate\":\"2014-01-06\",\"endDate\":\"2014-01-14\",\"foreseenEndDate\":\"2014-01-16\",\"source\":\"CCC\",\"application\":\"OLM\",\"owners\":[{\"login\":\"john\"},{\"login\":\"mary\"}],\"posts\":[{\"timestamp\":\"2014-01-03 09:15:30\",\"user\":\"john\",\"text\":\"Scope changed. No re-scheduling will be necessary\"},{\"timestamp\":\"2014-01-08 18:20:49\",\"user\":\"john\",\"text\":\"Doing #overtime to finish it sooner\"}]}]";
        JsonClient client = mock(JsonClient.class);
        when(client.at("tasks")).thenReturn(client);
        when(client.addUrlParam("year", "2014")).thenReturn(client);
        when(client.addUrlParam("month", "1")).thenReturn(client);
        when(client.getAsJson()).thenReturn(json);

        TaskRepository repo = new TaskClient(client);
        List<Week> weeks = repo.weeksByMonth(1);

        OneWeekTask first = new OneWeekTask("52f518377cf06f3be158a352", 2, 5, 5, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>());
        OneWeekTask second = new OneWeekTask("52f518377cf06f3be158a352", 2, 4, 2, Stage.FINISHED, "My First CoreTask", new HashMap<Integer, Hashtags>());

        assertThat(weeks.get(1).iterator().next(), equalTo(first));
        assertThat(weeks.get(2).iterator().next(), equalTo(second));
    }

}
