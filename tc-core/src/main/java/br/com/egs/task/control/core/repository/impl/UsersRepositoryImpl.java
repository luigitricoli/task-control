package br.com.egs.task.control.core.repository.impl;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.UsersRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UsersRepositoryImpl implements UsersRepository {

    private MongoDbConnection conn;

    @Inject
    public UsersRepositoryImpl(MongoDbConnection conn) {
        this.conn = conn;
    }

    @Override
    public User get(String login) {
        DBCollection collection = conn.getCollection("users");
        BasicDBObject dbUser = (BasicDBObject) collection.findOne(new BasicDBObject("_id", login));
        if (dbUser == null) {
            return null;
        } else {
            return User.fromDbObject(dbUser);
        }
    }

    @Override
    public void remove(User user) {
        BasicDBObject key = new BasicDBObject("_id", user.getLogin());
        conn.getCollection("users").remove(key);
    }

    @Override
    public void add(User user) {
        DBCollection collection = conn.getCollection("users");
        BasicDBObject dbUser = user.toDbObject();
        collection.insert(dbUser);
    }

    @Override
    public List<User> getAll() {
        DBCollection collection = conn.getCollection("users");
        DBCursor cursor = collection.find();
        cursor.sort(new BasicDBObject("name", 1));

        List<User> results = new ArrayList<>();
        while (cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cursor.next();
            results.add(User.fromDbObject(obj));
        }
        return results;
    }

    @Override
    public List<User> getByApplication(String application) {
        DBCollection collection = conn.getCollection("users");
        DBCursor cursor = collection.find(new BasicDBObject("applications.name", application));

        List<User> results = new ArrayList<>();
        while (cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cursor.next();
            results.add(User.fromDbObject(obj));
        }
        return results;
    }

    @Override
    public void update(User user) {
        DBCollection collection = conn.getCollection("users");
        BasicDBObject dbUser = user.toDbObject();
        collection.update(new BasicDBObject("_id", user.getLogin()), dbUser);
    }
}
