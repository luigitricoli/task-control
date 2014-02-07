package br.com.egs.task.control.core.repository.impl;

import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import com.mongodb.BasicDBObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the repository implementation. We use this for operations which require some logic,
 * for example building search filters.
 */
public class TasksRepositoryImplTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private TasksRepositoryImpl repository = new TasksRepositoryImpl(null);

    @Test
    public void createDateInterval() throws ParseException {
        Date[] interval = repository.createDateIntervalForMonth(2013, 10);
        assertEquals(timestampFormat.parse("2013-10-01 00:00:00.000"), interval[0]);
        assertEquals(timestampFormat.parse("2013-10-31 23:59:59.999"), interval[1]);

        interval = repository.createDateIntervalForMonth(2014, 2);
        assertEquals(timestampFormat.parse("2014-02-01 00:00:00.000"), interval[0]);
        assertEquals(timestampFormat.parse("2014-02-28 23:59:59.999"), interval[1]);
    }

    @Test
    public void filterByMonth() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .month(2014, 2);

        BasicDBObject filter = repository.createFilterObject(criteria);

        BasicDBObject expectedFilter = new BasicDBObject()
            .append("$or", new BasicDBObject[]{
                    new BasicDBObject("startDate", new BasicDBObject()
                            .append("$gte", timestampFormat.parse("2014-02-01 00:00:00.000"))
                            .append("$lte", timestampFormat.parse("2014-02-28 23:59:59.999")))
                    ,
                    new BasicDBObject("foreseenEndDate", new BasicDBObject()
                            .append("$gte", timestampFormat.parse("2014-02-01 00:00:00.000"))
                            .append("$lte", timestampFormat.parse("2014-02-28 23:59:59.999")))
                    ,
                    new BasicDBObject("endDate", new BasicDBObject()
                            .append("$gte", timestampFormat.parse("2014-02-01 00:00:00.000"))
                            .append("$lte", timestampFormat.parse("2014-02-28 23:59:59.999")))
            })
        ;

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
    }

    @Test
    public void filterByApplication() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .application("TaskControl");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{application: 'TaskControl'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterBySource() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .source("CCC");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{source: 'CCC'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterByOwner() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .ownerLogin("mr.dev");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{'owners.login': 'mr.dev'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void combinedFilters() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .application("EMM")
                .source("CCC")
                .ownerLogin("mr.dev");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{$and: [" +
                                        "{'application':'EMM'}," +
                                        "{'source':'CCC'}," +
                                        "{'owners.login': 'mr.dev'}" +
                                "]}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }
}
