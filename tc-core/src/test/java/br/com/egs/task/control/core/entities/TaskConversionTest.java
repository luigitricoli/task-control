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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the conversions implemented by the Task class
 */
public class TaskConversionTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void toJson() throws Exception {
        Task t = createTestTask(false, false, false);

        String json = t.toJson();

        String expectedJson = "{" +
                "id: '111122223333aaaabbbbcccc'," +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "foreseenWorkHours: 50," +
                "endDate: '2014-01-09'," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'," +
                "            name: 'John Programmer'," +
                "            type: 'N1'," +
                "            workDays: [" +
                "                {" +
                "                   day: '2014-01-03'," +
                "                   hours: 8" +
                "                }" +
                "            ]}," +
                "           {login: 'mary'," +
                "            name: 'Mary Devs'," +
                "            type: 'N2'," +
                "            workDays: []}" +
                "]," +
                "posts: [" +
                "           {timestamp: '2014-01-03 09:15:30'," +
                "            login: 'john'," +
                "            name: 'John The Programmer'," +
                "            text: 'Scope changed. No re-scheduling will be necessary'}," +
                "           {timestamp: '2014-01-08 18:20:49'," +
                "            login: 'john'," +
                "            name: 'John The Programmer'," +
                "            text: 'Doing #overtime to finish it sooner'}" +
                "]" +
                "}";

        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void toJson_noPosts() throws Exception {
        Task t = createTestTask(false, false, true);

        String json = t.toJson();

        String expectedJson = "{" +
                "id: '111122223333aaaabbbbcccc'," +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "foreseenWorkHours: 50," +
                "endDate: '2014-01-09'," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'," +
                "            name: 'John Programmer'," +
                "            type: 'N1'," +
                "            workDays: [" +
                "                {" +
                "                   day: '2014-01-03'," +
                "                   hours: 8" +
                "                }" +
                "            ]}," +
                "           {login: 'mary'," +
                "            name: 'Mary Devs'," +
                "            type: 'N2'," +
                "            workDays: []}" +
                "]" +
                "}";

        JSONAssert.assertEquals(expectedJson, json, true);
    }

    @Test
    public void jsonToTask_allAttributesSet() throws Exception {
        String jsonString = "{task: {" +
                "id: '111122223333aaaabbbbcccc'," +
                "description: 'Test the Task Implementation'," +
                "startDate: '2014-01-02'," +
                "foreseenEndDate: '2014-01-10'," +
                "foreseenWorkHours: 50," +
                "endDate: '2014-01-09'," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'," +
                "            name: 'John Programmer'," +
                "            type: 'N1'," +
                "            workDays: [" +
                "                {" +
                "                   day: '2014-01-03'," +
                "                   hours: 8" +
                "                }" +
                "            ]}," +
                "           {login: 'mary'," +
                "            name: 'Mary Devs'," +
                "            type: 'N2'," +
                "            workDays: []}" +
                "]" +
                "}}";

        Task t = Task.fromJson(jsonString);
        assertEquals("111122223333aaaabbbbcccc", t.getId());
        assertEquals("Test the Task Implementation", t.getDescription());
        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), t.getStartDate());
        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), t.getForeseenEndDate());
        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), t.getEndDate());
        assertEquals(50, t.getForeseenWorkHours().intValue());
        assertEquals("Sup.Producao", t.getSource());
        assertEquals("OLM", t.getApplication().getName());

        assertEquals(2, t.getOwners().size());

        assertEquals("john", t.getOwners().get(0).getLogin());
        assertEquals("John Programmer", t.getOwners().get(0).getName());
        assertEquals("N1", t.getOwners().get(0).getType());
        assertEquals(1, t.getOwners().get(0).getWorkDays().size());
        assertEquals("2014-01-03", t.getOwners().get(0).getWorkDays().get(0).getDay());
        assertEquals(8, t.getOwners().get(0).getWorkDays().get(0).getHours());

        assertEquals("mary", t.getOwners().get(1).getLogin());
        assertEquals("Mary Devs", t.getOwners().get(1).getName());
        assertEquals("N2", t.getOwners().get(1).getType());
        assertEquals(0, t.getOwners().get(1).getWorkDays().size());
    }

    @Test
    public void jsonToTask_nullAttributes() throws Exception {
        String jsonString = "{task:{}}";

        Task t = Task.fromJson(jsonString);
        assertNull(t.getId());
        assertNull(t.getDescription());
        assertNull(t.getStartDate());
        assertNull(t.getForeseenEndDate());
        assertNull(t.getForeseenWorkHours());
        assertNull(t.getEndDate());
        assertNull(t.getSource());
        assertNull(t.getApplication());
        assertNull(t.getOwners());
    }

    @Test
    public void jsonToTask_invalidDate() throws Exception {
        String jsonString = "{task:{startDate: 'WRONG'}}";

        try {
            Task.fromJson(jsonString);
            fail("Exception was expected");
        } catch (JsonParseException e) {
            assertTrue(e.getCause() instanceof ParseException);
        }
    }

    @Test
    public void fromTaskToDbObject_id() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();
        assertEquals(new ObjectId("111122223333aaaabbbbcccc"), dbTask.get("_id"));
    }

    @Test
    public void fromTaskToDbObject_nullId() throws Exception {
        Task t = createTestTask(true, false, false);

        BasicDBObject dbTask = t.toDbObject();
        assertFalse(dbTask.containsKey((Object)"_id"));
    }

    @Test
    public void fromTaskToDbObject_description() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();
        assertEquals("Test the Task Implementation", dbTask.get("description"));
    }

    @Test
    public void fromTaskToDbObject_startDate() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), dbTask.get("startDate"));
    }

    @Test
    public void fromTaskToDbObject_foreseenEndDate() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), dbTask.get("foreseenEndDate"));
    }

    @Test
    public void fromTaskToDbObject_foreseenWorkHours() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(50, dbTask.get("foreseenWorkHours"));
    }

    @Test
    public void fromTaskToDbObject_endDate() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), dbTask.get("endDate"));
    }
    @Test
    public void fromTaskToDbObject_nullEndDate() throws Exception {
        Task t = createTestTask(false, true, false); // Null End Date = Unfinished task
        BasicDBObject dbTask = t.toDbObject();

        assertFalse(dbTask.containsKey((Object) "endDate"));
    }

    @Test
    public void fromTaskToDbObject_source() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        assertEquals("Sup.Producao", dbTask.get("source"));
    }

    @Test
    public void fromTaskToDbObject_application() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        assertEquals("OLM", ((BasicDBObject)dbTask.get("application")).get("name"));
    }

    @Test
    public void fromTaskToDbObject_owners() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        List<BasicDBObject> owners = (List<BasicDBObject>) dbTask.get("owners");

        assertEquals(2, owners.size());
        assertEquals("john", owners.get(0).get("login"));
        assertEquals("John Programmer", owners.get(0).get("name"));
        assertEquals("N1", owners.get(0).get("type"));

        List<BasicDBObject> owner1Workdays = (List<BasicDBObject>) owners.get(0).get("workDays");
        assertEquals(1, owner1Workdays.size());
        assertEquals("2014-01-03", owner1Workdays.get(0).get("day"));
        assertEquals(8, owner1Workdays.get(0).get("hours"));

        assertEquals("mary", owners.get(1).get("login"));
        assertEquals("Mary Devs", owners.get(1).get("name"));
        assertEquals("N2", owners.get(1).get("type"));

        List<BasicDBObject> owner2Workdays = (List<BasicDBObject>) owners.get(1).get("workDays");
        assertEquals(0, owner2Workdays.size());
    }

    @Test
    public void fromTaskToDbObject_posts() throws Exception {
        Task t = createTestTask(false, false, false);
        BasicDBObject dbTask = t.toDbObject();

        List<BasicDBObject> posts = (List<BasicDBObject>) dbTask.get("posts");

        assertEquals(2, posts.size());

        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), posts.get(0).get("timestamp"));
        assertEquals("john", posts.get(0).get("login"));
        assertEquals("John The Programmer", posts.get(0).get("name"));
        assertEquals("Scope changed. No re-scheduling will be necessary", posts.get(0).get("text"));

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), posts.get(1).get("timestamp"));
        assertEquals("john", posts.get(1).get("login"));
        assertEquals("John The Programmer", posts.get(1).get("name"));
        assertEquals("Doing #overtime to finish it sooner", posts.get(1).get("text"));

    }

    @Test
    public void fromTaskToDbObject_noPosts() throws Exception {
        Task t = createTestTask(false, false, true);

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
    public void fromDbObjectToTask_foreseenWorkHours() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);
        assertEquals(50, task.getForeseenWorkHours().intValue());
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
        Task t = Task.fromDbObject(dbTask);

        assertEquals(2, t.getOwners().size());

        assertEquals("john", t.getOwners().get(0).getLogin());
        assertEquals("John Programmer", t.getOwners().get(0).getName());
        assertEquals("N1", t.getOwners().get(0).getType());
        assertEquals(1, t.getOwners().get(0).getWorkDays().size());
        assertEquals("2014-01-03", t.getOwners().get(0).getWorkDays().get(0).getDay());
        assertEquals(8, t.getOwners().get(0).getWorkDays().get(0).getHours());

        assertEquals("mary", t.getOwners().get(1).getLogin());
        assertEquals("Mary Devs", t.getOwners().get(1).getName());
        assertEquals("N2", t.getOwners().get(1).getType());
        assertEquals(0, t.getOwners().get(1).getWorkDays().size());
    }

    @Test
    public void fromDbObjectToTask_posts() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = Task.fromDbObject(dbTask);

        assertEquals(2, task.getPosts().size());

        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), task.getPosts().get(0).getTimestamp());
        assertEquals("john", task.getPosts().get(0).getLogin());
        assertEquals("John The Programmer", task.getPosts().get(0).getName());
        assertEquals("Scope changed. No re-scheduling will be necessary", task.getPosts().get(0).getText());

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), task.getPosts().get(1).getTimestamp());
        assertEquals("Doing #overtime to finish it sooner", task.getPosts().get(1).getText());
    }

    private Task createTestTask(boolean nullId, boolean nullEndDate, boolean nullPosts) throws Exception {
        DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Task t = new Task(
                nullId ? null : "111122223333aaaabbbbcccc",
                "Test the Task Implementation",

                timestampFormat.parse("2014-01-02 00:00:00.000"),
                timestampFormat.parse("2014-01-10 23:59:59.999"),
                nullEndDate ? null : timestampFormat.parse("2014-01-09 23:59:59.999"),
                50,

                "Sup.Producao",
                new Application("OLM"),

                Arrays.asList(new TaskOwner("john", "John Programmer", "N1").addWorkHours("2014-01-03", 8),
                                new TaskOwner("mary", "Mary Devs", "N2")));

        if (!nullPosts) {
            Post p1 = new Post("john", "John The Programmer", "Scope changed. No re-scheduling will be necessary",
                    timestampFormat.parse("2014-01-03 09:15:30.700"));
            t.addPost(p1);

            Post p2 = new Post("john", "John The Programmer", "Doing #overtime to finish it sooner",
                    timestampFormat.parse("2014-01-08 18:20:49.150"));
            t.addPost(p2);
        }

        return t;
    }

    private BasicDBObject createTestTaskAsDbObject() throws ParseException {
        BasicDBObject t = new BasicDBObject();

        t.append("_id", new ObjectId("111122223333aaaabbbbcccc"));
        t.append("description", "Test the Task Implementation");

        t.append("startDate", timestampFormat.parse("2014-01-02 00:00:00.000"));
        t.append("foreseenEndDate", timestampFormat.parse("2014-01-10 23:59:59.999"));
        t.append("foreseenWorkHours", 50);
        t.append("endDate", timestampFormat.parse("2014-01-09 23:59:59.999"));

        t.append("source", "Sup.Producao");
        t.append("application", new BasicDBObject("name", "OLM"));

        List<BasicDBObject> owners = new ArrayList<>();
        owners.add(new BasicDBObject()
                .append("login", "john")
                .append("name", "John Programmer")
                .append("type", "N1")
                .append("workDays", Arrays.asList(
                        new BasicDBObject()
                                .append("day", "2014-01-03")
                                .append("hours", 8)
                )));
        owners.add(new BasicDBObject()
                .append("login", "mary")
                .append("name", "Mary Devs")
                .append("type", "N2")
                .append("workDays", Collections.emptyList()));
        t.append("owners", owners);

        List<BasicDBObject> posts = new ArrayList<>();
        posts.add(new BasicDBObject()
                .append("timestamp", timestampFormat.parse("2014-01-03 09:15:30.700"))
                .append("login", "john")
                .append("name", "John The Programmer")
                .append("text", "Scope changed. No re-scheduling will be necessary")
        );
        posts.add(new BasicDBObject()
                .append("timestamp", timestampFormat.parse("2014-01-08 18:20:49.150"))
                .append("login", "john")
                .append("name", "John The Programmer")
                .append("text", "Doing #overtime to finish it sooner")
        );
        t.append("posts", posts);

        return t;
    }
}
