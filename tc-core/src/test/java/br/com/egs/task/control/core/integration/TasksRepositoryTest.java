package br.com.egs.task.control.core.integration;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import br.com.egs.task.control.core.repository.impl.TasksRepositoryImpl;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TasksRepositoryTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final String TODAY = "2014-01-11 15:15:15.000";

	private Tasks repository;
	private MongoDbConnection conn;
	
	@Before
	public void setUp() throws Exception {
		conn = TestConnectionFactory.getConnection();
		populateDatabase();
		repository = new TasksRepositoryImpl(conn, timestampFormat.parse(TODAY));
	}

	@After
	public void tearDown() {
		conn.getCollection("tasks").drop();
		conn.close();
	}

    @Test
    public void getById() {
        Task task = repository.get("111122223333aaaabbbbccc1");
        assertNotNull(task);
        assertEquals("111122223333aaaabbbbccc1", task.getId());
    }

    @Test
    public void getById_notFound() {
        Task task = repository.get("999999999999999999999999");
        assertNull(task);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getById_invalid() {
        Task task = repository.get("ZZZ");
    }

    @Test
    public void searchAll() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria();
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(4, result.size());

        assertEquals("111122223333aaaabbbbccc1", result.get(0).getId());
        assertEquals("111122223333aaaabbbbccc2", result.get(1).getId());
        assertEquals("111122223333aaaabbbbccc3", result.get(2).getId());
        assertEquals("111122223333aaaabbbbccc4", result.get(3).getId());
    }

    /**
     * Checks the database-to-object mapping by performing a detailed validation
     * of a returned record.
     * @throws Exception
     */
    @Test
    public void searchAll_detailedRecordValidation() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria();
        List<Task> result = repository.searchTasks(criteria);

        Task resultSample = result.get(0);
        compareWithFirstTestRecord(resultSample);
    }

    @Test
    public void searchByMonth() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .month(2014, 1);
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(2, result.size());

        assertEquals("111122223333aaaabbbbccc1", result.get(0).getId());
        assertEquals("111122223333aaaabbbbccc2", result.get(1).getId());
    }

    @Test
    public void searchByOwner() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .ownerLogin("bob");
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(2, result.size());

        assertEquals("111122223333aaaabbbbccc3", result.get(0).getId());
        assertEquals("111122223333aaaabbbbccc4", result.get(1).getId());
    }

    @Test
    public void searchByMonthAndOwner() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .month(2013, 12)
                .ownerLogin("bob");
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(1, result.size());

        assertEquals("111122223333aaaabbbbccc3", result.get(0).getId());
    }

    @Test
    public void searchByApplication() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .application("TaskControl");
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(1, result.size());

        assertEquals("111122223333aaaabbbbccc2", result.get(0).getId());
    }

    @Test
    public void search_excludeHistory() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .excludePosts();

        List<Task> result = repository.searchTasks(criteria);

        for (Task task : result) {
              assertNull(task.getPosts());
        }
    }

    @Test
    public void searchLateTasks() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .status(TaskSearchCriteria.Status.LATE);
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(2, result.size());

        assertEquals("111122223333aaaabbbbccc2", result.get(0).getId());
        assertEquals("111122223333aaaabbbbccc3", result.get(1).getId());
    }

    @Test
    public void addTask() {
        DBCollection collection = conn.getCollection("tasks");
        assertEquals(4, collection.count());

        Task t = new Task();
        t.setId(null);
        t.setDescription("Testing insert");
        t.setStartDate(new Date());
        t.setForeseenEndDate(new Date());
        t.setEndDate(null);
        t.setSource("CCC");
        t.setApplication(new Application("OLM"));
        t.setOwners(Arrays.asList(new TaskOwner("joe")));

        t = repository.add(t);

        assertEquals(5, collection.count());
        assertFalse(StringUtils.isBlank(t.getId()));
    }

	private void populateDatabase() throws Exception {
        BasicDBObject t = createTestTask();
        conn.getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc2"))
            .append("startDate", timestampFormat.parse("2014-01-05 00:00:00.000"))
            .append("application", new BasicDBObject("name", "TaskControl"))
        ;
        t.remove("endDate");
        conn.getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc3"))
            .append("startDate", timestampFormat.parse("2013-12-15 00:00:00.000"))
            .append("foreseenEndDate", timestampFormat.parse("2013-12-20 23:59:59.999"))
            .append("endDate", timestampFormat.parse("2013-12-21 23:59:59.999"))
            .append("owners", Arrays.asList(
                    new BasicDBObject("login", "bob"),
                    new BasicDBObject("login", "buzz")))
        ;
        conn.getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc4"))
            .append("startDate", timestampFormat.parse("2014-02-01 00:00:00.000"))
            .append("foreseenEndDate", timestampFormat.parse("2014-02-07 23:59:59.999"))
            .append("owners", Arrays.asList(
                    new BasicDBObject("login", "bob")))
        ;
        t.remove("endDate");

        conn.getCollection("tasks").insert(t);
	}

    private BasicDBObject createTestTask() throws ParseException {
        BasicDBObject t = new BasicDBObject();

        t.append("_id", new ObjectId("111122223333aaaabbbbccc1"));
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

    /**
     * Validates each field of this Task against the first inserted record.
     * @param task
     * @throws ParseException
     */
    private void compareWithFirstTestRecord(Task task) throws ParseException {
        assertEquals("111122223333aaaabbbbccc1", task.getId());
        assertEquals("Test the Task Implementation", task.getDescription());

        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), task.getStartDate());
        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), task.getForeseenEndDate());
        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), task.getEndDate());

        assertEquals("Sup.Producao", task.getSource());
        assertEquals(new Application("OLM"), task.getApplication());

        assertEquals(2, task.getOwners().size());
        assertEquals(new TaskOwner("john"), task.getOwners().get(0));
        assertEquals(new TaskOwner("mary"), task.getOwners().get(1));

        assertEquals(2, task.getPosts().size());
        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), task.getPosts().get(0).getTimestamp());
        assertEquals("john", task.getPosts().get(0).getUser());
        assertEquals("Scope changed. No re-scheduling will be necessary", task.getPosts().get(0).getText());

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), task.getPosts().get(1).getTimestamp());
        assertEquals("john", task.getPosts().get(1).getUser());
        assertEquals("Doing #overtime to finish it sooner", task.getPosts().get(1).getText());
    }
}
