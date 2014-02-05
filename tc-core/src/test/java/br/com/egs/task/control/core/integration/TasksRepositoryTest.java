package br.com.egs.task.control.core.integration;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.egs.task.control.core.database.DbConfiguration;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.Tasks;
import br.com.egs.task.control.core.repository.impl.TasksRepositoryImpl;

import com.mongodb.BasicDBObject;

public class TasksRepositoryTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	private Tasks repository;
	private MongoDbConnection conn;
	
	@Before
	public void setUp() throws Exception {
		conn = TestConnectionFactory.getConnection();
		populateDatabase();
		repository = new TasksRepositoryImpl(conn);
	}

	@After
	public void tearDown() {
		conn.getDatabase().getCollection("tasks").drop();
		conn.close();
	}

    @Test
    public void searchAll() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria();
        List<Task> result = repository.searchTasks(criteria);

        assertEquals(4, result.size());

        Task resultSample = result.get(0);

        assertEquals("111122223333aaaabbbbccc1", resultSample.getId());
        assertEquals("Test the Task Implementation", resultSample.getDescription());

        assertEquals(timestampFormat.parse("2014-01-02 00:00:00.000"), resultSample.getStartDate());
        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), resultSample.getForeseenEndDate());
        assertEquals(timestampFormat.parse("2014-01-09 23:59:59.999"), resultSample.getEndDate());

        assertEquals("Sup.Producao", resultSample.getSource());
        assertEquals(new Application("OLM"), resultSample.getApplication());

        assertEquals(2, resultSample.getOwners().size());
        assertEquals(new TaskOwner("john"), resultSample.getOwners().get(0));
        assertEquals(new TaskOwner("mary"), resultSample.getOwners().get(1));

        assertEquals(2, resultSample.getPosts().size());
        assertEquals(timestampFormat.parse("2014-01-03 09:15:30.700"), resultSample.getPosts().get(0).getTimestamp());
        assertEquals("john", resultSample.getPosts().get(0).getUser());
        assertEquals("Scope changed. No re-scheduling will be necessary", resultSample.getPosts().get(0).getText());

        assertEquals(timestampFormat.parse("2014-01-08 18:20:49.150"), resultSample.getPosts().get(1).getTimestamp());
        assertEquals("john", resultSample.getPosts().get(1).getUser());
        assertEquals("Doing #overtime to finish it sooner", resultSample.getPosts().get(1).getText());
    }

	private void populateDatabase() throws Exception {
        BasicDBObject t = createTestTask();
        conn.getDatabase().getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc2"))
            .append("startDate", timestampFormat.parse("2014-01-05 00:00:00.000"))
        ;
        conn.getDatabase().getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc3"))
            .append("startDate", timestampFormat.parse("2013-12-15 00:00:00.000"))
            .append("foreseenEndDate", timestampFormat.parse("2013-12-20 23:59:59.999"))
            .append("endDate", timestampFormat.parse("2013-12-20 23:59:59.999"))
        ;
        conn.getDatabase().getCollection("tasks").insert(t);

        t = createTestTask()
            .append("_id", new ObjectId("111122223333aaaabbbbccc4"))
            .append("startDate", timestampFormat.parse("2014-02-01 00:00:00.000"))
            .append("foreseenEndDate", timestampFormat.parse("2014-02-07 23:59:59.999"))
        ;
        t.remove("endDate");

        conn.getDatabase().getCollection("tasks").insert(t);
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
