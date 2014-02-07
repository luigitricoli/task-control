package br.com.egs.task.control.core.service;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	@Produces(MediaType.APPLICATION_JSON)
	public String searchTasks(
                            @QueryParam("year") String year,
                            @QueryParam("month") String month) {

        TaskSearchCriteria criteria = new TaskSearchCriteria();

        if (StringUtils.isBlank(year) || StringUtils.isBlank(month)) {
            throw new WebApplicationException("Year and Month parameters are required", Response.Status.BAD_REQUEST);
        }

        try {
            criteria.month(Integer.parseInt(year), Integer.parseInt(month));
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Invalid year/month value", Response.Status.BAD_REQUEST);
        } catch (IllegalArgumentException iae) {
            throw new WebApplicationException(iae.getMessage(), Response.Status.BAD_REQUEST);
        }

        List<Task> result = repository.searchTasks(criteria);
        return new GsonBuilder().registerTypeAdapter(Task.class, new Task.TaskSerializer())
                   .create().toJson(result);
	}

}
