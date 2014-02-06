package br.com.egs.task.control.core.database.mapper;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class TaskMapper {
    public BasicDBObject getAsDbObject(Task task) {
        BasicDBObject obj = new BasicDBObject();

        if (StringUtils.isNotBlank(task.getId())) {
            obj.append("_id", new ObjectId(task.getId()));
        }

        obj.append("description", task.getDescription());

        obj.append("startDate", toZeroHourDate(task.getStartDate()));
        obj.append("foreseenEndDate", toMaxHourDate(task.getForeseenEndDate()));
        if (task.getEndDate() != null) {
            obj.append("endDate", toMaxHourDate(task.getEndDate()));
        }

        obj.append("source", task.getSource());
        obj.append("application", new BasicDBObject("name", task.getApplication().getName()));

        List<BasicDBObject> owners = new ArrayList<>();
        for (TaskOwner owner : task.getOwners()) {
            owners.add(new BasicDBObject("user", owner.getUser()));
        }
        obj.append("owners", owners);

        List<BasicDBObject> posts = new ArrayList<>();
        for (Post post : task.getPosts()) {
            posts.add(new BasicDBObject()
                    .append("timestamp", post.getTimestamp())
                    .append("user", post.getUser())
                    .append("text", post.getText()));
        }
        obj.append("posts", posts);


        return obj;
    }

    public Task getAsTask(BasicDBObject dbTask) {
        Task task = new Task();

        task.setId(dbTask.getObjectId("_id").toString());
        task.setDescription(dbTask.getString("description"));

        task.setStartDate(dbTask.getDate("startDate"));
        task.setForeseenEndDate(dbTask.getDate("foreseenEndDate"));
        task.setEndDate(dbTask.getDate("endDate"));

        task.setSource(dbTask.getString("source"));
        task.setApplication(new Application(((BasicDBObject)dbTask.get("application")).getString("name")));

        List<TaskOwner> owners = new ArrayList<>();
        List<BasicDBObject> dbOwners = (List<BasicDBObject>) dbTask.get("owners");
        for (BasicDBObject dbOwner : dbOwners) {
            owners.add(new TaskOwner(dbOwner.getString("user")));
        }
        task.setOwners(owners);

        List<Post> posts = new ArrayList<>();
        List<BasicDBObject> dbPosts = (List<BasicDBObject>) dbTask.get("posts");
        for (BasicDBObject dbPost : dbPosts) {
            Post p = new Post();
            p.setTimestamp(dbPost.getDate("timestamp"));
            p.setUser(dbPost.getString("user"));
            p.setText(dbPost.getString("text"));
            posts.add(p);
        }
        task.setPosts(posts);
        
        return task;
    }

    private Date toZeroHourDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date toMaxHourDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
