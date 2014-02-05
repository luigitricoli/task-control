package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.egs.task.control.web.model.repository.TaskRepository;

@Resource
public class TasksController {

	private Result result;
	private TaskRepository tasks;

	public TasksController(Result result, TaskRepository repository) {
        this.result = result;
        this.tasks = repository;
	}

	@Get("/tarefas")
	public void index(String task){
        if(task != null){
            result.include("openTask", task);
        }
	}
	
	@Get("/tarefas/mes/{month}")
	public void tasksBy(Integer month){
		result.include("weeks", tasks.weekByMonth(month));
	}

    @Get("/tarefas/{task}/historico")
    public void postsBy(String task){
        result.include("posts", tasks.postBy(task));
    }

    @Post("/tarefas/{task}/historico")
    public void addPost(String task){
        //TODO add
        result.redirectTo(this).index(task);
    }
	
}
