package br.com.egs.task.control.web.model.repository;

import java.util.List;

import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.Week;

public interface TaskRepository {

	List<Task> byMonth();

	List<Week> weekByMonth();

}
