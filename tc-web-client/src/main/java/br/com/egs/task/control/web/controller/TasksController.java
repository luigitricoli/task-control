package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.exception.TaskControlWebClientException;
import br.com.egs.task.control.web.model.exception.UpdateException;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.task.TaskWebValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@AuthRequired
public class TasksController {

    private static final Logger log = LoggerFactory.getLogger(TasksController.class);
    public static final String EMPTY = "";
    private static final String SUCCESS_RESPONSE_CODE = "success";
    private static final String FAIL_RESPONSE_CODE = "fail";
    private static final String BRAZILIAN_DATE_FORMAT = "dd/MM/yy";

    @Inject
    private Result result;
    @Inject
    private TaskRepository tasks;
    @Inject
    private SessionUser session;



    @Get("/tarefas")
    public void index(String taskId) {
        if (taskId != null) {
            result.include("openTask", taskId);
        }
    }

    @Get("/tarefas/mes/{month}")
    public void tasksBy(Integer month, String filters, String users) {
        if (filters == null) {
            result.include("weeks", tasks.weeksBy(month));
        } else {
            result.include("weeks", tasks.weeksBy(month, Arrays.asList(filters.split(",")), users));
        }

    }

    @Post("/tarefas")
    public void addTask(String start, String foreseen, String type, String system, String description, List<String> owners) {
        if(!isValidTask(start, foreseen, type, system, description, owners)){
            return;
        }

        log.debug("Description: {}", description);
        if (tasks.add(start, foreseen, type, system, description, owners)) {
            result.use(Results.http()).body(SUCCESS_RESPONSE_CODE);
        } else {
            result.use(Results.http()).body(FAIL_RESPONSE_CODE);
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
            result.use(Results.http()).body(FAIL_RESPONSE_CODE);
            return false;
        }
        return true;
    }

    @Delete(value = "/tarefas/{taskId}")
    public void delete(String taskId) {
        try {
            tasks.delete(taskId);
            result.use(Results.http()).body(SUCCESS_RESPONSE_CODE);
        } catch (TaskControlWebClientException e) {
            log.error(e.getMessage());
            result.use(Results.http()).body(FAIL_RESPONSE_CODE);
        }
    }

    @Put(value = "/tarefas/{taskId}/finalizacao")
    public void finish(String taskId, String date) {
        try {
            tasks.finish(taskId, date);
            result.use(Results.http()).body(SUCCESS_RESPONSE_CODE);
        } catch (TaskControlWebClientException e) {
            log.error(e.getMessage());
            result.use(Results.http()).body(FAIL_RESPONSE_CODE);
        }
    }

    @Put(value = "/tarefas/{taskId}/planejamento")
    public void replan(String taskId, String start, String foreseen) {
        try {
            Task task = new TaskWebValidation(tasks.get(taskId), session.getUser());
            tasks.update(task.replan(start, foreseen));
            result.use(Results.http()).body(SUCCESS_RESPONSE_CODE);
        } catch (UpdateException e){
            log.error(e.getMessage());
            result.use(Results.http()).body(e.getMessage());
        } catch (TaskControlWebClientException e) {
            log.error(e.getMessage());
            result.use(Results.http()).body(FAIL_RESPONSE_CODE);
        }
    }

    @Get("/tarefas/{taskId}/historico")
    public void postsBy(String taskId) {
        result.include("task", tasks.get(taskId));
    }

    @Post("/tarefas/{taskId}/historico")
    public void addPost(String taskId, String text) {
        br.com.egs.task.control.web.model.Post post = new br.com.egs.task.control.web.model.Post(
                            Calendar.getInstance(), session.getUser().getNickname(), null, text);
        if (tasks.add(post, taskId)) {
            result.use(Results.http()).body(SUCCESS_RESPONSE_CODE);
        } else {
            result.use(Results.http()).body(FAIL_RESPONSE_CODE);
        }
    }

}
