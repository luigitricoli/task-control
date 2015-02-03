package br.com.egs.task.control.web.controller;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.view.Results;
import br.com.egs.task.control.web.interceptor.AuthRequired;
import br.com.egs.task.control.web.model.ForeseenType;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.SimpleTask;
import br.com.egs.task.control.web.model.Task;
import br.com.egs.task.control.web.model.exception.TaskControlWebClientException;
import br.com.egs.task.control.web.model.exception.UpdateException;
import br.com.egs.task.control.web.model.repository.TaskRepository;
import br.com.egs.task.control.web.model.task.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Controller
@AuthRequired
public class TasksController {

    private static final Logger log = LoggerFactory.getLogger(TasksController.class);
    public static final String EMPTY = "";
    private static final String SUCCESS_RESPONSE_CODE = "success";
    private static final String FAIL_RESPONSE_CODE = "fail";

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

    @Get("/tarefas/mes/{date}")
    public void tasksBy(String date, String filters, String users) {
        String[] datePartials = date.split("-");
        int month = Integer.valueOf(datePartials[0]);
        int year = Integer.valueOf(datePartials[1]);
        List<Task> monthTasks;
        if (filters == null) {
            monthTasks = tasks.get(month, year);

        } else {
            monthTasks = tasks.get(month, year, Arrays.asList(filters.split(",")), users);
        }
        result.use(Results.http()).body(BasicTask.marshaller().toJson(monthTasks));

    }

    @Post("/tarefas")
    public void addTask(String start, ForeseenType foreseenType, Integer foreseenQtd, String type, String system,
                        String description, List<String> owners, String idType, String idValue, boolean repeat, Integer repeatValue) {
        TaskBuilder tasker = new TaskBuilder();
        tasker.setStartDate(start).setForeseenType(foreseenType).setForeseenQtd(foreseenQtd).setSource(type);
        tasker.setApplication(system).setDescription(description).setTaskType(idType.concat(idValue));
        tasker.addOwnersAsString(owners);

        try {
            Task task = tasker.build();
            boolean status = tasks.add(task);

            if(repeat){
                for(int index = 1; index < repeatValue; index++){
                    task = tasker.addOnStartDate(1).addOnForeseenEndDate(1).build();
                    status = tasks.add(task);
                }
            }

            if (status) {
                result.use(Results.http()).body(SUCCESS_RESPONSE_CODE);
            } else {
                result.use(Results.http()).body(FAIL_RESPONSE_CODE);
            }
        } catch (InvalidTask cause) {
            result.use(Results.http()).body(cause.getMessage());
        }
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
        } catch (UpdateException e) {
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
