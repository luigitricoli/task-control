package br.com.egs.task.control.core.repository.impl;

import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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
        Calendar referenceDate = Calendar.getInstance();
        referenceDate.setTime(timestampFormat.parse("2014-02-20 13:00:00.000"));
        repository = new TasksRepositoryImpl(null, referenceDate);
    }

    @Test
    public void filterByMonth_futureTasks() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .month(2015, 2);

        BasicDBObject filter = repository.createFilterObject(criteria);

        /*
            When searching future tasks, the basic search filter is applied.

                     search month begin          search month end
            .................|:::::::::::::::::::::::::|....................
             task 1          |  ++++++++++++++         |
             task 2      ++++|+++++++++                |
             task 3          |                 ++++++++|+++++++
             task 4       +++|+++++++++++++++++++++++++|++++++++++
             task 5   +++++  |                         |
             task 6          |                         | ++++++++++++

             Tasks 1-4 are in different ways related to the search month, and
             must be returned by this search. Tasks 5-6 must be filtered out.

             All of this conditions can be summarized in a simple filter:
             -----> A task is related to the search month if :
                    The startDate is less than the end of the search month.
                            AND
                    The endDate(or foreseenEndDate) is greater than the begin of the search month.
         */

        BasicDBObject expectedFilter = new BasicDBObject("$and", new BasicDBObject[]{
                new BasicDBObject("startDate", new BasicDBObject(
                        "$lte", timestampFormat.parse("2015-02-28 23:59:59.999")))
                ,
                new BasicDBObject("$or", new BasicDBObject[]{
                        new BasicDBObject("foreseenEndDate", new BasicDBObject(
                                "$gte", timestampFormat.parse("2015-02-01 00:00:00.000"))
                        ),
                        new BasicDBObject("endDate", new BasicDBObject(
                                "$gte", timestampFormat.parse("2015-02-01 00:00:00.000"))
                        )
                })
        });

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
    }


    @Test
    public void filterByMonth_pastAndCurrentTasks() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .month(2014, 2);

        BasicDBObject filter = repository.createFilterObject(criteria);

        /*
        When searching past (or current month) tasks, there is an additional match case:

                                     search month begin          search month end
            ................................|:::::::::::::::::::::::::|....................
             task 1       +-------------+   |                         |
                     start date     foreseen end date                 |
                        (task unfinished)   |                         |

             An additional condition handles the tasks that did not end
             (endDate does not exist)
         */
        BasicDBObject expectedFilter = new BasicDBObject("$and", new BasicDBObject[]{
                new BasicDBObject("startDate", new BasicDBObject(
                        "$lte", timestampFormat.parse("2014-02-28 23:59:59.999")))
                ,
                new BasicDBObject("$or", new BasicDBObject[]{
                        new BasicDBObject("foreseenEndDate", new BasicDBObject(
                                "$gte", timestampFormat.parse("2014-02-01 00:00:00.000"))
                        ),
                        new BasicDBObject("endDate", new BasicDBObject(
                                "$gte", timestampFormat.parse("2014-02-01 00:00:00.000"))
                        ),
                        new BasicDBObject("endDate", new BasicDBObject(
                                "$exists", Boolean.FALSE)
                        )
                })
        });

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
    }

    @Test
    public void filterByDayInterval() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar begin = Calendar.getInstance();
        begin.setTime(df.parse("2013-10-09"));

        Calendar end = Calendar.getInstance();
        end.setTime(df.parse("2013-10-23"));

        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .dayInterval(begin, end);

        BasicDBObject filter = repository.createFilterObject(criteria);

        // See explanation on filterByMonth_pastAndCurrentTasks test.
        BasicDBObject expectedFilter = new BasicDBObject("$and", new BasicDBObject[]{
                new BasicDBObject("startDate", new BasicDBObject(
                        "$lte", timestampFormat.parse("2013-10-23 23:59:59.999")))
                ,
                new BasicDBObject("$or", new BasicDBObject[]{
                        new BasicDBObject("foreseenEndDate", new BasicDBObject(
                                "$gte", timestampFormat.parse("2013-10-09 00:00:00.000"))
                        ),
                        new BasicDBObject("endDate", new BasicDBObject(
                                "$gte", timestampFormat.parse("2013-10-09 00:00:00.000"))
                        ),
                        new BasicDBObject("endDate", new BasicDBObject(
                                "$exists", Boolean.FALSE)
                        )
                })
        });

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
    }

    @Test
    public void filterByApplication() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .applications("TaskControl");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{'application.name': 'TaskControl'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void filterByApplication_multiple() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .applications("TaskControl", "EMA");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{'application.name': { $in: ['TaskControl', 'EMA'] } }";

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

        BasicDBObject expectedFilter = new BasicDBObject("$and", Arrays.asList(
                new BasicDBObject("endDate", new BasicDBObject("$exists", false)),
                new BasicDBObject("startDate", new BasicDBObject("$lte", timestampFormat.parse("2014-02-20 23:59:59.999")))
        ));

        JSONAssert.assertEquals(expectedFilter.toString(), filter.toString(), true);
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
                .status(TaskSearchCriteria.Status.WAITING, TaskSearchCriteria.Status.FINISHED);

        BasicDBObject expectedWaitingFilter = new BasicDBObject("$and", Arrays.asList(
                new BasicDBObject("endDate", new BasicDBObject("$exists", false)),
                new BasicDBObject("startDate", new BasicDBObject("$gt", timestampFormat.parse("2014-02-20 23:59:59.999")))
        ));
        Object expectedFinishedFilter = JSON.parse("{endDate: { $exists: true }}");
        BasicDBObject expectedFilter = new BasicDBObject("$or", Arrays.asList(expectedFinishedFilter, expectedWaitingFilter));


        BasicDBObject resultingFilter = repository.createFilterObject(criteria);

        JSONAssert.assertEquals(expectedFilter.toString(), resultingFilter.toString(), true);
    }

    @Test
    public void filterByOwner() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .ownerLogins("mr.dev");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{'owners.login': 'mr.dev'}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }


    @Test
    public void filterByOwner_multiple() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .ownerLogins("mr.dev", "the.programmer");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{'owners.login': { $in: ['mr.dev','the.programmer']}}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }

    @Test
    public void combinedFilters() throws Exception {
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .applications("EMM")
                .sources("CCC")
                .ownerLogins("mr.dev");

        BasicDBObject filter = repository.createFilterObject(criteria);

        String expectedFilter = "{$and: [" +
                                        "{'application.name':'EMM'}," +
                                        "{'source':'CCC'}," +
                                        "{'owners.login': 'mr.dev'}" +
                                "]}";

        JSONAssert.assertEquals(expectedFilter, filter.toString(), true);
    }
}
