package br.com.egs.task.control.core.entities;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
