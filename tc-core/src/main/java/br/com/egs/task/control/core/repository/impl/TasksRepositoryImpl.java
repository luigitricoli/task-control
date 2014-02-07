package br.com.egs.task.control.core.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.egs.task.control.core.database.mapper.TaskMapper;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;

import br.com.egs.task.control.core.database.MongoDbConnection;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.repository.Tasks;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;

public class TasksRepositoryImpl implements Tasks {

	private MongoDbConnection connection;
    private TaskMapper mapper = new TaskMapper();

    @Inject
	public TasksRepositoryImpl(MongoDbConnection connection) {
		this.connection = connection;
	}


    @Override
         public List<Task> searchTasks(TaskSearchCriteria criteria) {

        DBObject filterObject = createFilterObject(criteria);

        DBCursor cursor = connection.getDatabase().getCollection("tasks").find(filterObject);

        List<Task> result = new ArrayList<>();
        while (cursor.hasNext()) {
            BasicDBObject dbObject = (BasicDBObject) cursor.next();
            result.add(mapper.getAsTask(dbObject));
        }

        return result;
    }

    BasicDBObject createFilterObject(TaskSearchCriteria criteria) {
        List<BasicDBObject> filters = new ArrayList<>();

        if (criteria.getMonth() > 0) {
            Date[] interval = createDateIntervalForMonth(criteria.getYear(), criteria.getMonth());

            filters.add(new BasicDBObject("$or", new BasicDBObject[]{
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
            }));
        }

        if (StringUtils.isNotBlank(criteria.getApplication())) {
            filters.add(new BasicDBObject("application", criteria.getApplication()));
        }

        if (StringUtils.isNotBlank(criteria.getSource())) {
            filters.add(new BasicDBObject("source", criteria.getSource()));
        }

        if (StringUtils.isNotBlank(criteria.getOwnerLogin())) {
            filters.add(new BasicDBObject("owners.login", criteria.getOwnerLogin()));
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


}
