package br.com.egs.task.control.core.entities;

import br.com.egs.task.control.core.exception.LateTaskException;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.utils.Messages;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Task {

    public static final int WORKDAY_DURATION_IN_HOURS = 8;
    public static final String WORKDAY_DATE_FORMAT = "yyyy-MM-dd";

	private String id;
    private String description;

    private Date startDate;
    private Date foreseenEndDate;
    private Date endDate;
    private Integer foreseenWorkHours;

    private String source;
    private Application application;

    private List<TaskOwner> owners;

    private List<Post> posts;

    private Messages messages;

    /**
     * This must be changed only in testing code, when a precise control over the variables is required.
     */
    private static Date fixedCurrentDate = null;

    public Task(String id,
                String description,
                Date startDate,
                Date foreseenEndDate,
                Date endDate,
                Integer foreseenWorkHours,
                String source,
                Application application,
                List<TaskOwner> owners) {

        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.endDate = endDate;
        this.foreseenWorkHours = foreseenWorkHours;
        this.source = source;
        this.application = application;
        this.owners = owners;

        this.messages = new Messages();
    }

    private Task() {
        this.messages = new Messages();
    }

    /**
     * Serialize this object to the presentation JSON format.
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

        obj.append("foreseenWorkHours", this.getForeseenWorkHours());

        obj.append("source", this.getSource());
        obj.append("application", new BasicDBObject("name", this.getApplication().getName()));

        List<BasicDBObject> owners = new ArrayList<>();
        for (TaskOwner owner : this.getOwners()) {
            BasicDBObject dbOwner = new BasicDBObject()
                    .append("login", owner.getLogin())
                    .append("name", owner.getName())
                    .append("type", owner.getType());

            List<BasicDBObject> dbWorkdays = new ArrayList<>();
            for (TaskOwner.WorkDay wd : owner.getWorkDays()) {
                dbWorkdays.add(new BasicDBObject()
                            .append("day", wd.getDay())
                            .append("hours", wd.getHours()));
            }
            dbOwner.append("workDays", dbWorkdays);

            owners.add(dbOwner);
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
     */
    public static Task fromDbObject(BasicDBObject dbTask) {
        Task task = new Task();

        task.id = (dbTask.getObjectId("_id").toString());
        task.description = (dbTask.getString("description"));

        task.startDate = (dbTask.getDate("startDate"));
        task.foreseenEndDate = (dbTask.getDate("foreseenEndDate"));
        task.endDate = (dbTask.getDate("endDate"));

        task.foreseenWorkHours = (dbTask.getInt("foreseenWorkHours"));

        task.source = (dbTask.getString("source"));
        task.application = (new Application(((BasicDBObject) dbTask.get("application")).getString("name")));

        List<TaskOwner> owners = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<BasicDBObject> dbOwners = (List<BasicDBObject>) dbTask.get("owners");
        for (BasicDBObject dbOwner : dbOwners) {
            TaskOwner owner = new TaskOwner(dbOwner.getString("login"),
                    dbOwner.getString("name"),
                    dbOwner.getString("type"));

            @SuppressWarnings("unchecked")
            List<BasicDBObject> dbWorkdays = (List<BasicDBObject>) dbOwner.get("workDays");
            for (BasicDBObject dbWorkday : dbWorkdays) {
                String day = (String) dbWorkday.get("day");
                int hours = (int) dbWorkday.get("hours");
                owner.addWorkHours(day, hours);
            }

            owners.add(owner);
        }
        task.owners = (owners);

        @SuppressWarnings("unchecked")
        List<BasicDBObject> dbPosts = (List<BasicDBObject>) dbTask.get("posts");
        if (dbPosts != null) {
            task.posts = new ArrayList<>();
            for (BasicDBObject dbPost : dbPosts) {
                Post p = new Post(
                        dbPost.getString("user"),
                        dbPost.getString("text"),
                        dbPost.getDate("timestamp")
                );
                task.posts.add(p);
            }
        }

        return task;
    }

    public void validateForInsert() throws ValidationException {
        if (id != null) {
            throw new ValidationException(
                    "The id field must not be set for a new Task",
                    Messages.Keys.VALIDATION_TASK_ID_MUST_NOT_BE_SET_ON_CREATE);
        }

        if (StringUtils.isBlank(description)) {
            throw new ValidationException("The description is required",
                    Messages.Keys.VALIDATION_TASK_DESCRIPTION_REQUIRED);
        }

        if (startDate == null) {
            throw new ValidationException("The startDate is required",
                    Messages.Keys.VALIDATION_TASK_START_REQUIRED);
        }

        if (foreseenEndDate == null) {
            throw new ValidationException("The foreseenEndDate is required",
                    Messages.Keys.VALIDATION_TASK_FORESEEN_REQUIRED);
        }

        if (endDate != null) {
            throw new ValidationException("The endDate must not be set for a new Task",
                    Messages.Keys.VALIDATION_TASK_END_MUST_NOT_BE_SET_ON_CREATE);
        }

        if (StringUtils.isBlank(source)) {
            throw new ValidationException("The source is required",
                    Messages.Keys.VALIDATION_TASK_SOURCE_REQUIRED);
        }

        if (application == null || StringUtils.isBlank(application.getName())) {
            throw new ValidationException("The application is required",
                    Messages.Keys.VALIDATION_TASK_APPLICATION_REQUIRED);
        }

        if (owners == null || owners.isEmpty()) {
            throw new ValidationException("At least one owner is required",
                    Messages.Keys.VALIDATION_TASK_OWNER_REQUIRED_AT_LEAST_ONE);
        }
        for (TaskOwner owner : owners) {
            if (StringUtils.isBlank(owner.getLogin())) {
                throw new ValidationException("Owner login is required",
                        Messages.Keys.VALIDATION_TASK_OWNER_LOGIN_REQUIRED);
            }
            if (owner.getLogin().contains(",")) {
                // The comma can be used as a separator in inputs that require a list of users.
                // e.g.  /tasks/searchTasks?userFilter=user1,user2
                // Do not let it be part of the login itself.
                throw new ValidationException("Owner login contains invalid character: [,]",
                        Messages.Keys.VALIDATION_TASK_OWNER_LOGIN_CONTAINS_SEPARATOR_CHAR);
            }
        }

        if (posts != null) {
            throw new ValidationException("Posts are not allowed for a new Task",
                    Messages.Keys.VALIDATION_TASK_POSTS_MUST_NOT_BE_SET_ON_CREATE);
        }

        if (foreseenEndDate.before(startDate)) {
            throw new ValidationException("Start Date cannot be greater than Foreseen End Date",
                    Messages.Keys.VALIDATION_TASK_START_AFTER_END);
        }

        Calendar beginOfCurrentDate = Calendar.getInstance();
        beginOfCurrentDate.setTime(getCurrentDate());
        beginOfCurrentDate.set(Calendar.HOUR_OF_DAY, 0);
        beginOfCurrentDate.set(Calendar.MINUTE, 0);
        beginOfCurrentDate.set(Calendar.SECOND, 0);
        beginOfCurrentDate.set(Calendar.MILLISECOND, 0);

        if (beginOfCurrentDate.getTime().after(foreseenEndDate)) {
            throw new ValidationException("Foreseen End Date cannot be less than the current date",
                    Messages.Keys.VALIDATION_TASK_FORESEEN_END_IN_THE_PAST);
        }
    }

    /**
     *
     * @throws ValidationException
     */
    public void finish(Date date) throws ValidationException {
        if (date == null) {
            throw new ValidationException("End date cannot be null",
                    Messages.Keys.VALIDATION_TASK_END_REQUIRED);
        }

        if (this.endDate != null) {
            throw new ValidationException("Task already finished",
                    Messages.Keys.VALIDATION_TASK_CANNOT_MODIFY_FINISHED);
        }
        date = toMaxHourDate(date);

        if (date.after(this.foreseenEndDate)) {
            boolean atrasoCommentExists = false;
            for (Post post : posts) {
                String text = post.getText().toLowerCase();
                String latePostExpression = messages.get(Messages.Keys.PARAMETER_TASK_LATE_POST_EXPRESSION);
                if (text.matches(latePostExpression)) {
                    atrasoCommentExists = true;
                    break;
                }
            }

            if (!atrasoCommentExists) {
                throw new LateTaskException();
            }
        }

        this.endDate = date;
    }

    /**
     *
     * @throws ValidationException
     */
    public void changeStartDate(Date startDate) throws ValidationException {
        if (startDate.before(getCurrentDate())) {
            throw new ValidationException("Cannot change the start date. The task is already started",
                    Messages.Keys.VALIDATION_TASK_CANNOT_CHANGE_START_ALREADY_STARTED);
        }

        if (this.endDate != null) {
            throw new ValidationException("Task already finished",
                    Messages.Keys.VALIDATION_TASK_CANNOT_MODIFY_FINISHED);
        }

        this.startDate = toZeroHourDate(startDate);
    }

    /**
     *
     * @throws ValidationException
     */
    public void changeForeseenEndDate(Date foreseen) throws ValidationException {
        if (this.endDate != null) {
            throw new ValidationException("Task already finished",
                    Messages.Keys.VALIDATION_TASK_CANNOT_MODIFY_FINISHED);
        }

        this.foreseenEndDate = toMaxHourDate(foreseen);
    }

    /**
     * Calculates the foreseenWorkHours automatically, based on start/foreseen dates,
     * and on the number of workers.
     */
    public void calculateForeseenWorkHours() {
        Calendar dt = Calendar.getInstance();
        dt.setTime(this.startDate);

        int numberOfWorkDays = 0;

        while (dt.getTime().before(this.foreseenEndDate)) {
            int dayOfWeek = dt.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY
                    && dayOfWeek != Calendar.SUNDAY) {
                numberOfWorkDays++;
            }
            dt.add(Calendar.DAY_OF_MONTH, 1);
        }

        this.foreseenWorkHours = (numberOfWorkDays * WORKDAY_DURATION_IN_HOURS) * owners.size();
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

    public Integer getForeseenWorkHours() {
        return foreseenWorkHours;
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

    public void addPost(Post p) throws ValidationException {
        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }

        String workedHoursExpression = messages.get(Messages.Keys.PARAMETER_TASK_WORKED_HOURS_POST_EXPRESSION);
        Pattern pattern = Pattern.compile(workedHoursExpression);
        Matcher matcher = pattern.matcher(p.getText());
        if (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            String day = new SimpleDateFormat(WORKDAY_DATE_FORMAT).format(p.getTimestamp());
            TaskOwner to = getOwnerByLogin(p.getUser());
            if (to == null) {
                throw new ValidationException(
                        "Post informing work hours by a user that is not on the task [" + p.getUser() + "]",
                        Messages.Keys.VALIDATION_TASK_CANNOT_RECORD_WORK_HOURS_USER_NOT_IN_TASK);
            }
            to.addWorkHours(day, hours);
        }

        this.posts.add(p);
    }

    private TaskOwner getOwnerByLogin(String login) {
        for (TaskOwner to : owners) {
            if (to.getLogin().equals(login)) {
                return to;
            }
        }
        return null;
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

            json.addProperty("foreseenWorkHours", task.getForeseenWorkHours());

            json.addProperty("source", task.getSource());
            json.addProperty("application", task.getApplication().getName());

            JsonArray owners = new JsonArray();
            for (TaskOwner to : task.getOwners()) {
                JsonObject owner = new JsonObject();
                owner.addProperty("login", to.getLogin());
                owner.addProperty("name", to.getName());
                owner.addProperty("type", to.getType());

                JsonArray workDays = new JsonArray();
                for (TaskOwner.WorkDay workDay : to.getWorkDays()) {
                    JsonObject workDayJson = new JsonObject();
                    workDayJson.addProperty("day", workDay.getDay());
                    workDayJson.addProperty("hours", workDay.getHours());
                    workDays.add(workDayJson);
                }

                owner.add("workDays", workDays);

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
            t.foreseenWorkHours = (obj.get("foreseenWorkHours") == null ? null : obj.get("foreseenWorkHours").getAsInt());
            t.endDate = (obj.get("endDate") == null ? null : parseDate(obj.get("endDate").getAsString(), true));
            t.source = (obj.get("source") == null ? null : obj.get("source").getAsString());
            t.application = (obj.get("application") == null ? null : new Application(obj.get("application").getAsString()));

            if (obj.get("owners") != null) {
                List<TaskOwner> owners = new ArrayList<>();

                for (JsonElement jsonOwner : obj.get("owners").getAsJsonArray()) {
                    JsonObject jsonOwnerObject = jsonOwner.getAsJsonObject();
                    String login = jsonOwnerObject.get("login").getAsString();
                    String name = jsonOwnerObject.get("name") == null
                            ? null : jsonOwnerObject.get("name").getAsString();
                    String usrType = jsonOwnerObject.get("type") == null
                            ? null : jsonOwnerObject.get("type").getAsString();

                    TaskOwner o = new TaskOwner(login, name, usrType);

                    if (jsonOwnerObject.get("workDays") != null) {
                        for (JsonElement jsonWorkDay : jsonOwnerObject.get("workDays").getAsJsonArray()) {
                            JsonObject jsonDayObject = jsonWorkDay.getAsJsonObject();
                            String day = jsonDayObject.get("day").getAsString();
                            int hours = jsonDayObject.get("hours").getAsInt();

                            o.addWorkHours(day, hours);
                        }
                    }

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
