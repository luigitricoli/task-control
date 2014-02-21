package br.com.egs.task.control.core.entities;

import com.google.gson.JsonParseException;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TaskTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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

    @Test
    public void toJson_noPosts() throws Exception {
        Task t = createTestTask();
        t.setPosts(null);

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
                "]" +
        "}";

        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void jsonToTask_allAttributesSet() throws Exception {
        String jsonString = "{" +
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
                "]" +
        "}";

        Task t = Task.fromJson(jsonString);
        assertEquals("111122223333aaaabbbbcccc", t.getId());
        assertEquals("Test the Task Implementation", t.getDescription());
        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), t.getStartDate());
        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), t.getForeseenEndDate());
        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), t.getEndDate());
        assertEquals("Sup.Producao", t.getSource());
        assertEquals("OLM", t.getApplication().getName());
        assertEquals(2, t.getOwners().size());
        assertEquals("john", t.getOwners().get(0).getLogin());
        assertEquals("mary", t.getOwners().get(1).getLogin());
    }

    @Test
    public void jsonToTask_nullAttributes() throws Exception {
        String jsonString = "{}";

        Task t = Task.fromJson(jsonString);
        assertNull(t.getId());
        assertNull(t.getDescription());
        assertNull(t.getStartDate());
        assertNull(t.getForeseenEndDate());
        assertNull(t.getEndDate());
        assertNull(t.getSource());
        assertNull(t.getApplication());
        assertNull(t.getOwners());
    }

    @Test
    public void jsonToTask_invalidDate() throws Exception {
        String jsonString = "{startDate: 'WRONG'}";

        try {
            Task.fromJson(jsonString);
            fail("Exception was expected");
        } catch (JsonParseException e) {
            assertTrue(e.getCause() instanceof ParseException);
        }
    }

    @Test
    public void fromTaskToDbObject_id() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();
        assertEquals(new ObjectId("111122223333aaaabbbbcccc"), dbTask.get("_id"));
    }

    @Test
    public void fromTaskToDbObject_nullId() throws Exception {
        Task t = createTestTask();
        t.setId(null); // Simulate a new record.
        BasicDBObject dbTask = t.toDbObject();
        assertFalse(dbTask.containsKey((Object)"_id"));
    }

    @Test
    public void fromTaskToDbObject_description() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();
        assertEquals("Test the Task Implementation", dbTask.get("description"));
    }

    @Test
    public void fromTaskToDbObject_startDate() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), dbTask.get("startDate"));
    }

    @Test
    public void fromTaskToDbObject_foreseenEndDate() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), dbTask.get("foreseenEndDate"));
    }

    @Test
    public void fromTaskToDbObject_endDate() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), dbTask.get("endDate"));
    }
    @Test
    public void fromTaskToDbObject_nullEndDate() throws Exception {
        Task t = createTestTask();
        t.setEndDate(null); // Unfinished task
        BasicDBObject dbTask = t.toDbObject();

        assertFalse(dbTask.containsKey((Object) "endDate"));
    }

    @Test
    public void fromTaskToDbObject_source() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        assertEquals("Sup.Producao", dbTask.get("source"));
    }

    @Test
    public void fromTaskToDbObject_application() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        assertEquals("OLM", ((BasicDBObject)dbTask.get("application")).get("name"));
    }

    @Test
    public void fromTaskToDbObject_owners() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        List<BasicDBObject> owners = (List<BasicDBObject>) dbTask.get("owners");

        assertEquals(2, owners.size());
        assertEquals("john", owners.get(0).get("login"));
        assertEquals("mary", owners.get(1).get("login"));
    }

    @Test
    public void fromTaskToDbObject_posts() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = t.toDbObject();

        List<BasicDBObject> posts = (List<BasicDBObject>) dbTask.get("posts");

        assertEquals(2, posts.size());

        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), posts.get(0).get("timestamp"));
        assertEquals("john", posts.get(0).get("user"));
        assertEquals("Scope changed. No re-scheduling will be necessary", posts.get(0).get("text"));

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), posts.get(1).get("timestamp"));
        assertEquals("john", posts.get(1).get("user"));
        assertEquals("Doing #overtime to finish it sooner", posts.get(1).get("text"));

    }

    @Test
    public void fromTaskToDbObject_noPosts() throws Exception {
        Task t = createTestTask();
        t.setPosts(null);
        BasicDBObject dbTask = t.toDbObject();

        List<BasicDBObject> posts = (List<BasicDBObject>) dbTask.get("posts");

        assertTrue(posts.isEmpty());
    }

    @Test
    public void fromDbObjectToTask_id() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals("111122223333aaaabbbbcccc", task.getId());
    }

    @Test
    public void fromDbObjectToTask_description() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals("Test the Task Implementation", task.getDescription());
    }

    @Test
    public void fromDbObjectToTask_startDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), task.getStartDate());
    }

    @Test
    public void fromDbObjectToTask_foreseenEndDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), task.getForeseenEndDate());
    }

    @Test
    public void fromDbObjectToTask_endDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), task.getEndDate());
    }

    @Test
    public void fromDbObjectToTask_nullEndDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        dbTask.remove("endDate");

        Task task = Task.fromDbObject(dbTask);
        assertNull(task.getEndDate());
    }

    @Test
    public void fromDbObjectToTask_source() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals("Sup.Producao", task.getSource());
    }

    @Test
    public void fromDbObjectToTask_application() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals(new Application("OLM"), task.getApplication());
    }

    @Test
    public void fromDbObjectToTask_owners() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);

        assertEquals(2, task.getOwners().size());
        assertEquals(new TaskOwner("john"), task.getOwners().get(0));
        assertEquals(new TaskOwner("mary"), task.getOwners().get(1));
    }

    @Test
    public void fromDbObjectToTask_posts() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);

        assertEquals(2, task.getPosts().size());

        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), task.getPosts().get(0).getTimestamp());
        assertEquals("john", task.getPosts().get(0).getUser());
        assertEquals("Scope changed. No re-scheduling will be necessary", task.getPosts().get(0).getText());

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), task.getPosts().get(1).getTimestamp());
        assertEquals("john", task.getPosts().get(1).getUser());
        assertEquals("Doing #overtime to finish it sooner", task.getPosts().get(1).getText());
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

        Post p1 = new Post("john", "Scope changed. No re-scheduling will be necessary",
                timestampFormat.parse("2014-01-03 09:15:30.700"));
        t.addPost(p1);

        Post p2 = new Post("john", "Doing #overtime to finish it sooner",
                timestampFormat.parse("2014-01-08 18:20:49.150"));
        t.addPost(p2);

        return t;
    }


    private BasicDBObject createTestTaskAsDbObject() throws ParseException {
        BasicDBObject t = new BasicDBObject();

        t.append("_id", new ObjectId("111122223333aaaabbbbcccc"));
        t.append("description", "Test the Task Implementation");

        t.append("startDate", timestampFormat.parse("2014-01-02 00:00:00.000"));
        t.append("foreseenEndDate", timestampFormat.parse("2014-01-10 23:59:59.999"));
        t.append("endDate", timestampFormat.parse("2014-01-09 23:59:59.999"));

        t.append("source", "Sup.Producao");
        t.append("application", new BasicDBObject("name", "OLM"));

        List<BasicDBObject> owners = new ArrayList<>();
        owners.add(new BasicDBObject("login", "john"));
        owners.add(new BasicDBObject("login", "mary"));
        t.append("owners", owners);

        List<BasicDBObject> posts = new ArrayList<>();
        posts.add(new BasicDBObject()
                .append("timestamp", timestampFormat.parse("2014-01-03 09:15:30.700"))
                .append("user", "john")
                .append("text", "Scope changed. No re-scheduling will be necessary")
        );
        posts.add(new BasicDBObject()
                .append("timestamp", timestampFormat.parse("2014-01-08 18:20:49.150"))
                .append("user", "john")
                .append("text", "Doing #overtime to finish it sooner")
        );
        t.append("posts", posts);

        return t;
    }
}
