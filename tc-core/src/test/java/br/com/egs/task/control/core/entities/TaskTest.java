package br.com.egs.task.control.core.entities;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class TaskTest {
    @Test
    public void toJson() throws Exception {
        Task t = createTestTask();

        String json = t.toJson();

        String expectedJson = "{" +
                "id: '111122223333aaaabbbbcccc'," +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "endDate: '2014-01-09'," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'}," +
                "           {login: 'mary'}" +
                "]," +
                "posts: [" +
                "           {timestamp: '2014-01-03 09:15:30'," +
                "            user: 'john'," +
                "            text: 'Scope changed. No re-scheduling will be necessary'}," +
                "           {timestamp: '2014-01-08 18:20:49'," +
                "            user: 'john'," +
                "            text: 'Doing #overtime to finish it sooner'}" +
                "]" +
        "}";

        JSONAssert.assertEquals(expectedJson, json, true);
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
