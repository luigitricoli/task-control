package br.com.egs.task.control.core.testutils;

import br.com.egs.task.control.core.database.DbConfiguration;
import br.com.egs.task.control.core.database.MongoDbConnection;

/**
 * Factory for test database connections.
 */
public class TestConnectionFactory {
    public static MongoDbConnection getConnection() {
        DbConfiguration testConfiguration = new DbConfiguration("/dbconfig.test.properties");
        MongoDbConnection conn = new MongoDbConnection(testConfiguration);

        // Make sure that the tests will not make changes to a production database
        String dbName = conn.getDatabase().getName();
        if (!dbName.endsWith("_test")) {
            throw new RuntimeException("A test database name must end with _test!");
        }

        return conn;
    }
}
