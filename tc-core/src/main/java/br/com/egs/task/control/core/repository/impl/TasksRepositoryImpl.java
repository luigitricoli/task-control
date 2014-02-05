package br.com.egs.task.control.core.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.egs.task.control.core.database.mapper.TaskMapper;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.Tasks;

public class TasksRepositoryImpl implements Tasks {

	private MongoDbConnection connection;
    private TaskMapper mapper = new TaskMapper();

    @Inject
	public TasksRepositoryImpl(MongoDbConnection connection) {
		this.connection = connection;
	}


    @Override
    public List<Task> searchTasks(TaskSearchCriteria criteria) {
        DBCursor cursor = connection.getDatabase().getCollection("tasks").find();

        List<Task> result = new ArrayList<>();
        while (cursor.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject) cursor.next();
            result.add(mapper.getAsTask(dbObject));
        }

        return result;
    }
}
