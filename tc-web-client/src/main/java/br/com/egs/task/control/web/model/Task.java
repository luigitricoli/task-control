package br.com.egs.task.control.web.model;

import br.com.egs.task.control.web.model.calendar.CalendarFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Task {

    private String id;
    private String description;
    private Calendar startDate;
    private Calendar foreseenEndDate;
    private String source;
    private String application;
    private List<Post> posts;
    private List<User> owners;
    
    private CalendarFormat calendarFormat = new CalendarFormat();

    public Task(String id, String description, Calendar startDate, Calendar foreseenEndDate, String source, String application, List<Post> posts, List<User> owners) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.source = source;
        this.application = application;
        this.posts = posts;
        this.owners = owners;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public String getStartDateAsString() {
        return calendarFormat.calendarToString(startDate);
    }

    public Calendar getForeseenEndDate() {
        return foreseenEndDate;
    }

    public String getForeseenEndDateAsString() {
        return calendarFormat.calendarToString(foreseenEndDate);
    }

    public String getSource() {
        return source;
    }

    public String getApplication() {
        return application;
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    public List<User> getOwners() {
        return Collections.unmodifiableList(owners);
    }
}
