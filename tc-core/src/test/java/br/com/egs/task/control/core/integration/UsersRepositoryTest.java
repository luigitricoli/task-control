package br.com.egs.task.control.core.integration;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.impl.UsersRepositoryImpl;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class UsersRepositoryTest {
    private MongoDbConnection conn;
    private UsersRepositoryImpl repository;

    @Before
    public void setUp() {
        conn = TestConnectionFactory.getConnection();
        repository = new UsersRepositoryImpl(conn);
    }

    @After
    public void tearDown() {
        conn.getDatabase().getCollection("users").drop();
        conn.close();
    }

    @Test
    public void insert() {
        User user = createTestUser();
        repository.add(user);

        DBCursor cursor = conn.getDatabase().getCollection("users").find();

        assertEquals(1, cursor.count());
        BasicDBObject dbObject = (BasicDBObject) cursor.next();
        assertEquals(createTestUserAsDbObject(), dbObject);
    }

    @Test
    public void getByLogin() {
        BasicDBObject user1 = createTestUserAsDbObject();
        BasicDBObject user2 = createTestUserAsDbObject().append("_id", "testusr2");
        conn.getDatabase().getCollection("users").insert(user1, user2);

        User result = repository.get("testusr2");

        assertNotNull(result);

        assertEquals("testusr2", result.getLogin());
        assertEquals("A Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("AAAAABBBBBCCCCCDDDDDEEEEE", result.getPasswordHash());

        List<Application> applications = result.getApplications();
        assertNotNull(applications);

        assertEquals(2, applications.size());
        assertEquals("OLM", applications.get(0).getName());
        assertEquals("TaskControl", applications.get(1).getName());
    }

    @Test
    public void getAll() {
        BasicDBObject user1 = createTestUserAsDbObject();
        BasicDBObject user2 = createTestUserAsDbObject().append("_id", "testusr2");
        conn.getDatabase().getCollection("users").insert(user1, user2);

        List<User> users = repository.getAll();

        assertEquals(2, users.size());
        assertEquals("testusr", users.get(0).getLogin());
        assertEquals("testusr2", users.get(1).getLogin());
    }

    private User createTestUser() {
        User user = new User("testusr");
        user.setName("A Test User");
        user.setEmail("test@example.com");
        user.setPasswordHash("AAAAABBBBBCCCCCDDDDDEEEEE");

        List<Application> applications = new ArrayList<Application>();
        applications.add(new Application("OLM"));
        applications.add(new Application("TaskControl"));
        user.setApplications(applications);
        return user;
    }

    private BasicDBObject createTestUserAsDbObject() {
        BasicDBObject user = new BasicDBObject()
                .append("_id", "testusr")
                .append("name", "A Test User")
                .append("email", "test@example.com")
                .append("passwordHash", "AAAAABBBBBCCCCCDDDDDEEEEE");

        BasicDBList applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "OLM"));
        applications.add(new BasicDBObject("name", "TaskControl"));

        user.append("applications", applications);

        return user;
    }

    private String testUserAsJson() {
        return "{ \"_id\" : \"testusr\" , \"name\" : \"A Test User\" , \"email\" : \"test@example.com\" , \"passwordHash\" : \"AAAAABBBBBCCCCCDDDDDEEEEE\" , \"applications\" : [ { \"name\" : \"OLM\"} , { \"name\" : \"TaskControl\"}]}";
    }
}
