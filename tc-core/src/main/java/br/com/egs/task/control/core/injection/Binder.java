package br.com.egs.task.control.core.injection;

import br.com.egs.task.control.core.database.DbConfiguration;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.repository.TasksRepository;
import br.com.egs.task.control.core.repository.UsersRepository;
import br.com.egs.task.control.core.repository.impl.TasksRepositoryImpl;
import br.com.egs.task.control.core.repository.impl.UsersRepositoryImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 */
public class Binder extends AbstractBinder {

    protected void configure() {

       bind(UsersRepositoryImpl.class).to(UsersRepository.class);
       bind(TasksRepositoryImpl.class).to(TasksRepository.class);

       bind(DbConfiguration.class).to(DbConfiguration.class);
       bind(MongoDbConnection.class).to(MongoDbConnection.class);
    }
}
