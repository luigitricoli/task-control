package br.com.egs.task.control.core.database.mapper;

import br.com.egs.task.control.core.entities.*;
import com.mongodb.BasicDBObject;
import static org.junit.Assert.*;

import org.bson.types.ObjectId;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class TaskMapperTest {

    private TaskMapper mapper = new TaskMapper();

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void fromTaskToDbObject_id() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);
        assertEquals(new ObjectId("111122223333aaaabbbbcccc"), dbTask.get("_id"));
    }

    @Test
    public void fromTaskToDbObject_nullId() throws Exception {
        Task t = createTestTask();
        t.setId(null); // Simulate a new record.
        BasicDBObject dbTask = mapper.getAsDbObject(t);
        assertFalse(dbTask.containsKey((Object)"_id"));
    }

    @Test
    public void fromTaskToDbObject_description() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);
        assertEquals("Test the Task Implementation", dbTask.get("description"));
    }

    @Test
    public void fromTaskToDbObject_startDate() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), dbTask.get("startDate"));
    }

    @Test
    public void fromTaskToDbObject_foreseenEndDate() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), dbTask.get("foreseenEndDate"));
    }

    @Test
    public void fromTaskToDbObject_endDate() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), dbTask.get("endDate"));
    }
    @Test
    public void fromTaskToDbObject_nullEndDate() throws Exception {
        Task t = createTestTask();
        t.setEndDate(null); // Unfinished task
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        assertFalse(dbTask.containsKey((Object) "endDate"));
    }

    @Test
    public void fromTaskToDbObject_source() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        assertEquals("Sup.Producao", dbTask.get("source"));
    }

    @Test
    public void fromTaskToDbObject_application() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        assertEquals("OLM", ((BasicDBObject)dbTask.get("application")).get("name"));
    }

    @Test
    public void fromTaskToDbObject_owners() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

        List<BasicDBObject> owners = (List<BasicDBObject>) dbTask.get("owners");

        assertEquals(2, owners.size());
        assertEquals("john", owners.get(0).get("user"));
        assertEquals("mary", owners.get(1).get("user"));
    }

    @Test
    public void fromTaskToDbObject_posts() throws Exception {
        Task t = createTestTask();
        BasicDBObject dbTask = mapper.getAsDbObject(t);

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
    public void fromDbObjectToTask_id() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals("111122223333aaaabbbbcccc", task.getId());
    }

    @Test
    public void fromDbObjectToTask_description() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals("Test the Task Implementation", task.getDescription());
    }

    @Test
    public void fromDbObjectToTask_startDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), task.getStartDate());
    }

    @Test
    public void fromDbObjectToTask_foreseenEndDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), task.getForeseenEndDate());
    }

    @Test
    public void fromDbObjectToTask_endDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), task.getEndDate());
    }

    @Test
    public void fromDbObjectToTask_nullEndDate() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        dbTask.remove("endDate");

        Task task = mapper.getAsTask(dbTask);
        assertNull(task.getEndDate());
    }

    @Test
    public void fromDbObjectToTask_source() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals("Sup.Producao", task.getSource());
    }

    @Test
    public void fromDbObjectToTask_application() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);
        assertEquals(new Application("OLM"), task.getApplication());
    }

    @Test
    public void fromDbObjectToTask_owners() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);

        assertEquals(2, task.getOwners().size());
        assertEquals(new TaskOwner("john"), task.getOwners().get(0));
        assertEquals(new TaskOwner("mary"), task.getOwners().get(1));
    }

    @Test
    public void fromDbObjectToTask_posts() throws Exception {
        BasicDBObject dbTask = createTestTaskAsDbObject();
        Task task = mapper.getAsTask(dbTask);

        assertEquals(2, task.getPosts().size());

        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), task.getPosts().get(0).getTimestamp());
        assertEquals("john", task.getPosts().get(0).getUser());
        assertEquals("Scope changed. No re-scheduling will be necessary", task.getPosts().get(0).getText());

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), task.getPosts().get(1).getTimestamp());
        assertEquals("john", task.getPosts().get(1).getUser());
        assertEquals("Doing #overtime to finish it sooner", task.getPosts().get(1).getText());
    }

    private Task createTestTask() throws ParseException {
        Task t = new Task();
        t.setId("111122223333aaaabbbbcccc");
        t.setDescription("Test the Task Implementation");

        t.setStartDate(dateFormat.parse("2014-01-02"));
        t.setForeseenEndDate(dateFormat.parse("2014-01-10"));
        t.setEndDate(dateFormat.parse("2014-01-09"));

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
        owners.add(new BasicDBObject("user", "john"));
        owners.add(new BasicDBObject("user", "mary"));
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
