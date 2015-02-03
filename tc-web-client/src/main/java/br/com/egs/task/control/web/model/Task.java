package br.com.egs.task.control.web.model;

import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.UpdateException;

import java.util.Calendar;
import java.util.List;

public interface Task {
    public String getId();

    public String getDescription();

    public Calendar getStartDate();

    public String getStartDateAsString();

    public Calendar getForeseenEndDate();

    public String getForeseenEndDateAsString();

    public Calendar getEndDate();

    public String getEndDateAsString();

    Integer getWorkHours();

    public String getSource();

    public String getApplication();

    public List<Post> getPosts();

    public List<User> getOwners();

    public Task replan(String start, String foreseen) throws InvalidDateException, UpdateException;

    void setDefaultDateFormat(String defaultDateFormat);
}
