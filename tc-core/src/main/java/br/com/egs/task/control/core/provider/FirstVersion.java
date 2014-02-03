package br.com.egs.task.control.core.provider;

import br.com.egs.task.control.core.injection.Binder;
import br.com.egs.task.control.core.service.TasksService;
import br.com.egs.task.control.core.service.UsersService;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("v1")
public class FirstVersion extends ResourceConfig {

    public FirstVersion() {
        registerClasses(getServiceClasses());
        register(new Binder());
    }

	private Set<Class<?>> getServiceClasses() {
		Set<Class<?>> v1 = new HashSet<Class<?>>();
		v1.add(TasksService.class);
		v1.add(UsersService.class);
		return v1;
	}

}
