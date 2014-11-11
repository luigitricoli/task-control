package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.entities.User;
import br.com.egs.task.control.core.exception.LateTaskException;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.TasksRepository;
import br.com.egs.task.control.core.repository.UsersRepository;
import br.com.egs.task.control.core.utils.HttpResponseUtils;
import br.com.egs.task.control.core.utils.Messages;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Path("tasks")
public class TasksService {

	private static final Logger log = LoggerFactory.getLogger(TasksService.class);

    private TasksRepository repository;
    private UsersRepository userRepository;
    private HttpResponseUtils responseUtils;

    @Inject
    public TasksService(TasksRepository repository, UsersRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.responseUtils = new HttpResponseUtils();
    }

    @GET
    @Path("{id}")
    @Produces("application/json; charset=UTF-8")
    public String findById(@PathParam("id") String id) {
        Task result = retrieveTask(id);

        return result.toJson();
    }

    @GET
	@Produces("application/json;charset=UTF-8")
	public String searchTasks(
                            @QueryParam("year") String year,
                            @QueryParam("month") String month,
                            @QueryParam("owner") String owner,
                            @QueryParam("application") String application,
                            @QueryParam("status") String status,
                            @QueryParam("sources") String sources,
                            @QueryParam("excludePosts") String excludePosts,
                            @QueryParam("prettyPrint") String prettyPrint,
                            @QueryParam("dayIntervalStart") String dayIntervalStart,
                            @QueryParam("dayIntervalEnd") String dayIntervalEnd,
                            @QueryParam("excludeForeseenTasks") String excludeForeseenTasks) {

        TaskSearchCriteria criteria = buildSearchCriteria(year, month, owner, application,
                sources, status, excludePosts, dayIntervalStart, dayIntervalEnd, excludeForeseenTasks);

        List<Task> result = repository.searchTasks(criteria);

        boolean isPrettyPrint = true;  // default
        if (StringUtils.isNotBlank(prettyPrint)) {
            isPrettyPrint = Boolean.parseBoolean(prettyPrint);
        }

        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Task.class, new Task.TaskSerializer());
        if (isPrettyPrint) {
            gsonBuilder.setPrettyPrinting();
        }
        return gsonBuilder
                .create()
                .toJson(result);
	}

    @POST
    @Produces("application/json; charset=UTF-8")
    @Consumes("application/json; charset=UTF-8")
    public String create(String body) {

        log.debug("TasksService::create(). Request body:\n{}", body);

        Task task = jsonToTask(body);

        try {
            task.prepareForInsert();
        } catch (ValidationException ve) {
            throw responseUtils.buildUnrecoverableBusinessException(ve.getUserMessageKey());
        }

        List<TaskOwner> ownersWithData = new ArrayList<>();
        for (TaskOwner owner : task.getOwners()) {
            User user = userRepository.get(owner.getLogin());

            if (user == null) {
                throw responseUtils.buildUnrecoverableBusinessException(
                      Messages.Keys.VALIDATION_TASK_INVALID_OWNER, owner.getLogin()
                );
            }

            ownersWithData.add(new TaskOwner(user.getLogin(), user.getName(), user.getType()));
        }

        task.getOwners().clear();
        task.getOwners().addAll(ownersWithData);

        task = repository.add(task);
        return task.toJson();
    }

    @POST
    @Path("{id}")
    @Produces("application/json;charset=UTF-8")
    public String addPost(@PathParam("id") String id, String body) {

        log.debug("TasksService::addPost(). ID=[ {} ]. Request body:\n{}", id, body);

        Post post;
        try {
            post = Post.fromJson(body);
        } catch (JsonParseException jpe) {
            throw responseUtils.buildBadRequestException(
                    Messages.Keys.VALIDATION_GENERAL_MALFORMED_REQUEST_ARG, jpe.getMessage());
        }

        User user = userRepository.get(post.getLogin());
        if (user == null) {
            throw responseUtils.buildUnrecoverableBusinessException(
                    Messages.Keys.VALIDATION_TASK_INVALID_OWNER, post.getLogin()
            );
        }
        post.setName(user.getName());

        Task task = retrieveTask(id);
        try {
            task.addPost(post);
        } catch (ValidationException e) {
            throw responseUtils.buildUnrecoverableBusinessException(e.getUserMessageKey(), e.getUserMessageArgs());
        }
        repository.update(task);

        return task.toJson();
    }

    /**
     * Used to finish or reschedule a task.
     * @param id Task ID
     * @param body The JSON request body
     * @return  Task JSON representation
     */
    @PUT
    @Path("{id}")
    @Produces("application/json;charset=UTF-8")
    public String modifyTask(@PathParam("id") String id, String body) {

        log.debug("TasksService::modifyTask(). ID: [ {} ]. Request body:\n{}", id, body);

        Task changedAttributes = jsonToTask(body);
        Task task = retrieveTask(id);

        if (changedAttributes.getEndDate() != null) {
            log.debug("TasksService::modifyTask(). ID: [ {} ]. Request has endDate, finishing task.", id);

            try {
                task.finish(changedAttributes.getEndDate());
            } catch (LateTaskException lte) {
                throw responseUtils.buildRecoverableBusinessException(lte.getUserMessageKey());
            } catch (ValidationException e) {
                throw responseUtils.buildUnrecoverableBusinessException(e.getUserMessageKey());
            }

        } else if (changedAttributes.getStartDate() != null || changedAttributes.getForeseenEndDate() != null) {
            log.debug(
                "TasksService::modifyTask(). ID: [ {} ]. Requested has startDate and/or foreseenEndDate, rescheduling."
                        + " New startDate=[{}], new foreseenEndDate=[{}]", 
                    id, changedAttributes.getStartDate(), changedAttributes.getForeseenEndDate());

            try {
                task.reschedule(changedAttributes.getStartDate(), changedAttributes.getForeseenEndDate());
            } catch (ValidationException e) {
                throw responseUtils.buildUnrecoverableBusinessException(e.getUserMessageKey());
            }

            if (changedAttributes.getForeseenWorkHours() == null || changedAttributes.getForeseenWorkHours() == 0) {
                // If not provided, calculate automatically
                task.calculateForeseenWorkHours();
            }

        } else {
            log.debug("TasksService::modifyTask(). ID: [ {} ]. No valid operation was present in the request.", id);
            throw responseUtils.buildBadRequestException(
                    Messages.Keys.VALIDATION_TASK_CHANGE_NO_OPERATION_SELECTED);
        }

        repository.update(task);

        return task.toJson();
    }

    @DELETE
    @Path("{id}")
    public void cancelTask(@PathParam("id") String id) {
        log.info("Cancelling (DELETE) task. ID: [ {} ]", id);

        Task task = retrieveTask(id);
        repository.remove(task);
    }

    private TaskSearchCriteria buildSearchCriteria(String year, String month, String owner,
                                                   String application, String sources, String status,
                                                   String excludePosts,
                                                   String startDay, String endDay, String excludeForeseenTasks) {
        TaskSearchCriteria criteria = new TaskSearchCriteria();

        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
            // Selected interval parameter: Month/year
            try {
                criteria.month(Integer.parseInt(year, 10), Integer.parseInt(month, 10));
            } catch (IllegalArgumentException iae) {
                throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_TASK_YEAR_OR_MONTH_INVALID);
            }

        } else if (StringUtils.isNotBlank(year) && !StringUtils.isNotBlank(month)
                || !StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month)) {
            // Selected interval parameter: Month/year - One of the values is missing
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_TASK_YEAR_AND_MONTH_USE_BOTH);

        } else if (StringUtils.isNotBlank(startDay) && StringUtils.isNotBlank(endDay)) {
            // Selected interval parameter: Start Date / End Date

            DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            parser.setLenient(false);

            Calendar beginCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            try {
                beginCal.setTime(parser.parse(startDay));
                endCal.setTime(parser.parse(endDay));
            } catch (ParseException e) {
                throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_TASK_INVALID_SEARCH_DATE);
            }

            if (beginCal.after(endCal)) {
                throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_TASK_INVALID_SEARCH_DATE_BEGIN_AFTER_END);
            }

            criteria.dayInterval(beginCal, endCal);

        } else if (StringUtils.isNotBlank(startDay) && !StringUtils.isNotBlank(endDay)
                || !StringUtils.isNotBlank(startDay) && StringUtils.isNotBlank(endDay)) {
            // Selected interval parameter: Start Date / End Date   - One of the values is missing
            throw responseUtils.buildBadRequestException(Messages.Keys.VALIDATION_TASK_DAY_INTERVAL_USE_BOTH);
        }

        if (StringUtils.isNotBlank(owner)) {
            String[] ownerTokens = owner.split(",");
            criteria.ownerLogins(ownerTokens);
        }

        if (StringUtils.isNotBlank(application)) {
            String[] applicationTokens = application.split(",");
            criteria.applications(applicationTokens);
        }

        if (StringUtils.isNotBlank(sources)) {
            String[] sourceTokens = sources.split(",");
            criteria.sources(sourceTokens);
        }

        if (StringUtils.isNotBlank(status)) {
            String[] statusTokens = status.split(",");
            TaskSearchCriteria.Status[] statusFilter = new TaskSearchCriteria.Status[statusTokens.length];

            for (int i = 0; i < statusTokens.length; i++) {
                String statusToken = statusTokens[i];
                try {
                    statusFilter[i] = TaskSearchCriteria.Status.valueOf(statusToken.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw responseUtils.buildBadRequestException(
                            Messages.Keys.VALIDATION_TASK_INVALID_STATUS, statusToken,
                            Arrays.toString(TaskSearchCriteria.Status.values()));
                }
            }

            criteria.status(statusFilter);
        }

        if (Boolean.parseBoolean(excludePosts)) {
             criteria.excludePosts();
        }

        if (Boolean.parseBoolean(excludeForeseenTasks)) {
            criteria.excludeForeseenTasks();
        }

        return criteria;
    }

    private Task retrieveTask(String id) {
        if (!id.matches("[0-9a-fA-F]{24}")) {
            throw responseUtils.buildNotFoundException(
                    Messages.Keys.VALIDATION_TASK_INVALID_ID, id);
        }

        Task task = repository.get(id);
        if (task == null) {
            throw responseUtils.buildNotFoundException(
                    Messages.Keys.VALIDATION_TASK_ID_NOT_FOUND, id);
        }
        return task;
    }

    private Task jsonToTask(String json) {
        if (StringUtils.isBlank(json)) {
            throw responseUtils.buildBadRequestException(
                    Messages.Keys.VALIDATION_GENERAL_REQUEST_BODY_CANNOT_BE_NULL);
        }

        Task task;
        try {
            task = Task.fromJson(json);
        } catch (JsonParseException jpe) {
            if (jpe.getCause() instanceof ParseException) {
                // Error generated when parsing a specific field
                throw responseUtils.buildBadRequestException(
                        Messages.Keys.VALIDATION_GENERAL_MALFORMED_REQUEST_ARG, jpe.getMessage());
            } else {
                // General JSON parse error
            }   throw responseUtils.buildBadRequestException(
                    Messages.Keys.VALIDATION_GENERAL_MALFORMED_REQUEST);
        }
        return task;
    }
}
