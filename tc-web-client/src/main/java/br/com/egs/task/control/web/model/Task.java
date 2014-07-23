package br.com.egs.task.control.web.model;

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
        return calendarToString(startDate);
    }

    public Calendar getForeseenEndDate() {
        return foreseenEndDate;
    }

    public String getForeseenEndDateAsString() {
        return calendarToString(foreseenEndDate);
    }

    private String calendarToString(Calendar date) {
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH)+1;
        int year = date.get(Calendar.YEAR) % 100;

        StringBuilder sDate = new StringBuilder();
        if(day < 10){
            sDate.append(0);
        }
        sDate.append(day);
        sDate.append("/");
        if(month < 10){
            sDate.append(0);
        }
        sDate.append(month);
        sDate.append("/");
        sDate.append(year);

        return sDate.toString();
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
