package br.com.egs.task.control.core.database.mapper;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.User;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UserMapperTest {

    private UserMapper mapper = new UserMapper();

    @Test
    public void fromUserToDbObject_login() {
        User user = populateTestUser();
        BasicDBObject dbObject = mapper.getAsDbObject(user);
        assertEquals("testusr", dbObject.get("_id"));
    }

    @Test
    public void fromUserToDbObject_name() {
        User user = populateTestUser();
        BasicDBObject dbObject = mapper.getAsDbObject(user);
        assertEquals("A Test User", dbObject.get("name"));
    }

    @Test
    public void fromUserToDbObject_email() {
        User user = populateTestUser();
        BasicDBObject dbObject = mapper.getAsDbObject(user);
        assertEquals("test@example.com", dbObject.get("email"));
    }

    @Test
    public void fromUserToDbObject_password() {
        User user = populateTestUser();
        BasicDBObject dbObject = mapper.getAsDbObject(user);
        assertEquals("AAAAABBBBBCCCCCDDDDDEEEEE", dbObject.get("passwordHash"));
    }

    @Test
    public void fromUserToDbObject_applications() {
        User user = populateTestUser();
        BasicDBObject dbObject = mapper.getAsDbObject(user);

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
        User usrObj = mapper.getAsUser(dbUser);
        assertEquals("testusr", usrObj.getLogin());
    }

    @Test
    public void fromDbObjectToUser_name() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = mapper.getAsUser(dbUser);
        assertEquals("A Test User", usrObj.getName());
    }

    @Test
    public void fromDbObjectToUser_email() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = mapper.getAsUser(dbUser);
        assertEquals("test@example.com", usrObj.getEmail());
    }

    @Test
    public void fromDbObjectToUser_password() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = mapper.getAsUser(dbUser);
        assertEquals("AAAAABBBBBCCCCCDDDDDEEEEE", usrObj.getPasswordHash());
    }

    @Test
    public void fromDbObjectToUser_applications() {
        BasicDBObject dbUser = populateTestDbUser();
        User usrObj = mapper.getAsUser(dbUser);

        List<Application> applications = usrObj.getApplications();
        assertNotNull(applications);

        assertEquals(2, applications.size());
        assertEquals("OLM", applications.get(0).getName());
        assertEquals("TaskControl", applications.get(1).getName());
    }

    private User populateTestUser() {
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

    private BasicDBObject populateTestDbUser() {
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

}
