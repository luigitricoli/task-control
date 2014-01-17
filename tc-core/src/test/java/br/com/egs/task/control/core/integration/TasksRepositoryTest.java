package br.com.egs.task.control.core.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
	MongoDbConnection conn;
	
	@Before
	public void setUp() {
		DbConfiguration testConfiguration = new DbConfiguration("dbconfig.test.properties");
		conn = new MongoDbConnection(testConfiguration);
		populateDatabase();
		repository = new TasksRepositoryImpl(conn);
	}

	@After
	public void tearDown() {
		conn.getDatabase().getCollection("tasks").drop();
		conn.close();
	}
	
//	@Test
	public void listAllTasks() {
		List<Task> list = repository.listAll();
		assertEquals(2, list.size());
		
		assertEquals("Create the first task in database", list.get(0).getDescription());
		assertEquals("john", list.get(0).getOwner());
	}
	
	
	private void populateDatabase() {
		BasicDBObject[] objects = {
			new BasicDBObject()
				.append("description", "Create the first task in database")
				.append("owner", "john")
			,

			new BasicDBObject()
				.append("description", "Design login page")
				.append("owner", "bob")
		};
		
		conn.getDatabase().getCollection("tasks").insert(objects);
	}
}
