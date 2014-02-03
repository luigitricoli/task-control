package br.com.egs.task.control.core.integration;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class UserRestTest {
    private MongoDbConnection conn;

    @Before
    public void setUp() {
        conn = TestConnectionFactory.getConnection();
        populateDatabase();
    }

    @After
    public void tearDown() {
        conn.getDatabase().getCollection("users").drop();
        conn.close();
    }

    @Test
    public void listAllUsers() throws JSONException {
        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/users").accept("application/json").get();

        String expected = "[" +
                "{" +
                    "'login':'testusr'," +
                    "'name':'A Test User'," +
                    "'email':'test@example.com'," +
                    "'passwordHash':'AAAAABBBBBCCCCCDDDDDEEEEE'," +
                    "'applications':[" +
                        "{'name':'OLM'},{'name':'TaskControl'}" +
                    "]" +
                "},{" +
                    "'login':'aseconduser'," +
                    "'name':'Another Test User'," +
                    "'email':'other@example.com'," +
                    "'passwordHash':'ZZZZZZZZYYYYYYYYXXXXXXXX'," +
                    "'applications':[" +
                        "{'name':'EMM'}" +
                    "]" +
                "}" +
                "]";

        assertEquals(200, response.getCode());
        JSONAssert.assertEquals(expected, response.getContent(), true);
    }

    @Test
    public void getByLogin() throws JSONException {
        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/users/testusr").accept("application/json").get();

        String expected = "{" +
                "'login':'testusr'," +
                "'name':'A Test User'," +
                "'email':'test@example.com'," +
                "'passwordHash':'AAAAABBBBBCCCCCDDDDDEEEEE'," +
                "'applications':[" +
                   "{'name':'OLM'},{'name':'TaskControl'}" +
                "]" +
        "}";

        assertEquals(200, response.getCode());
        JSONAssert.assertEquals(expected, response.getContent(), true);
    }

    @Test
    public void getNonExistingByLogin() throws JSONException {
        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/users/doesnotexist").accept("application/json").get();
        assertEquals(404, response.getCode());
    }

    /**
     *
     */
    public void populateDatabase() {
        BasicDBObject user = new BasicDBObject()
                .append("_id", "testusr")
                .append("name", "A Test User")
                .append("email", "test@example.com")
                .append("passwordHash", "AAAAABBBBBCCCCCDDDDDEEEEE");

        BasicDBList applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "OLM"));
        applications.add(new BasicDBObject("name", "TaskControl"));
        user.append("applications", applications);

        conn.getDatabase().getCollection("users").insert(user);

        ///////
        user = new BasicDBObject()
                .append("_id", "aseconduser")
                .append("name", "Another Test User")
                .append("email", "other@example.com")
                .append("passwordHash", "ZZZZZZZZYYYYYYYYXXXXXXXX");

        applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "EMM"));
        user.append("applications", applications);

        conn.getDatabase().getCollection("users").insert(user);
    }
}
