package br.com.egs.task.control.core.entities;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The POST is an item of a Task's history. Corresponds to a comment or status change.
 */
public class Post {
    private Date timestamp;
    private String login;
    private String name;
    private String text;

    public Post(String userLogin, String userName, String text, Date timestamp) {
        if (StringUtils.isBlank(userLogin)) {
            throw new IllegalArgumentException("Post user login cannot be empty");
        }
        // Name is allowed to be blank at the construction, it may be filled later.
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Post text cannot be empty");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Post date/time cannot be empty");
        }
        this.login = userLogin;
        this.name = userName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public static Post fromJson(String json) {
        return new GsonBuilder()
                .registerTypeAdapter(Post.class, new PostDeserializer())
                .create()
                .fromJson(json, Post.class);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class PostDeserializer implements JsonDeserializer<Post> {

        private static final DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public Post deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            JsonObject obj = jsonElement.getAsJsonObject();

            if (!obj.has("post")) {
                throw new JsonParseException("'post' root element not found");
            }
            obj = obj.get("post").getAsJsonObject();

            String login = obj.get("login") != null ? obj.get("login").getAsString() : null;
            String name = obj.get("name") != null ? obj.get("name").getAsString() : null;
            String text = obj.get("text") != null ? obj.get("text").getAsString() : null;
            Date timestamp;
            try {
                timestamp = obj.get("timestamp") != null
                        ? dateParser.parse(obj.get("timestamp").getAsString()) : null;
            } catch (ParseException e) {
                throw new JsonParseException("Invalid Post timestamp: ["
                        + obj.get("timestamp").getAsString() + "]", e);
            }

            Post p;
            try {
                p = new Post(login, name, text, timestamp);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Error creating user: " + e.getMessage(), e);
            }
            return p;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (login != null ? !login.equals(post.login) : post.login != null) return false;
        if (name != null ? !name.equals(post.name) : post.name != null) return false;
        if (text != null ? !text.equals(post.text) : post.text != null) return false;
        if (timestamp != null ? !timestamp.equals(post.timestamp) : post.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}

