package br.com.egs.task.control.core.injection;

import br.com.egs.task.control.core.database.DbConfiguration;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.repository.Tasks;
import br.com.egs.task.control.core.repository.Users;
import br.com.egs.task.control.core.repository.impl.TasksRepositoryImpl;
import br.com.egs.task.control.core.repository.impl.UsersRepositoryImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 */
public class Binder extends AbstractBinder {

    protected void configure() {

       bind(UsersRepositoryImpl.class).to(Users.class);
       bind(TasksRepositoryImpl.class).to(Tasks.class);

       bind(DbConfiguration.class).to(DbConfiguration.class);
       bind(MongoDbConnection.class).to(MongoDbConnection.class);
    }
}
