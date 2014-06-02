package br.com.egs.task.control.core.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.com.egs.task.control.core.exception.EnvironmentException;

/**
 * Provides access to the database configuration properties.
 *
 */
public class DbConfiguration {

	private static final String DEFAULT_CONFIGURATION_FILE = "/dbconfig.properties";

	private String databaseHost;
	private int databasePort;
	private String databaseName;
	private int databaseMaxConnections;

    private String username;
    private String password;
    private String authenticationDatabase;
	
	public DbConfiguration() {
		this(DEFAULT_CONFIGURATION_FILE);
	}
	
	public DbConfiguration(String configurationFile) {
		if(!configurationFile.startsWith("/")){
			throw new IllegalArgumentException("The configurationFile param needs to begin with slash");
		}
		InputStream propFile = getClass().getResourceAsStream(configurationFile);
		if (propFile == null) {
			throw new EnvironmentException("Configuration file not found in classpath: " + configurationFile);
		}
		
		Properties config = new Properties();
		try {
			config.load(propFile);
		} catch (IOException e) {
			throw new EnvironmentException("Cannot load configuration file", e);
		}
		
		this.databaseHost = config.getProperty("mongodb.server");
		this.databasePort = Integer.parseInt(config.getProperty("mongodb.port"));
		this.databaseName = config.getProperty("mongodb.database");
		this.databaseMaxConnections = Integer.parseInt(config.getProperty("mongodb.maxconnections"));

        this.username = config.getProperty("mongodb.auth.username");
        this.password = config.getProperty("mongodb.auth.password");
        this.authenticationDatabase = config.getProperty("mongodb.auth.database");
	}

	public String getDatabaseHost() {
		return databaseHost;
	}

	public int getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public int getDatabaseMaxConnections() {
		return databaseMaxConnections;
	}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthenticationDatabase() {
        return authenticationDatabase;
    }
}
