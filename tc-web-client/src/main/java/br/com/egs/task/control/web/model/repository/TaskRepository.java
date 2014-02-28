package br.com.egs.task.control.web.model.repository;

import java.util.List;

import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Week;

public interface TaskRepository {

	public List<Week> weeksBy(Integer month);
	
	public List<Week> weeksBy(Integer month, List<String> filters);

    public List<Post> postsBy(String taskId);
    
    public boolean add(Post p, String taskId);

}
