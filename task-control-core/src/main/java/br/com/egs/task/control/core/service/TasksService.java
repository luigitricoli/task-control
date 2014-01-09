package br.com.egs.task.control.core.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("tasks")
public class TasksService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String allMonthTasks(){
		return "{\"description\":\"Change Fluxo GPRS Ericsson\"}";
	}
	
}
