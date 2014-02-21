package br.com.egs.task.control.core.repository.impl;

import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import com.mongodb.BasicDBObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the repository implementation. We use this for operations which require some logic,
 * for example building search filters.
 */
public class TasksRepositoryImplTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private TasksRepositoryImpl repository;

    @Before
    public void setUp() throws ParseException {
        Date referenceDate = timestampFormat.parse("2014-02-20 13:00:00.000");
        repository = new TasksRepositoryImpl(null, referenceDate);
    }

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

        String expectedFilter = "{'application.name': 'TaskControl'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterBySource() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .sources("CCC");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{source: 'CCC'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterBySource_multiple() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .sources("CCC", "Internal");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{source: { $in: [ 'CCC', 'Internal' ] }}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterByStatus_doing() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .status(TaskSearchCriteria.Status.DOING);

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{endDate: { $exists: false }}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterByStatus_finished() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .status(TaskSearchCriteria.Status.FINISHED);

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{endDate: { $exists: true }}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterByStatus_waiting() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .status(TaskSearchCriteria.Status.WAITING);

        BasicDBObject filter = repository.createFilterObject(criteria);

        BasicDBObject expectedFilter = new BasicDBObject("$and", Arrays.asList(
            new BasicDBObject("endDate", new BasicDBObject("$exists", false)),
            new BasicDBObject("startDate", new BasicDBObject("$gt", timestampFormat.parse("2014-02-20 23:59:59.999")))
        ));

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
    }

    @Test
    public void filterByStatus_late() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .status(TaskSearchCriteria.Status.LATE);

        BasicDBObject filter = repository.createFilterObject(criteria);

        /*
        * A task is late if:
        *
        * it has no End Date and the Foreseen End Date is less than today
        * OR
        * it has an End Date, which is greater than the Foreseen End Date
        *
         */
        BasicDBObject expectedFilter = new BasicDBObject("$or", Arrays.asList(
                new BasicDBObject("$and", Arrays.asList(
                      new BasicDBObject("endDate", new BasicDBObject("$exists", false)),
                      new BasicDBObject("foreseenEndDate", new BasicDBObject("$lt",
                                        timestampFormat.parse("2014-02-20 00:00:00.000")))
                )),
                new BasicDBObject("$and", Arrays.asList(
                        new BasicDBObject("endDate", new BasicDBObject("$exists", true)),
                        new BasicDBObject("$where", "this.endDate > this.foreseenEndDate")
                ))
        ));

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
    }

    @Test
    public void filterByStatus_combined() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .status(TaskSearchCriteria.Status.DOING, TaskSearchCriteria.Status.FINISHED);

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{ $or: [" +
                "{endDate: { $exists: false }}," +
                "{endDate: { $exists: true }}" +
        "]}";

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
                .sources("CCC")
                .ownerLogin("mr.dev");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{$and: [" +
                                        "{'application.name':'EMM'}," +
                                        "{'source':'CCC'}," +
                                        "{'owners.login': 'mr.dev'}" +
                                "]}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }
}
