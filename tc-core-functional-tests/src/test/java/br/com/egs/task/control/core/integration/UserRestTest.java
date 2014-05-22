package br.com.egs.task.control.core.integration;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        conn.getCollection("users").drop();
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
                    "'type':'N1'," +
                    "'applications':[" +
                        "{'name':'OLM'},{'name':'TaskControl'}" +
                    "]" +
                "},{" +
                    "'login':'aseconduser'," +
                    "'name':'Another Test User'," +
                    "'email':'other@example.com'," +
                    "'type':'N2'," +
                    "'applications':[" +
                        "{'name':'EMM'}" +
                    "]" +
                "}" +
                "]";

        assertEquals(200, response.getCode());
        JSONAssert.assertEquals(expected, response.getContent(), true);
    }

    @Test
    public void listUsersByApplication() throws JSONException {
        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/users?application=EMM").accept("application/json").get();

        String expected = "[" +
                "{" +
                "'login':'aseconduser'," +
                "'name':'Another Test User'," +
                "'email':'other@example.com'," +
                "'type':'N2'," +
                "'applications':[" +
                "     {'name':'EMM'}" +
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
                "'type':'N1'," +
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

    @Test
    public void createUser() throws JSONException {
        RestClient restfulie = Restfulie.custom();

        String userJson = "{" +
            "'login':'testusr3'," +
            "'name':'A Third Test User'," +
            "'password':'foo123bar'," +
            "'email':'test3@example.com'," +
            "'type':'N3'," +
            "'applications':[" +
                "{'name':'FEM'},{'name':'EMM'}" +
            "]" +
        "}";

        String expectedResponse = "{" +
                "'login':'testusr3'," +
                "'name':'A Third Test User'," +
                "'email':'test3@example.com'," +
                "'type':'N3'," +
                "'applications':[" +
                "{'name':'FEM'},{'name':'EMM'}" +
                "]" +
                "}";

        Response response = restfulie.at("http://localhost:8090/v1/users").accept("application/json")
                 .as("application/json").post(userJson);

        assertEquals(200, response.getCode());

        String content = response.getContent();
        JSONAssert.assertEquals(expectedResponse, content, true);

        assertEquals(3, conn.getCollection("users").count());
        assertNotNull(conn.getCollection("users").findOne(new BasicDBObject("_id", "testusr3")));
    }


    @Test
    public void createUser_missingAttribute() throws JSONException {
        RestClient restfulie = Restfulie.custom();

        String userJson = "{" +
                // No login
                "'name':'A Third Test User'," +
                "'email':'test3@example.com'," +
                "'type':'N3'," +
                "'applications':[" +
                "{'name':'FEM'},{'name':'EMM'}" +
                "]" +
                "}";

        Response response = restfulie.at("http://localhost:8090/v1/users").accept("application/json")
                .as("application/json").post(userJson);

        assertEquals(400, response.getCode());
    }

    @Test
    public void updateUser() throws JSONException {
        RestClient restfulie = Restfulie.custom();

        String userJson = "{" +
            "'name':'A Modified Test User'," +
            "'email':'changed@example.com'," +
            "'type':'N2'," +
            "'applications':[" +
                 "{'name':'FEM'},{'name':'EMM'}" +
            "]" +
         "}";

        Response response = restfulie.at("http://localhost:8090/v1/users/testusr").accept("application/json")
                .as("application/json").put(userJson);

        assertEquals(200, response.getCode());
        String content = response.getContent();

        assertEquals(2, conn.getCollection("users").count());

        DBObject savedUser = conn.getCollection("users").findOne(new BasicDBObject("_id", "testusr"));
        JSONAssert.assertEquals(userJson, savedUser.toString(), false);
    }

    @Test
    public void changePassword() throws JSONException {
        RestClient restfulie = Restfulie.custom();

        DBObject savedUser = conn.getCollection("users").findOne(new BasicDBObject("_id", "testusr"));

        String changePasswordJson = "{'password':'NEWPASS'}";

        Response response = restfulie.at("http://localhost:8090/v1/users/testusr").accept("application/json")
                .as("application/json").put(changePasswordJson);

        assertEquals(200, response.getCode());
        assertEquals(2, conn.getCollection("users").count());

        // The only attribute expected to change
        savedUser.put("passwordHash", "36644404355E6C29C3CA7179FCF48D158A9BA7AB");

        DBObject savedUserAfter = conn.getCollection("users").findOne(new BasicDBObject("_id", "testusr"));

        assertEquals(savedUser, savedUserAfter);
    }

    /**
     *
     */
    public void populateDatabase() {
        BasicDBObject user = new BasicDBObject()
                .append("_id", "testusr")
                .append("name", "A Test User")
                .append("email", "test@example.com")
                .append("type", "N1")
                .append("passwordHash", "AAAAABBBBBCCCCCDDDDDEEEEE");

        BasicDBList applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "OLM"));
        applications.add(new BasicDBObject("name", "TaskControl"));
        user.append("applications", applications);

        conn.getCollection("users").insert(user);

        ///////
        user = new BasicDBObject()
                .append("_id", "aseconduser")
                .append("name", "Another Test User")
                .append("email", "other@example.com")
                .append("type", "N2")
                .append("passwordHash", "ZZZZZZZZYYYYYYYYXXXXXXXX");

        applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "EMM"));
        user.append("applications", applications);

        conn.getCollection("users").insert(user);
    }
}
