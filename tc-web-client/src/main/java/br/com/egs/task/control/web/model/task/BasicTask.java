package br.com.egs.task.control.web.model.task;

import br.com.egs.task.control.web.model.ForeseenType;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BasicTask implements Task {

    private String id;
    private String description;
    private Calendar startDate;
    private Calendar foreseenEndDate;
    private String source;
    private String application;
    private List<Post> posts;
    private List<User> owners;

    private String defaultDateFormat = "dd/MM/yy";

    public BasicTask(String id, String description, Calendar startDate, Calendar foreseenEndDate, String source, String application, List<Post> posts, List<User> owners) {
        this.id = id;
        this.description = description;
        this.startDate = startDate;
        this.foreseenEndDate = foreseenEndDate;
        this.source = source;
        this.application = application;
        this.posts = posts;
        this.owners = owners;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Calendar getStartDate() {
        return startDate;
    }

    @Override
    public String getStartDateAsString() {
        return calendarToString(startDate);
    }

    @Override
    public Calendar getForeseenEndDate() {
        return foreseenEndDate;
    }

    @Override
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

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    @Override
    public List<User> getOwners() {
        return Collections.unmodifiableList(owners);
    }

    @Override
    public Task replan(String start, String foreseen) throws InvalidDateException {
        if(start != null){
            startDate = new TaskDate(start, defaultDateFormat).toCalendar();
        }
        if(foreseen != null){
            foreseenEndDate = new TaskDate(foreseen, defaultDateFormat).toCalendar();
        }

        return this;
    }

    @Override
    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    public static class Builder{



    }
}
