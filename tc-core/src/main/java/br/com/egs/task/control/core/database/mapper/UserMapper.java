package br.com.egs.task.control.core.database.mapper;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.User;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps User objects to its representation in MongoDB API, and vice-versa.
 */
public class UserMapper {
    public BasicDBObject getAsDbObject(User user) {
        BasicDBObject result = new BasicDBObject();
        result.put("_id", user.getLogin());
        result.put("name", user.getName());
        result.put("email", user.getEmail());
        result.put("passwordHash", user.getPasswordHash());

        BasicDBList resultApplications = new BasicDBList();
        for (Application app : user.getApplications()) {
            resultApplications.add(new BasicDBObject()
                    .append("name", app.getName())
            );
        }

        result.put("applications", resultApplications);

        return result;
    }

    public User getAsUser(BasicDBObject dbUser) {
        String login = dbUser.getString("_id");

        User u = new User(login);
        u.setName(dbUser.getString("name"));
        u.setEmail(dbUser.getString("email"));
        u.setPasswordHash(dbUser.getString("passwordHash"));

        @SuppressWarnings("unchecked")
        List<BasicDBObject> dbApplications = (List<BasicDBObject>) dbUser.get("applications");

        List<Application> applications = new ArrayList<Application>();
        for (BasicDBObject dbApp : dbApplications) {
            applications.add(new Application(dbApp.getString("name")));
        }
        u.setApplications(applications);

        return u;
    }
}
