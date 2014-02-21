package br.com.egs.task.control.core.repository.impl;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import com.mongodb.*;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.util.*;

public class TasksRepositoryImpl implements Tasks {

	private MongoDbConnection connection;

    /**
     * By default, calculations that depend on the current date are made using
     * the object created by "new java.util.Date()".
     *
     * Whenever there is need to use a fixed reference date (for example, testing
     * in a controlled environment) this field must be set.
     */
    private Date fixedReferenceDate;

    @Inject
	public TasksRepositoryImpl(MongoDbConnection connection) {
		this.connection = connection;
	}

    public TasksRepositoryImpl(MongoDbConnection connection, Date fixedReferenceDate) {
        this.connection = connection;
        this.fixedReferenceDate = fixedReferenceDate;
    }

    @Override
    public Task get(String id) {
        ObjectId idObject = new ObjectId(id);
        BasicDBObject filter = new BasicDBObject("_id", idObject);
        BasicDBObject result = (BasicDBObject) connection.getDatabase().getCollection("tasks").findOne(filter);
        if (result == null) {
            return null;
        } else {
            return Task.fromDbObject(result);
        }
    }

    @Override
    public List<Task> searchTasks(TaskSearchCriteria criteria) {

        DBObject filterObject = createFilterObject(criteria);
        DBObject keys = createKeysObject(criteria);

        DBCursor cursor;
        if (keys == null) {
            cursor = connection.getDatabase().getCollection("tasks").find(filterObject);
        } else {
            cursor = connection.getDatabase().getCollection("tasks").find(filterObject, keys);
        }

        List<Task> result = new ArrayList<>();
        while (cursor.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject) cursor.next();
            result.add(Task.fromDbObject(dbObject));
        }

        return result;
    }

    @Override
    public Task add(Task task) {
        BasicDBObject dbObject = task.toDbObject();
        DBCollection collection = connection.getDatabase().getCollection("tasks");

        collection.insert(dbObject);

        // Now the document has the _id attribute set.
        return Task.fromDbObject(dbObject);
    }

    private DBObject createKeysObject(TaskSearchCriteria criteria) {
        if (criteria.isExcludePosts()) {
            return new BasicDBObject("posts", 0);
        } else {
            return null;
        }
    }

    BasicDBObject createFilterObject(TaskSearchCriteria criteria) {
        List<BasicDBObject> filters = new ArrayList<>();

        if (criteria.getMonth() > 0) {
            filters.add(createYearMonthFilter(criteria.getYear(), criteria.getMonth()));
        }

        if (StringUtils.isNotBlank(criteria.getApplication())) {
            filters.add(createApplicationFilter(criteria.getApplication()));
        }

        if (criteria.getSources() != null && criteria.getSources().length > 0) {
            filters.add(createSourceFilter(criteria.getSources()));
        }

        if (StringUtils.isNotBlank(criteria.getOwnerLogin())) {
            filters.add(createOwnerFilter(criteria.getOwnerLogin()));
        }

        if (criteria.getStatus() != null && criteria.getStatus().length > 0) {
            filters.add(createStatusFilter(criteria.getStatus()));
        }

        if (filters.size() == 0) {
            // No filters
            return new BasicDBObject();
        } else if (filters.size() == 1) {
            // A single filter. Return the corresponding criteria directly
            return filters.get(0);
        } else {
            // Multiple filters. Join them in a $and structure
            return new BasicDBObject("$and", filters);
        }
    }

    private BasicDBObject createStatusFilter(TaskSearchCriteria.Status[] status) {
        // Put in a set to remove duplicates
        EnumSet<TaskSearchCriteria.Status> statusSet = EnumSet.copyOf(Arrays.asList(status));

        List<BasicDBObject> filters = new ArrayList<>();

        if (statusSet.contains(TaskSearchCriteria.Status.DOING)) {
            filters.add(new BasicDBObject("endDate", new BasicDBObject("$exists", false)));
        }

        if (statusSet.contains(TaskSearchCriteria.Status.FINISHED)) {
            filters.add(new BasicDBObject("endDate", new BasicDBObject("$exists", true)));
        }

        if (statusSet.contains(TaskSearchCriteria.Status.WAITING)) {
            Calendar endOfCurrentDate = Calendar.getInstance();
            endOfCurrentDate.setTime(getDate());
            endOfCurrentDate.set(Calendar.HOUR_OF_DAY, 23);
            endOfCurrentDate.set(Calendar.MINUTE, 59);
            endOfCurrentDate.set(Calendar.SECOND, 59);
            endOfCurrentDate.set(Calendar.MILLISECOND, 999);

            filters.add(new BasicDBObject("$and", Arrays.asList(
                   new BasicDBObject("endDate", new BasicDBObject("$exists", false)),
                   new BasicDBObject("startDate", new BasicDBObject("$gt", endOfCurrentDate.getTime()))
            )));
        }

        if (statusSet.contains(TaskSearchCriteria.Status.LATE)) {
            Calendar startOfCurrentDate = Calendar.getInstance();
            startOfCurrentDate.setTime(getDate());
            startOfCurrentDate.set(Calendar.HOUR_OF_DAY, 0);
            startOfCurrentDate.set(Calendar.MINUTE, 0);
            startOfCurrentDate.set(Calendar.SECOND, 0);
            startOfCurrentDate.set(Calendar.MILLISECOND, 0);

            filters.add(new BasicDBObject("$or", Arrays.asList(
                    new BasicDBObject("$and", Arrays.asList(
                            new BasicDBObject("endDate", new BasicDBObject("$exists", false)),
                            new BasicDBObject("foreseenEndDate", new BasicDBObject("$lt", startOfCurrentDate.getTime()))
                    )),
                    new BasicDBObject("$and", Arrays.asList(
                            new BasicDBObject("endDate", new BasicDBObject("$exists", true)),
                            new BasicDBObject("$where", "this.endDate > this.foreseenEndDate")
                    ))
            )));
        }

        if (filters.size() == 1) {
            // A single filter. Return the corresponding criteria directly
            return filters.get(0);
        } else {
            // Multiple filters. Join them in a OR structure
            return new BasicDBObject("$or", filters);
        }
    }

    private BasicDBObject createOwnerFilter(String ownerLogin) {
        return new BasicDBObject("owners.login", ownerLogin);
    }

    private BasicDBObject createSourceFilter(String[] sources) {
        BasicDBObject filter;
        if (sources.length == 1) {
            filter = new BasicDBObject("source", sources[0]);
        } else {
            filter = new BasicDBObject("source", new BasicDBObject("$in", sources));
        }
        return filter;
    }

    private BasicDBObject createApplicationFilter(String application) {
        return new BasicDBObject("application.name", application);
    }

    private BasicDBObject createYearMonthFilter(int year, int month) {
        Date[] interval = createDateIntervalForMonth(year, month);

        return new BasicDBObject("$or", new BasicDBObject[]{
                new BasicDBObject("startDate", new BasicDBObject()
                        .append("$gte", interval[0])
                        .append("$lte", interval[1]))
                ,
                new BasicDBObject("foreseenEndDate", new BasicDBObject()
                        .append("$gte", interval[0])
                        .append("$lte", interval[1]))
                ,
                new BasicDBObject("endDate", new BasicDBObject()
                        .append("$gte", interval[0])
                        .append("$lte", interval[1]))
        });
    }

    /**
     * Create a two-position array with the limits (start and end) of the requested month.
     * @param year
     * @param month
     * @return
     */
    Date[] createDateIntervalForMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date begin = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        Date end = cal.getTime();

        return new Date[] {begin, end};
    }

    private Date getDate() {
        if (fixedReferenceDate != null) {
            return fixedReferenceDate;
        } else {
            return new Date();
        }
    }
}
