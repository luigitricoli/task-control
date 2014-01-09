package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.Task;

@Resource
public class TasksController {

	private Result result;
	
	public TasksController(Result result) {
        this.result = result;
	}

	@Get("/tarefas")
	public void index(){
		
	}
	
	@Get("/tarefas/mes/{month}")
	public Task tasks(Integer month){
		return new Task();
	}
	
}
