package br.com.egs.task.control.core.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Encapsulates the MongoClient.
 *
 */
@Singleton
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
			throw new RuntimeException("Error initializing database client", e);
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
