package br.com.egs.task.control.core.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import br.com.egs.task.control.core.testutils.TestConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.egs.task.control.core.database.DbConfiguration;
import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.Tasks;
import br.com.egs.task.control.core.repository.impl.TasksRepositoryImpl;

import com.mongodb.BasicDBObject;

public class TasksRepositoryTest {

	private Tasks repository;
	private MongoDbConnection conn;
	
	@Before
	public void setUp() {
		conn = TestConnectionFactory.getConnection();
		populateDatabase();
		repository = new TasksRepositoryImpl(conn);
	}

	@After
	public void tearDown() {
		conn.getDatabase().getCollection("tasks").drop();
		conn.close();
	}
	

	private void populateDatabase() {

	}
}
