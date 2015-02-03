package br.com.egs.task.control.web.model.repository;

import br.com.egs.task.control.web.model.*;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.TaskControlWebClientException;
import br.com.egs.task.control.web.model.exception.UpdateException;
import br.com.egs.task.control.web.model.task.BasicTask;

import java.util.List;

public interface TaskRepository {

	public List<Task> get(Integer month, Integer year);
	
	public List<Task> get(Integer month, Integer year, List<String> filters, String users);

    public Task get(String taskId);

    public List<Post> postsBy(String taskId);

    public boolean add(Task task);
    
    public boolean add(Post p, String taskId);

    public void finish(String taskId, String date) throws InvalidDateException, UpdateException;

    public void replan(String taskId, String dateFormat, String start, String foreseen) throws InvalidDateException, UpdateException;
    
    public List<SimpleTask> listTasks(Integer month, Integer year);

    void delete(String task) throws TaskControlWebClientException;

    void update(Task task) throws InvalidDateException, UpdateException;

    public List<SimpleTask> listActiveTasks(String date, String dateFormat) throws InvalidDateException;

}
