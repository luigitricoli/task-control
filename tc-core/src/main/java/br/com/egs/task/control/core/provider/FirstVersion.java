package br.com.egs.task.control.core.provider;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.egs.task.control.core.service.TasksService;

@ApplicationPath("v1")
public class FirstVersion extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> v1 = new HashSet<Class<?>>();
		v1.add(TasksService.class);
		return v1;
	}

}
