package br.com.egs.task.control.core.entities;

import java.util.Date;

/**
 * The POST is an item of a Task's history. Corresponds to a comment or status change.
 */
public class Post {
    private Date timestamp;
    private String user;
    private String text;

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
}

