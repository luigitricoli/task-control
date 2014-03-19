package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Resource
@AuthRequired
public class TasksController {

    private static final Logger log = LoggerFactory.getLogger(TasksController.class);

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

    @Post("/tarefas")
    public void addTask(String start, String foreseen, String type, String system, String description, List<String> owners) {
        if (tasks.add(start, foreseen, type, system, description, owners)) {
            result.use(Results.http()).body("success");
        } else {
            result.use(Results.http()).body("fail");
        }
    }

    @Put(value="/tarefas/{task}/finalizacao")
    public void finish(String task, String date) {
        log.debug("Value task param: {}", task);
        log.debug("Value date param: {}", date);

        try {
            if (tasks.finish(task, new TaskDate(date))) {
                result.use(Results.http()).body("success");
            } else {
                result.use(Results.http()).body("fail");
            }
        } catch (ParseException e) {
            log.error(String.format("The value [%s] of date argument is invalid for the format [yyyy-MM-dd]"), date);
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
