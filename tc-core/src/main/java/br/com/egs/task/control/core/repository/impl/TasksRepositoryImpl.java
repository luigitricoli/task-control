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


}
