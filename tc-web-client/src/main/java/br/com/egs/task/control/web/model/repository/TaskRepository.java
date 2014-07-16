package br.com.egs.task.control.web.model.repository;

import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.exception.InvalidDateException;
import br.com.egs.task.control.web.model.exception.UpdateException;

import java.util.List;

public interface TaskRepository {

	public List<Week> weeksBy(Integer month);
	
	public List<Week> weeksBy(Integer month, List<String> filters, String users);

    public List<Post> postsBy(String taskId);

    public boolean add(String start, String foreseen, String type, String system, String description, List<String> owners);
    
    public boolean add(Post p, String taskId);

    public void finish(String taskId, String date) throws InvalidDateException, UpdateException;

    public void replan(String taskId, String start, String foreseen) throws InvalidDateException, UpdateException;
}
