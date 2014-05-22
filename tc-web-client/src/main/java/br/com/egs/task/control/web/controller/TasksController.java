package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.rest.client.task.TaskDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

@Resource
@AuthRequired
public class TasksController {

    private static final Logger log = LoggerFactory.getLogger(TasksController.class);
    public static final String EMPTY = "";

    private Result result;
    private TaskRepository tasks;
    private SessionUser user;

    public TasksController(Result result, TaskRepository repository, SessionUser user) {
        this.result = result;
        this.tasks = repository;
        this.user = user;
    }

    @Get("/tarefas")
    public void index(String task) {
        if (task != null) {
            result.include("openTask", task);
        }
    }

    @Get("/tarefas/mes/{month}")
    public void tasksBy(Integer month, String filters) {
        if (filters == null) {
            result.include("weeks", tasks.weeksBy(month));
        } else {
            result.include("weeks", tasks.weeksBy(month, Arrays.asList(filters.split(","))));
        }

    }

    @Post("/tarefas")
    public void addTask(String start, String foreseen, String type, String system, String description, List<String> owners) {
        if(!isValidTask(start, foreseen, type, system, description, owners)){
            return;
        }

        log.debug("Description: {}", description);
        if (tasks.add(start, foreseen, type, system, description, owners)) {
            result.use(Results.http()).body("success");
        } else {
            result.use(Results.http()).body("fail");
        }
    }

    public boolean isValidTask(String start, String foreseen, String type, String system, String description, List<String> owners) {
        Pattern pValidDate = Pattern.compile("([0-2][0-9]|3[0-1])\\/(0[1-9]|1[0-2])\\/[1-9][0-9]");
        if (start == null || !pValidDate.matcher(start).matches()) {
            result.use(Results.http()).body("Data de início inválida");
            return false;
        } else if (foreseen == null || !pValidDate.matcher(foreseen).matches()) {
            result.use(Results.http()).body("Data fim inválida");
            return false;
        } else if (description == null || description.equals(EMPTY)) {
            result.use(Results.http()).body("Descrição inválida");
            return false;
        } else if (type == null || system == null || owners == null) {
            result.use(Results.http()).body("fail");
            return false;
        }
        return true;
    }


    @Put(value = "/tarefas/{task}/finalizacao")
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
        br.com.egs.task.control.web.model.Post post = new br.com.egs.task.control.web.model.Post(Calendar.getInstance(), user.getName(), text);
        if (tasks.add(post, task)) {
            result.use(Results.http()).body("success");
        } else {
            result.use(Results.http()).body("fail");
        }
    }

}
