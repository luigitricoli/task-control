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

import static org.junit.Assert.*;

/**
 *
 */
public class AuthenticationRestTest {
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
    public void authenticate() throws JSONException {
        String authData = "{username:'user', password:'secret'}";

        RestClient restfulie = Restfulie.custom();
        Response response = restfulie.at("http://localhost:8090/v1/authentication").accept("application/json")
                .as("application/json").post(authData);

        String expected = "{" +
                    "'login':'user'," +
                    "'name':'A Test User'," +
                    "'email':'test@example.com'," +
                    "'applications':[" +
                        "{'name':'OLM'},{'name':'TaskControl'}" +
                    "]" +
                "}";

        assertEquals(200, response.getCode());
        JSONAssert.assertEquals(expected, response.getContent(), true);
    }

    /**
     *
     */
    public void populateDatabase() {
        BasicDBObject user = new BasicDBObject()
                .append("_id", "user")
                .append("name", "A Test User")
                .append("email", "test@example.com")

                // Corresponds to the password "secret"
                .append("passwordHash", "335AA6A62C6C0C1C628AEF13A97E1E63BA3A1612");

        BasicDBList applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "OLM"));
        applications.add(new BasicDBObject("name", "TaskControl"));
        user.append("applications", applications);

        conn.getDatabase().getCollection("users").insert(user);
    }
}
