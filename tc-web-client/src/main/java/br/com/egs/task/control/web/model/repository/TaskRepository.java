package br.com.egs.task.control.web.model.repository;

import java.util.List;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.egs.task.control.web.model.AddResponse;
import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.SimpleTask;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.TaskControlWebClientException;
import br.com.egs.task.control.web.model.exception.UpdateException;

public interface TaskRepository {

	public List<Task> get(Integer month, Integer year);
	
	public List<Task> get(Integer month, Integer year, List<String> filters, String users);

    public Task get(String taskId);

    public List<Post> postsBy(String taskId);

    public boolean add(Task task);
    
    public boolean add(Post p, String taskId, UploadedFile upload);

    public void finish(String taskId, String date) throws InvalidDateException, UpdateException;

    public void replan(String taskId, String dateFormat, String start, String foreseen) throws InvalidDateException, UpdateException;
    
    public List<SimpleTask> listTasks(Integer month, Integer year);

    void delete(String task) throws TaskControlWebClientException;

    void update(Task task) throws InvalidDateException, UpdateException;

    public List<SimpleTask> listActiveTasks(String date, String dateFormat) throws InvalidDateException;

}
