package br.com.egs.task.control.core.integration;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.TasksRepository;
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

    private static final String TODAY = "2014-01-11 15:15:15.000";

	private TasksRepository repository;
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
        repository.get("ZZZ");
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
                .ownerLogins("bob");
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(2, result.size());

        assertEquals("111122223333aaaabbbbccc3", result.get(0).getId());
        assertEquals("111122223333aaaabbbbccc4", result.get(1).getId());
    }

    @Test
    public void searchByMonthAndOwner() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .month(2013, 12)
                .ownerLogins("bob");
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(1, result.size());

        assertEquals("111122223333aaaabbbbccc3", result.get(0).getId());
    }

    @Test
    public void searchByApplication() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .applications("TaskControl");
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

        Task t = new Task(
                null,
                "Testing insert",
                new Date(),
                new Date(),
                null,
                30,
                "CCC",
                new Application("OLM"),
                Arrays.asList(new TaskOwner("joe", "Joe The Programmer", "N1")));

        t = repository.add(t);

        assertEquals(5, collection.count());
        assertFalse(StringUtils.isBlank(t.getId()));
    }

    @Test
    public void updateTask() throws ParseException {
        DBCollection collection = conn.getCollection("tasks");

        // Assume that the object returned by createTestTask() was persisted during test SetUp
        Task task = Task.fromDbObject(createTestTask());

        // Creating a clone, with some changes
        Task modified = new Task(
                task.getId(),
                "Modified task",
                task.getStartDate(),
                task.getForeseenEndDate(),
                task.getEndDate(),
                30,
                task.getSource(),
                task.getApplication(),
                task.getOwners()
        );
        for (Post p : task.getPosts()) {
            modified.addPost(p);
        }
        modified.addPost(new Post("bob", "Posting into a modified task", new Date()));

        /////////////////////////////
        repository.update(modified);

        /////////////////////////////////////////////
        assertEquals(4, collection.count());

        BasicDBObject modifiedObjectFilter = new BasicDBObject("_id", new ObjectId("111122223333aaaabbbbccc1"));
        BasicDBObject dbObject = (BasicDBObject) collection.findOne(modifiedObjectFilter);

        assertEquals("Modified task", dbObject.get("description"));
        assertEquals(3, ((List)dbObject.get("posts")).size());
    }

    @Test
    public void removeTask() {
        Task t = new Task("111122223333aaaabbbbccc1", null, null, null, null, 0, null, null, null);

        // Precondition
        assertNotNull(conn.getCollection("tasks").findOne(new BasicDBObject("_id", new ObjectId(t.getId()))));

        repository.remove(t);

        assertNull(conn.getCollection("tasks").findOne(new BasicDBObject("_id", new ObjectId(t.getId()))));
    }

	private void populateDatabase() throws Exception {
        BasicDBObject t = createTestTask();
        conn.getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc2"))
            .append("startDate", timestampFormat.parse("2013-12-05 00:00:00.000"))
            .append("foreseenEndDate", timestampFormat.parse("2013-12-25 00:00:00.000"))
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
                    new BasicDBObject()
                            .append("login", "bob")
                            .append("name", "Bob")
                            .append("type", "N1")
                            .append("workDays", new ArrayList<>()),
                    new BasicDBObject()
                            .append("login", "buzz")
                            .append("name", "Buzz")
                            .append("type", "N2")
                            .append("workDays", new ArrayList<>())))
        ;
        conn.getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc4"))
            .append("startDate", timestampFormat.parse("2014-02-01 00:00:00.000"))
            .append("foreseenEndDate", timestampFormat.parse("2014-02-07 23:59:59.999"))
            .append("owners", Arrays.asList(
                    new BasicDBObject()
                            .append("login", "bob")
                            .append("name", "Bob")
                            .append("type", "N1")
                            .append("workDays", new ArrayList<>())))
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

        t.append("foreseenWorkHours", 8);

        t.append("source", "Sup.Producao");
        t.append("application", new BasicDBObject("name", "OLM"));

        List<BasicDBObject> owners = new ArrayList<>();
        owners.add(new BasicDBObject()
                .append("login", "john")
                .append("name", "Joe The Programmer")
                .append("type", "N1")
                .append("workDays", Arrays.asList(
                        new BasicDBObject()
                        .append("day", "2014-01-02")
                        .append("hours", 8)
                )));
        owners.add(new BasicDBObject()
                .append("login", "mary")
                .append("name", "Mary Developer")
                .append("type", "N2")
                .append("workDays", new ArrayList<>()));
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
     * @param task Task to validate
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
        assertEquals("john", task.getOwners().get(0).getLogin());
        assertEquals("Joe The Programmer", task.getOwners().get(0).getName());
        assertEquals("N1", task.getOwners().get(0).getType());
        assertEquals(1, task.getOwners().get(0).getWorkDays().size());
        assertEquals("2014-01-02", task.getOwners().get(0).getWorkDays().get(0).getDay());
        assertEquals(8, task.getOwners().get(0).getWorkDays().get(0).getHours());

        assertEquals("mary", task.getOwners().get(1).getLogin());
        assertEquals("Mary Developer", task.getOwners().get(1).getName());
        assertEquals("N2", task.getOwners().get(1).getType());

        assertEquals(2, task.getPosts().size());
        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), task.getPosts().get(0).getTimestamp());
        assertEquals("john", task.getPosts().get(0).getUser());
        assertEquals("Scope changed. No re-scheduling will be necessary", task.getPosts().get(0).getText());

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), task.getPosts().get(1).getTimestamp());
        assertEquals("john", task.getPosts().get(1).getUser());
        assertEquals("Doing #overtime to finish it sooner", task.getPosts().get(1).getText());
    }
}
