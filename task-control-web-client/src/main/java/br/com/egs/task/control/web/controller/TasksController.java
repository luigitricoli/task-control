package br.com.egs.task.control.web.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.Week;
import br.com.egs.task.control.web.model.repository.TaskRepository;

@Resource
public class TasksController {

	private Result result;
	private TaskRepository tasks;
	
	@Inject
	public TasksController(Result result, TaskRepository repository) {
        this.result = result;
        this.tasks = repository;
	}

	@Get("/tarefas")
	public void index(){
		
	}
	
	@Get("/tarefas/mes/{month}")
	public void tasks(Integer month){
		result.include("weeks", tasks.weekByMonth());
	}
	
}
