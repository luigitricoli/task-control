package br.com.egs.task.control.core.entities;

import com.google.gson.JsonParseException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class UserTest {

    @Test
    public void generatePasswordHash() {
        User u = new User("user");
        u.setPassword("secret");
        assertEquals("335AA6A62C6C0C1C628AEF13A97E1E63BA3A1612", u.getPasswordHash());

        // Ensure that a different user with same password gets a different hash
        u = new User("user2");
        u.setPassword("secret");
        assertEquals("1299DCB1C274905CD58C37A1ABF17B080A06C6C5", u.getPasswordHash());
    }

    @Test
    public void checkPassword_failure() {
        User u = new User("user");
        u.setPassword("secret");

        boolean check = u.checkPassword("nonono");
        assertFalse(check);
    }

    @Test
    public void checkPassword_ok() {
        User u = new User("user");
        u.setPassword("secret");

        boolean check = u.checkPassword("secret");
        assertTrue(check);
    }

    @Test
    public void fromUserToDbObject_login() {
        User user = populateTestUser();
        BasicDBObject dbObject = user.toDbObject();
        assertEquals("testusr", dbObject.get("_id"));
    }

    @Test
    public void fromUserToDbObject_name() {
        User user = populateTestUser();
        BasicDBObject dbObject = user.toDbObject();
        assertEquals("A Test User", dbObject.get("name"));
    }

    @Test
    public void fromUserToDbObject_email() {
        User user = populateTestUser();
        BasicDBObject dbObject = user.toDbObject();
        assertEquals("test@example.com", dbObject.get("email"));
    }

    @Test
    public void fromUserToDbObject_type() {
        User user = populateTestUser();
        BasicDBObject dbObject = user.toDbObject();
        assertEquals("N2", dbObject.get("type"));
    }

    @Test
    public void fromUserToDbObject_password() {
        User user = populateTestUser();
        BasicDBObject dbObject = user.toDbObject();
        assertEquals("AAAAABBBBBCCCCCDDDDDEEEEE", dbObject.get("passwordHash"));
    }

    @Test
    public void fromUserToDbObject_applications() {
        User user = populateTestUser();
        BasicDBObject dbObject = user.toDbObject();

        assertTrue(dbObject.get("applications") instanceof BasicDBList);
        BasicDBList list = (BasicDBList) dbObject.get("applications");
        assertEquals(2, list.size());

        BasicDBObject app1 = (BasicDBObject) list.get(0);
        assertEquals("OLM", app1.get("name"));
        BasicDBObject app2 = (BasicDBObject) list.get(1);
        assertEquals("TaskControl", app2.get("name"));
    }

    @Test
    public void fromDbObjectToUser_login() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = User.fromDbObject(dbUser);
        assertEquals("testusr", usrObj.getLogin());
    }

    @Test
    public void fromDbObjectToUser_name() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = User.fromDbObject(dbUser);
        assertEquals("A Test User", usrObj.getName());
    }

    @Test
    public void fromDbObjectToUser_email() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = User.fromDbObject(dbUser);
        assertEquals("test@example.com", usrObj.getEmail());
    }

    @Test
    public void fromDbObjectToUser_type() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = User.fromDbObject(dbUser);
        assertEquals("N2", usrObj.getType());
    }

    @Test
    public void fromDbObjectToUser_password() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = User.fromDbObject(dbUser);
        assertEquals("AAAAABBBBBCCCCCDDDDDEEEEE", usrObj.getPasswordHash());
    }

    @Test
    public void fromDbObjectToUser_applications() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = User.fromDbObject(dbUser);

        List<Application> applications = usrObj.getApplications();
        assertNotNull(applications);

        assertEquals(2, applications.size());
        assertEquals("OLM", applications.get(0).getName());
        assertEquals("TaskControl", applications.get(1).getName());
    }

    @Test
    public void fromJsonToUser() {
        String userJson = "{" +
                "'login':'user'," +
                "'name':'A Test User'," +
                "'password':'secret'," +
                "'email':'test3@example.com'," +
                "'type':'N3'," +
                "'applications':[" +
                "{'name':'FEM'}, {'name':'EMA'}" +
                "]" +
                "}";

        User u = User.fromJson(userJson);

        assertEquals("user", u.getLogin());
        assertEquals("A Test User", u.getName());
        assertEquals("335AA6A62C6C0C1C628AEF13A97E1E63BA3A1612", u.getPasswordHash());
        assertEquals("test3@example.com", u.getEmail());
        assertEquals("N3", u.getType());
        assertEquals(2, u.getApplications().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonToUser_noLogin() {
        String userJson = "{'name':'Some incomplete User'}";
        User.fromJson(userJson);
    }

    @Test
    public void fromJsonToUser_passwordOnly() {
        String userJson = "{'password':'secret'}";
        User u = User.fromJson(userJson, "user");

        assertEquals("335AA6A62C6C0C1C628AEF13A97E1E63BA3A1612", u.getPasswordHash());
    }

    private User populateTestUser() {
        User user = new User("testusr");
        user.setName("A Test User");
        user.setEmail("test@example.com");
        user.setType("N2");
        user.passwordHash ="AAAAABBBBBCCCCCDDDDDEEEEE";

        List<Application> applications = new ArrayList<>();
        applications.add(new Application("OLM"));
        applications.add(new Application("TaskControl"));
        user.setApplications(applications);
        return user;
    }

    private BasicDBObject populateTestDbUser() {
        BasicDBObject user = new BasicDBObject()
                .append("_id", "testusr")
                .append("name", "A Test User")
                .append("email", "test@example.com")
                .append("type", "N2")
                .append("passwordHash", "AAAAABBBBBCCCCCDDDDDEEEEE");

        BasicDBList applications = new BasicDBList();
        applications.add(new BasicDBObject("name", "OLM"));
        applications.add(new BasicDBObject("name", "TaskControl"));

        user.append("applications", applications);

        return user;
    }
}
