import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.egs.task.control.core.database.DbConfiguration;
import br.com.egs.task.control.core.database.MongoDbConnection;

import com.mongodb.BasicDBObject;

/**
 * Inserts some records on the production database. Be sure to have the
 * dbconfig.properties file on the classpath when running this program.
 */
public class TaskInsert {

	public static void main(String[] args) throws Exception {
		populateDatabase();
		System.out.println("Done");
	}

	private static void populateDatabase() throws Exception {
		MongoDbConnection conn = new MongoDbConnection(new DbConfiguration("/dbconfig.dev.properties"));

		BasicDBObject t = createTestTask();
		conn.getCollection("tasks").insert(t);
	}

	private static BasicDBObject createTestTask() throws ParseException {
		DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		BasicDBObject t = new BasicDBObject();

		Long time = System.currentTimeMillis();
		t.append("description", "SR".concat(time.toString()));

		t.append("startDate", timestampFormat.parse("2014-02-03 00:00:00.000"));
		t.append("foreseenEndDate", timestampFormat.parse("2014-02-11 23:59:59.999"));
		t.append("endDate", timestampFormat.parse("2014-02-06 23:59:59.999"));

		t.append("source", "CCC");
		t.append("application", new BasicDBObject("name", "EMA"));

		List<BasicDBObject> owners = new ArrayList<>();
		owners.add(new BasicDBObject("login", "john"));
		owners.add(new BasicDBObject("login", "mary"));
		t.append("owners", owners);

		List<BasicDBObject> posts = new ArrayList<>();
		posts.add(new BasicDBObject().append("timestamp", timestampFormat.parse("2014-01-03 09:15:30.700")).append("user", "john")
				.append("text", "Scope changed. No re-scheduling will be necessary"));
		posts.add(new BasicDBObject().append("timestamp", timestampFormat.parse("2014-01-08 18:20:49.150")).append("user", "john")
				.append("text", "Doing #horaextra to finish it sooner"));
		posts.add(new BasicDBObject().append("timestamp", timestampFormat.parse("2014-01-08 18:20:49.150")).append("user", "john")
				.append("text", "Doing #atraso to finish it sooner"));
		posts.add(new BasicDBObject().append("timestamp", timestampFormat.parse("2014-01-08 18:20:49.150")).append("user", "john")
				.append("text", "Doing to finish it sooner"));
		t.append("posts", posts);

		return t;
	}
}
