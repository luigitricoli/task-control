package br.com.egs.task.control.core.database;

import javax.inject.Inject;

import br.com.egs.task.control.core.exception.EnvironmentException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

/**
 * Encapsulates the MongoClient.
 *
 */
public class MongoDbConnection {

	private MongoClient mongo;
	private DbConfiguration properties;
	
	@Inject
	public MongoDbConnection(DbConfiguration props) {
		MongoClientOptions options = MongoClientOptions.builder()
									.connectionsPerHost(props.getDatabaseMaxConnections())
									.build();
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(
					new ServerAddress(props.getDatabaseHost(), props.getDatabasePort()),
					options);
		} catch (Exception e) {
			throw new EnvironmentException("Error initializing database client", e);
		}
		
		this.mongo = mongoClient;
		this.properties = props;
	}

	/**
	 * Gets a reference to the application database.
	 * @return
	 */
	public DB getDatabase() {
		return mongo.getDB(properties.getDatabaseName());
	}
	
	/**
	 * Close the database connections.
	 */
	public void close() {
		if (mongo != null) {
			mongo.close();
			mongo = null;
		}
	}
}
