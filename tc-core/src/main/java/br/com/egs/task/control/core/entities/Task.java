package br.com.egs.task.control.core.entities;

import com.google.gson.*;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class Task {

	private String id;
    private String description;

    private Date startDate;
    private Date foreseenEndDate;
    private Date endDate;

    private String source;
    private Application application;

    private List<TaskOwner> owners;

    private List<Post> posts;

    /**
     * Serialize this object to the presentation JSON format.
     * @return
     */
    public String toJson() {
        return new GsonBuilder().registerTypeAdapter(Task.class, new TaskSerializer())
                .create()
                .toJson(this);
    }

    /**
     * Converts to the MongoDB Object representation
     * @return
     */
    public BasicDBObject toDbObject() {
        BasicDBObject obj = new BasicDBObject();

        if (StringUtils.isNotBlank(this.getId())) {
            obj.append("_id", new ObjectId(this.getId()));
        }

        obj.append("description", this.getDescription());

        obj.append("startDate", toZeroHourDate(this.getStartDate()));
        obj.append("foreseenEndDate", toMaxHourDate(this.getForeseenEndDate()));
        if (this.getEndDate() != null) {
            obj.append("endDate", toMaxHourDate(this.getEndDate()));
        }

        obj.append("source", this.getSource());
        obj.append("application", new BasicDBObject("name", this.getApplication().getName()));

        List<BasicDBObject> owners = new ArrayList<>();
        for (TaskOwner owner : this.getOwners()) {
            owners.add(new BasicDBObject("login", owner.getLogin()));
        }
        obj.append("owners", owners);

        List<BasicDBObject> posts = new ArrayList<>();
        for (Post post : this.getPosts()) {
            posts.add(new BasicDBObject()
                    .append("timestamp", post.getTimestamp())
                    .append("user", post.getUser())
                    .append("text", post.getText()));
        }
        obj.append("posts", posts);
        return obj;
    }

    /**
     * Converts a MongoDB object representation to a Task instance
     * @param dbTask
     * @return
     */
    public static Task fromDbObject(BasicDBObject dbTask) {
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
            owners.add(new TaskOwner(dbOwner.getString("login")));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getForeseenEndDate() {
        return foreseenEndDate;
    }

    public void setForeseenEndDate(Date foreseenEndDate) {
        this.foreseenEndDate = foreseenEndDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<TaskOwner> getOwners() {
        return owners;
    }

    public void setOwners(List<TaskOwner> owners) {
        this.owners = owners;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    /**
     * Custom JSON serialization.
     */
    public static class TaskSerializer implements JsonSerializer<Task> {

        private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject json = new JsonObject();

            json.addProperty("id", task.getId());
            json.addProperty("description", task.getDescription());

            json.addProperty("startDate", dateFormat.format(task.getStartDate()));
            json.addProperty("endDate", dateFormat.format(task.getEndDate()));
            if (task.getForeseenEndDate() != null) {
                json.addProperty("foreseenEndDate", dateFormat.format(task.getForeseenEndDate()));
            }

            json.addProperty("source", task.getSource());
            json.addProperty("application", task.getApplication().getName());

            JsonArray owners = new JsonArray();
            for (TaskOwner to : task.getOwners()) {
                JsonObject owner = new JsonObject();
                owner.addProperty("login", to.getLogin());
                owners.add(owner);
            }
            json.add("owners", owners);

            JsonArray posts = new JsonArray();
            for (Post p : task.getPosts()) {
                JsonObject post = new JsonObject();
                post.addProperty("timestamp", timestampFormat.format(p.getTimestamp()));
                post.addProperty("user", p.getUser());
                post.addProperty("text", p.getText());
                posts.add(post);
            }
            json.add("posts", posts);
            
            return json;
        }
    }
}
