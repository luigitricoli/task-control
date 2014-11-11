package br.com.egs.task.control.web.model.task;

import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.User;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.UpdateException;

import java.util.Calendar;
import java.util.List;

public class TaskWebValidation implements Task {

    private Task task;
    private User user;

    public TaskWebValidation(Task task, User user) {
        this.task = task;
        this.user = user;
    }


    @Override
    public String getId() {
        return task.getId();
    }

    @Override
    public String getDescription() {
        return task.getId();
    }

    @Override
    public Calendar getStartDate() {
        return task.getStartDate();
    }

    @Override
    public String getStartDateAsString() {
        return task.getStartDateAsString();
    }

    @Override
    public Calendar getForeseenEndDate() {
        return task.getForeseenEndDate();
    }

    @Override
    public String getForeseenEndDateAsString() {
        return task.getForeseenEndDateAsString();
    }

    @Override
    public String getSource() {
        return task.getSource();
    }

    @Override
    public String getApplication() {
        return task.getApplication();
    }

    @Override
    public List<Post> getPosts() {
        return task.getPosts();
    }

    @Override
    public List<User> getOwners() {
        return task.getOwners();
    }

    @Override
    public Task replan(String start, String foreseen) throws InvalidDateException, UpdateException {
        if(!user.isAdmin() && start != null && !start.equals(task.getStartDateAsString())){
            throw new UpdateException("Could not possible to change the start date as normal user.");
        }
        return task.replan(start, foreseen);
    }

    @Override
    public void setDefaultDateFormat(String defaultDateFormat) {
        task.setDefaultDateFormat(defaultDateFormat);
    }
}
