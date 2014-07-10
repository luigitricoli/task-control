package br.com.egs.task.control.core.integration;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
        conn.getCollection("tasks").drop();
        conn.getCollection("users").drop();
        conn.close();
    }

    @Test
    public void findTask() throws Exception {
        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/tasks/111122223333aaaabbbbccf1")
                .accept("application/json").get();

        assertEquals(200, response.getCode());

        String content = response.getContent();

        String expected = "{id: '111122223333aaaabbbbccf1'}";
        JSONAssert.assertEquals(expected, content, false);
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

    @Test
    public void createTask_ok() throws Exception {
        String jsonString = "{task: {" +
                "description: 'Test the Task Implementation'," +
                "startDate: '2017-01-02'," +
                "foreseenEndDate: '2017-01-10'," +
                "foreseenWorkHours: 8," +
                "source: 'Sup.Producao'," +
                "application: 'OLM'," +
                "owners: [" +
                "           {login: 'john'}" +
                "]" +
        "}}";

        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/tasks")
                .accept("application/json")
                .as("application/json")
                .post(jsonString);

        String content = response.getContent();

        assertEquals(200, response.getCode());

        String idAttributePattern = "\"id\" ?: ?\"[0-9a-fA-F]{24}\"";

        assertTrue(Pattern.compile(idAttributePattern).matcher(content).find());
    }


    @Test
    public void createPost() throws Exception {
        DBCollection collection = conn.getCollection("tasks");
        BasicDBObject dbFilter = new BasicDBObject("_id", new ObjectId("111122223333aaaabbbbccf1"));
        assertEquals(2, ((List)collection.findOne(dbFilter).get("posts")).size());

        String jsonString = "{post: {" +
                "login: 'mary'," +
                "name: 'Mary Developer'," +
                "text: 'Using the service to add a post'," +
                "timestamp: '2014-01-08 15:20:30' }}";

        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/tasks/111122223333aaaabbbbccf1")
                .accept("application/json")
                .as("application/json")
                .post(jsonString);

        response.getContent();

        assertEquals(200, response.getCode());

        @SuppressWarnings("unchecked")
        List<BasicDBObject> posts = (List<BasicDBObject>) collection.findOne(dbFilter).get("posts");
        assertEquals(3, posts.size());
        assertEquals("Using the service to add a post", posts.get(2).getString("text"));
    }

    @Test
    public void createTask_validationErrors() throws Exception {
        String jsonString = "{aCompletelyUnrelatedAttribute: 1}";

        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/tasks")
                .accept("application/json")
                .as("application/json")
                .post(jsonString);

        assertEquals(481, response.getCode());
    }

    private void populateDatabase() throws Exception {
        BasicDBObject t = createTestTask();
        conn.getCollection("tasks").insert(t);

        BasicDBObject u = createTestUser();
        conn.getCollection("users").insert(u);
    }

    private BasicDBObject createTestTask() throws ParseException {
        DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        BasicDBObject t = new BasicDBObject();

        t.append("_id", new ObjectId("111122223333aaaabbbbccf1"));
        t.append("description", "Test the Task Implementation");

        t.append("startDate", timestampFormat.parse("2014-01-02 00:00:00.000"));
        t.append("foreseenEndDate", timestampFormat.parse("2014-01-10 23:59:59.999"));
        t.append("endDate", timestampFormat.parse("2014-01-09 23:59:59.999"));

        t.append("foreseenWorkHours", 50);

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

    private BasicDBObject createTestUser() {
        BasicDBObject user = new BasicDBObject()
                .append("_id", "john")
                .append("name", "A Test User")
                .append("type", "N2")
                .append("applications", Arrays.asList(new BasicDBObject("name", "OLM")));
        return user;
    }
}
