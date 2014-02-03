package br.com.egs.task.control.core.repository.impl;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.database.mapper.UserMapper;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.repository.Users;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UsersRepositoryImpl implements Users {

    private MongoDbConnection conn;
    private UserMapper mapper = new UserMapper();

    @Inject
    public UsersRepositoryImpl(MongoDbConnection conn) {
        this.conn = conn;
    }

    @Override
    public User get(String login) {
        DBCollection collection = conn.getDatabase().getCollection("users");
        BasicDBObject dbUser = (BasicDBObject) collection.findOne(new BasicDBObject("_id", login));
        if (dbUser == null) {
            return null;
        } else {
            return mapper.getAsUser(dbUser);
        }
    }

    @Override
    public void add(User user) {
        DBCollection collection = conn.getDatabase().getCollection("users");
        BasicDBObject dbUser = mapper.getAsDbObject(user);
        collection.insert(dbUser);
    }

    @Override
    public List<User> getAll() {
        DBCollection collection = conn.getDatabase().getCollection("users");
        DBCursor cursor = collection.find();

        List<User> results = new ArrayList<User>();
        while (cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cursor.next();
            results.add(mapper.getAsUser(obj));
        }
        return results;
    }
}
