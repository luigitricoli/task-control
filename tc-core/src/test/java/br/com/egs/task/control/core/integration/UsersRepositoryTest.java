package br.com.egs.task.control.core.integration;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.impl.UsersRepositoryImpl;
import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class UsersRepositoryTest {

    private static final String TESTUSER_PASSWORD_PLAIN = "abc";
    private static final String TESTUSER_PASSWORD_HASH = "EF57FB0FD830B9E8389E94D78D611C9CA3506950";

    private MongoDbConnection conn;
    private UsersRepositoryImpl repository;

    @Before
    public void setUp() {
        conn = TestConnectionFactory.getConnection();
        repository = new UsersRepositoryImpl(conn);
    }

    @After
    public void tearDown() {
        conn.getCollection("users").drop();
        conn.close();
    }

    @Test
    public void insert() {
        User user = createTestUser();
        repository.add(user);

        DBCursor cursor = conn.getCollection("users").find();

        assertEquals(1, cursor.count());
        BasicDBObject dbObject = (BasicDBObject) cursor.next();
        assertEquals(createTestUserAsDbObject(), dbObject);
    }

    @Test
    public void getByLogin() {
        BasicDBObject user1 = createTestUserAsDbObject();
        BasicDBObject user2 = createTestUserAsDbObject().append("_id", "testusr2");
        conn.getCollection("users").insert(user1, user2);

        User result = repository.get("testusr2");

        assertNotNull(result);

        assertEquals("testusr2", result.getLogin());
        assertEquals("A Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("N2", result.getType());
        assertEquals(TESTUSER_PASSWORD_HASH, result.getPasswordHash());

        List<Application> applications = result.getApplications();
        assertNotNull(applications);

        assertEquals(2, applications.size());
        assertEquals("OLM", applications.get(0).getName());
        assertEquals("TaskControl", applications.get(1).getName());
    }

    @Test
    public void getNonexistingUser() {
        User user = repository.get("doesnotexist");
        assertNull(user);
    }

    @Test
    public void getAll() {
        BasicDBObject user1 = createTestUserAsDbObject();
        BasicDBObject user2 = createTestUserAsDbObject().append("_id", "testusr2");
        conn.getCollection("users").insert(user1, user2);

        List<User> users = repository.getAll();

        assertEquals(2, users.size());
        assertEquals("testusr", users.get(0).getLogin());
        assertEquals("testusr2", users.get(1).getLogin());
    }

    @Test
    public void getByApplication() {
        BasicDBObject user1 = createTestUserAsDbObject();

        BasicDBObject user2 = createTestUserAsDbObject()
                .append("_id", "testusr2");
        user2.remove("applications");
        user2.append("applications", Arrays.asList(new BasicDBObject("name", "EMM")));

        conn.getCollection("users").insert(user1, user2);

        List<User> users = repository.getByApplication("TaskControl");

        assertEquals(1, users.size());
        assertEquals("testusr", users.get(0).getLogin());
    }

    @Test
    public void update() {
        conn.getCollection("users").insert(createTestUserAsDbObject());

        User modified = createTestUser();
        modified.setName("Changed Test User");
        modified.setEmail("modified@changes.com");
        modified.getApplications().add(new Application("EMM"));

        repository.update(modified);

        BasicDBObject expectedObject = createTestUserAsDbObject();
        expectedObject.append("name", "Changed Test User");
        expectedObject.append("email", "modified@changes.com");
        ((List<DBObject>)expectedObject.get("applications")).add(new BasicDBObject("name", "EMM"));

        DBObject savedObject = conn.getCollection("users").findOne();

        assertEquals(expectedObject, savedObject);
    }

    private User createTestUser() {
        User user = new User("testusr");
        user.setName("A Test User");
        user.setEmail("test@example.com");
        user.setType("N2");

        user.setPassword(TESTUSER_PASSWORD_PLAIN);
        assertEquals("Ensuring that the generated password is the expected value",
                TESTUSER_PASSWORD_HASH, user.getPasswordHash());

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
                .append("type", "N2")
                .append("passwordHash", TESTUSER_PASSWORD_HASH);

        BasicDBList applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "OLM"));
        applications.add(new BasicDBObject("name", "TaskControl"));

        user.append("applications", applications);

        return user;
    }

}
