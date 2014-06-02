package br.com.egs.task.control.core.database;

import br.com.egs.task.control.core.exception.EnvironmentException;
import com.mongodb.*;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

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

        MongoCredential credential;
        if (StringUtils.isBlank(props.getUsername())) {
            credential = null;
        } else {
            credential = MongoCredential.createMongoCRCredential(
                    props.getUsername(), props.getAuthenticationDatabase(), props.getPassword().toCharArray());
        }

		MongoClient mongoClient;
		try {
            if (credential == null) {
                mongoClient = new MongoClient(
                        new ServerAddress(props.getDatabaseHost(), props.getDatabasePort()),
                        options);
            } else {
                mongoClient = new MongoClient(
                        new ServerAddress(props.getDatabaseHost(), props.getDatabasePort()),
                        Arrays.asList(credential),
                        options);
            }
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
     * Gets a reference to the specified collection.
     * @param name
     * @return
     */
    public DBCollection getCollection(String name) {
        return getDatabase().getCollection(name);
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
