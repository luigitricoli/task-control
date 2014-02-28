package br.com.egs.task.control.web.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
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
	public void index(String task) {
		if (task != null) {
			result.include("openTask", task);
		}
	}

	@Get("/tarefas/mes/{month}")
	public void tasksBy(Integer month, String filters) {	
		if(filters == null){
			result.include("weeks", tasks.weeksBy(month));	
		} else {
			result.include("weeks", tasks.weeksBy(month, Arrays.asList(filters.split(","))));
		}
		
	}

	@Get("/tarefas/{task}/historico")
	public void postsBy(String task) {
		result.include("posts", tasks.postsBy(task));
	}

	@Post("/tarefas/{task}/historico")
	public void addPost(String task, String text) {
		br.com.egs.task.control.web.model.Post post = new br.com.egs.task.control.web.model.Post(Calendar.getInstance(), "Luigi", text);
		if (tasks.add(post, task)) {
			result.use(Results.http()).body("success");
		} else {
			result.use(Results.http()).body("fail");
		}
	}

}
