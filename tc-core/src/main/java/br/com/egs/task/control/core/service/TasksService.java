package br.com.egs.task.control.core.service;

import br.com.egs.task.control.core.entities.Post;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.exception.ValidationException;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import br.com.egs.task.control.core.utils.WebserviceUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.List;

@Path("tasks")
public class TasksService {

	private static final Logger log = LoggerFactory.getLogger(TasksService.class);

    private Tasks repository;

    @Inject
    public TasksService(Tasks repository) {
        this.repository = repository;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findById(@PathParam("id") String id) {
        Task result = retrieveTask(id);

        return result.toJson();
    }

    @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String searchTasks(
                            @QueryParam("year") String year,
                            @QueryParam("month") String month,
                            @QueryParam("owner") String owner,
                            @QueryParam("application") String application,
                            @QueryParam("status") String status,
                            @QueryParam("sources") String sources,
                            @QueryParam("excludePosts") String excludePosts) {

        TaskSearchCriteria criteria = buildSearchCriteria(year, month, owner, application, sources, status, excludePosts);

        List<Task> result = repository.searchTasks(criteria);
        return new GsonBuilder().registerTypeAdapter(Task.class, new Task.TaskSerializer())
                   .create().toJson(result);
	}

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String body) {
        if (StringUtils.isBlank(body)) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Request body cannot by null");
        }

        Task task = null;
        try {
            task = Task.fromJson(body);
        } catch (JsonParseException jpe) {
            if (jpe.getCause() instanceof ParseException) {
                // Error generated when parsing a specific field
                WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, jpe.getMessage());
            } else {
                // General JSON parse error
            }   WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Invalid JSON body");
        }

        try {
            task.validateForInsert();
        } catch (ValidationException ve) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Error validating task: " + ve.getMessage());
        }

        task = repository.add(task);
        return task.toJson();
    }

    @POST
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addPost(@PathParam("id") String id, String body) {
        Post post = null;
        try {
            post = Post.fromJson(body);
        } catch (JsonParseException jpe) {
            if (jpe.getCause() instanceof ParseException) {
                // Error generated when parsing a specific field
                WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, jpe.getMessage());
            } else {
                // General JSON parse error
            }   WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Invalid JSON body");
        }

        Task task = retrieveTask(id);
        task.addPost(post);
        repository.update(task);

        return task.toJson();
    }

    private TaskSearchCriteria buildSearchCriteria(String year, String month, String owner, String application, String sources, String status, String excludePosts) {
        TaskSearchCriteria criteria = new TaskSearchCriteria();

        if (StringUtils.isBlank(year) || StringUtils.isBlank(month)) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Year and Month parameters are required");
        }

        try {
            criteria.month(Integer.parseInt(year, 10), Integer.parseInt(month, 10));
        } catch (NumberFormatException e) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, "Invalid year/month value");
        } catch (IllegalArgumentException iae) {
            WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, iae.getMessage());
        }

        if (StringUtils.isNotBlank(owner)) {
            criteria.ownerLogin(owner);
        }

        if (StringUtils.isNotBlank(application)) {
            criteria.application(application);
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
                    String message = "Invalid status: [" + statusToken + "]. Use one of the following:";
                    for (TaskSearchCriteria.Status statusOption : TaskSearchCriteria.Status.values()) {
                        message = message + " " + statusOption.name();
                    }
                    WebserviceUtils.throwWebApplicationException(Response.Status.BAD_REQUEST, message);
                }
            }

            criteria.status(statusFilter);
        }

        if (Boolean.parseBoolean(excludePosts)) {
             criteria.excludePosts();
        }
        return criteria;
    }

    private Task retrieveTask(String id) {
        if (!id.matches("[0-9a-fA-F]{24}")) {
            WebserviceUtils.throwWebApplicationException(Response.Status.NOT_FOUND, "Invalid Task ID");
        }

        Task task = repository.get(id);
        if (task == null) {
            WebserviceUtils.throwWebApplicationException(Response.Status.NOT_FOUND, "Task not found");
        }
        return task;
    }
}
