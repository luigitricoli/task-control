package br.com.egs.task.control.core.entities;

import br.com.egs.task.control.core.exception.LateTaskException;
import br.com.egs.task.control.core.exception.ValidationException;
import com.google.gson.*;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
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
     * This must be changed only in testing code, when a precise control over the variables is required.
     */
    private static Date fixedCurrentDate = null;

    public Task(String id,
                String description,
                Date startDate,
                Date foreseenEndDate,
                Date endDate,
                String source,
                Application application,
                List<TaskOwner> owners) {

        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.endDate = endDate;
        this.source = source;
        this.application = application;
        this.owners = owners;
    }

    private Task() {

    }

    /**
     * Serialize this object to the presentation JSON format.
     * @return
     */
    public String toJson() {
        return new GsonBuilder().registerTypeAdapter(Task.class, new TaskSerializer())
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }

    public static Task fromJson(String jsonString) {
        return new GsonBuilder().registerTypeAdapter(Task.class, new TaskDeserializer())
                .create()
                .fromJson(jsonString, Task.class);
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
        if (this.getPosts() != null) {
            for (Post post : this.getPosts()) {
                posts.add(new BasicDBObject()
                        .append("timestamp", post.getTimestamp())
                        .append("user", post.getUser())
                        .append("text", post.getText()));
            }
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

        task.id = (dbTask.getObjectId("_id").toString());
        task.description = (dbTask.getString("description"));

        task.startDate = (dbTask.getDate("startDate"));
        task.foreseenEndDate = (dbTask.getDate("foreseenEndDate"));
        task.endDate = (dbTask.getDate("endDate"));

        task.source = (dbTask.getString("source"));
        task.application = (new Application(((BasicDBObject) dbTask.get("application")).getString("name")));

        List<TaskOwner> owners = new ArrayList<>();
        List<BasicDBObject> dbOwners = (List<BasicDBObject>) dbTask.get("owners");
        for (BasicDBObject dbOwner : dbOwners) {
            owners.add(new TaskOwner(dbOwner.getString("login")));
        }
        task.owners = (owners);

        List<BasicDBObject> dbPosts = (List<BasicDBObject>) dbTask.get("posts");
        if (dbPosts != null) {
            for (BasicDBObject dbPost : dbPosts) {
                Post p = new Post(
                        dbPost.getString("user"),
                        dbPost.getString("text"),
                        dbPost.getDate("timestamp")
                );
                task.addPost(p);
            }
        }

        return task;
    }

    public void validateForInsert() throws ValidationException {
        if (id != null) {
            throw new ValidationException("The id field must not be set for a new Task");
        }

        if (StringUtils.isBlank(description)) {
            throw new ValidationException("The description is required");
        }

        if (startDate == null) {
            throw new ValidationException("The startDate is required");
        }

        if (foreseenEndDate == null) {
            throw new ValidationException("The foreseenEndDate is required");
        }

        if (endDate != null) {
            throw new ValidationException("The endDate must not be set for a new Task");
        }

        if (StringUtils.isBlank(source)) {
            throw new ValidationException("The source is required");
        }

        if (application == null || StringUtils.isBlank(application.getName())) {
            throw new ValidationException("The application is required");
        }

        if (owners == null || owners.isEmpty()) {
            throw new ValidationException("At least one owner is required");
        }

        if (posts != null) {
            throw new ValidationException("Posts are not allowed for a new Task");
        }

        if (foreseenEndDate.before(startDate)) {
            throw new ValidationException("Start Date cannot be greater than Foreseen End Date");
        }
    }

    /**
     *
     * @param date
     * @throws ValidationException
     */
    public void finish(Date date) throws ValidationException {
        if (date == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        if (this.endDate != null) {
            throw new ValidationException("Task already finished");
        }
        date = toMaxHourDate(date);

        if (date.after(this.foreseenEndDate)) {
            boolean atrasoCommentExists = false;
            for (Post post : posts) {
                if (post.getText().contains("#atraso")) {
                    atrasoCommentExists = true;
                    break;
                }
            }

            if (!atrasoCommentExists) {
                throw new LateTaskException(
                        "A late task can only be finished if a #atraso message is present in the posts");
            }
        }

        this.endDate = date;
    }

    /**
     *
     * @param startDate
     * @throws ValidationException
     */
    public void changeStartDate(Date startDate) throws ValidationException {
        if (startDate.before(getCurrentDate())) {
            throw new ValidationException("Cannot change the start date. The task is already started");
        }

        if (this.endDate != null) {
            throw new ValidationException("Task already finished");
        }

        this.startDate = toZeroHourDate(startDate);
    }

    /**
     *
     * @param foreseen
     * @throws ValidationException
     */
    public void changeForeseenEndDate(Date foreseen) throws ValidationException {
        if (this.endDate != null) {
            throw new ValidationException("Task already finished");
        }

        this.foreseenEndDate = toMaxHourDate(foreseen);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getForeseenEndDate() {
        return foreseenEndDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Application getApplication() {
        return application;
    }

    public List<TaskOwner> getOwners() {
        return owners;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void addPost(Post p) {
        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }
        this.posts.add(p);
    }

    public String getSource() {
        return source;
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
            json.addProperty("foreseenEndDate", dateFormat.format(task.getForeseenEndDate()));
            if (task.getEndDate() != null) {
                json.addProperty("endDate", dateFormat.format(task.getEndDate()));
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

            if (task.getPosts() != null) {
                JsonArray posts = new JsonArray();
                for (Post p : task.getPosts()) {
                    JsonObject post = new JsonObject();
                    post.addProperty("timestamp", timestampFormat.format(p.getTimestamp()));
                    post.addProperty("user", p.getUser());
                    post.addProperty("text", p.getText());
                    posts.add(post);
                }
                json.add("posts", posts);
            }
            
            return json;
        }
    }

    public static class TaskDeserializer implements JsonDeserializer<Task> {

        private static final DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Task deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Task t = new Task();

            JsonObject obj = jsonElement.getAsJsonObject();

            // The attributes can optionally be encapsulated under a 'task' parent
            // object. If this is the case, we "go into" this parent object
            // before looking for the attributes.
            //
            //      { task: { 'attribute1': 'value1', 'attribute2': 'value2' } }
            //                 is equivalent to
            //      { 'attribute1': 'value1', 'attribute2': 'value2' }
            //
            if (obj.has("task")) {
                obj = obj.get("task").getAsJsonObject();
            }

            t.id = (obj.get("id") == null ? null : obj.get("id").getAsString());
            t.description = (obj.get("description") == null ? null : obj.get("description").getAsString());
            t.startDate = (obj.get("startDate") == null ? null : parseDate(obj.get("startDate").getAsString(), false));
            t.foreseenEndDate = (obj.get("foreseenEndDate") == null ? null : parseDate(obj.get("foreseenEndDate").getAsString(), true));
            t.endDate = (obj.get("endDate") == null ? null : parseDate(obj.get("endDate").getAsString(), true));
            t.source = (obj.get("source") == null ? null : obj.get("source").getAsString());
            t.application = (obj.get("application") == null ? null : new Application(obj.get("application").getAsString()));

            if (obj.get("owners") != null) {
                List<TaskOwner> owners = new ArrayList<>();

                for (JsonElement jsonOwner : obj.get("owners").getAsJsonArray()) {
                    JsonObject jsonOwnerObject = jsonOwner.getAsJsonObject();
                    TaskOwner o = new TaskOwner(jsonOwnerObject.get("login").getAsString());
                    owners.add(o);
                }
                t.owners = owners;
            }

            return t;
        }

        private Date parseDate(String dtString, boolean moveToEndOfDay) {
            if (StringUtils.isBlank(dtString)) {
                return null;
            }

            Date dt;
            try {
                dt = dateParser.parse(dtString);
            } catch (ParseException e) {
                throw new JsonParseException("Invalid date: " + dtString, e);
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            if (moveToEndOfDay) {
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
            } else {
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
            }
            return cal.getTime();
        }
    }

    /**
     *
     * @return
     */
    private Date getCurrentDate() {
         if (fixedCurrentDate != null) {
             // This conditions applies only to unit test environments!
             return fixedCurrentDate;
         } else {
             return new Date();
         }
    }

    /**
     * This must be changed only in testing code, when a precise control over the variables is required.
     */
    public static void setFixedCurrentDate(Date dt) {
        fixedCurrentDate = dt;
    }
}
