package br.com.egs.task.control.core.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.Tasks;

public class TasksRepositoryImpl implements Tasks {

	private MongoDbConnection connection;
	
	@Inject
	public TasksRepositoryImpl(MongoDbConnection connection) {
		this.connection = connection;
	}

	@Override
	public List<Task> listAll() {
		DB db = connection.getDatabase();
		
		DBCursor cur = db.getCollection("tasks").find();
		
		List<Task> results = new ArrayList<Task>();
		while (cur.hasNext()) {
			BasicDBObject obj = (BasicDBObject) cur.next();
			Task task = new Task();
			task.setDescription(obj.getString("description"));
			task.setOwner(obj.getString("owner"));
			results.add(task);
		}
		return results;
	}

}
