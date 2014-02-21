package br.com.egs.task.control.web.model.repository;

import java.util.List;

import br.com.egs.task.control.web.model.Post;
import br.com.egs.task.control.web.model.Week;

public interface TaskRepository {

	List<Week> weeksByMonth(Integer month);

    List<Post> postBy(String id);

}
