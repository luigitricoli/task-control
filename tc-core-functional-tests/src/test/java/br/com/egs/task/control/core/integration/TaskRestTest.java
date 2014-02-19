package br.com.egs.task.control.core.integration;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaskRestTest {

    private MongoDbConnection conn;

    @Before
    public void setUp() throws Exception {
        conn = TestConnectionFactory.getConnection();
        populateDatabase();
    }

    @After
    public void tearDown() {
        conn.getDatabase().getCollection("tasks").drop();
        conn.close();
    }

	@Test
	public void listTasks() throws Exception {
		RestClient restfulie = Restfulie.custom();
		Response response = restfulie.at("http://localhost:8090/v1/tasks?year=2014&month=1").accept("application/json").get();

        String content = response.getContent();

        assertEquals(200, response.getCode());
        JSONAssert.assertEquals("[{id:'111122223333aaaabbbbccf1'}]", content, false);
        assertTrue(content.contains("posts"));
	}

    @Test
    public void listTasksWithDetailedFilters() throws Exception {
        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at(
                "http://localhost:8090/v1/tasks?year=2014&month=1" +
                        "&owner=john" +
                        "&application=OLM" +
                        "&status=finished" +
                        "&sources=Sup.Producao,CCC" +
                        "&excludePosts=true")
                .accept("application/json").get();

        String content = response.getContent();

        assertEquals(200, response.getCode());
        assertFalse(content.contains("posts"));
    }

    private void populateDatabase() throws Exception {
        BasicDBObject t = createTestTask();
        conn.getDatabase().getCollection("tasks").insert(t);
    }

    private BasicDBObject createTestTask() throws ParseException {
        DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        BasicDBObject t = new BasicDBObject();

        t.append("_id", new ObjectId("111122223333aaaabbbbccf1"));
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
