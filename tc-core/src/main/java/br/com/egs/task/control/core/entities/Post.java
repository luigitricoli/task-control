package br.com.egs.task.control.core.entities;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The POST is an item of a Task's history. Corresponds to a comment or status change.
 */
public class Post {
    private Date timestamp;
    private String user;
    private String text;

    public Post(String user, String text, Date timestamp) {
        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException("Post user cannot be empty");
        }
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Post text cannot be empty");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Post date/time cannot be empty");
        }
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }

    public static Post fromJson(String json) {
        return new GsonBuilder()
                .registerTypeAdapter(Post.class, new PostDeserializer())
                .create()
                .fromJson(json, Post.class);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

            String user = obj.get("user") != null ? obj.get("user").getAsString() : null;
            String text = obj.get("text") != null ? obj.get("text").getAsString() : null;
            Date timestamp = null;
            try {
                timestamp = obj.get("timestamp") != null
                        ? dateParser.parse(obj.get("timestamp").getAsString()) : null;
            } catch (ParseException e) {
                throw new JsonParseException("Invalid Post timestamp: ["
                        + obj.get("timestamp").getAsString() + "]", e);
            }

            Post p = null;
            try {
                p = new Post(user, text, timestamp);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Error creating user: " + e.getMessage(), e);
            }
            return p;
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
}

