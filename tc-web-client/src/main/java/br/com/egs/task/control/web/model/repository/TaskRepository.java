package br.com.egs.task.control.web.model.repository;

import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.rest.client.task.TaskDate;

import java.util.List;

public interface TaskRepository {

	public List<Week> weeksBy(Integer month);
	
	public List<Week> weeksBy(Integer month, List<String> filters);

    public List<Post> postsBy(String taskId);
    
    public boolean add(Post p, String taskId);

    boolean finish(String taskId, TaskDate date);
}
