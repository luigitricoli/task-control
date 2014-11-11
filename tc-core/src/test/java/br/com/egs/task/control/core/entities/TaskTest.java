package br.com.egs.task.control.core.entities;

import br.com.egs.task.control.core.exception.LateTaskException;
import br.com.egs.task.control.core.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class TaskTest {

    private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Before
    public void setUp() throws Exception {
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-01 23:59:59.999"));
    }

    @Test
    public void validateForInsert_ok() throws Exception {
        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", null, "OLM", false, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.prepareForInsert();
    }

    @Test
    public void validateForInsert_ok_startBeforeCurrentDate() throws Exception {
        // The start date can be less than the current date, as long as the
        // foreseen end date is in the future.
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-08 10:00:00.000"));

        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", null, "OLM", false, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.prepareForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_nonNullId() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", false, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.prepareForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_nonNullPosts() throws Exception {
        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.prepareForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_foreseenBeforeStartDate() throws Exception {
        Task t = new Task(
                null,
                "Test the Task Implementation",

                timestampFormat.parse("2014-01-11 00:00:00.000"),
                timestampFormat.parse("2014-01-10 23:59:59.999"),
                null,
                30,

                "Sup.Producao",
                new Application("OLM"),
                Arrays.asList(new TaskOwner("bob", "Bob Programmer", "N1")));

        t.prepareForInsert();
    }

    @Test
    public void finishTask_ontime() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date endDate = timestampFormat.parse("2014-01-10 14:47:48.555");
        t.finish(endDate);

        assertEquals(timestampFormat.parse("2014-01-10 23:59:59.999"), t.getEndDate());
    }

    @Test(expected = ValidationException.class)
    public void finishTask_alreadyFinished() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", "2014-01-09 23:59:59.999", "OLM", true,
                new TaskOwner("john", "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date endDate = timestampFormat.parse("2014-01-10 14:47:48.555");
        t.finish(endDate);
    }

    @Test(expected = LateTaskException.class)
    public void finishTask_lateWithNoAtrasoPost() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date endDate = timestampFormat.parse("2014-01-20 14:47:48.555");
        t.finish(endDate);
    }

    @Test
    public void finishTask_lateWithAtrasoPost_variation1() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.addPost(new Post("testusr", "A Test User", "Some #atraso has occured", new Date()));

        Date endDate = timestampFormat.parse("2014-01-20 14:47:48.555");
        t.finish(endDate);

        assertEquals(timestampFormat.parse("2014-01-20 23:59:59.999"), t.getEndDate());
    }

    @Test
    public void finishTask_lateWithAtrasoPost_variation2() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.addPost(new Post("testusr", "A Test User", "The task is #atrasado", new Date()));

        Date endDate = timestampFormat.parse("2014-01-20 14:47:48.555");
        t.finish(endDate);

        assertEquals(timestampFormat.parse("2014-01-20 23:59:59.999"), t.getEndDate());
    }

    @Test
    public void finishTask_lateWithAtrasoPost_variation3() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.addPost(new Post("testusr", "A Test User", "The task is #atrasada", new Date()));

        Date endDate = timestampFormat.parse("2014-01-20 14:47:48.555");
        t.finish(endDate);

        assertEquals(timestampFormat.parse("2014-01-20 23:59:59.999"), t.getEndDate());
    }

    @Test
    public void changeStartDate() throws Exception {

        // The current date is before the task start.
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-01 12:00:00.000"));

        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date startDate = timestampFormat.parse("2014-01-05 14:47:48.555");
        t.reschedule(startDate, null);

        assertEquals(timestampFormat.parse("2014-01-05 00:00:00.000"), t.getStartDate());
    }

    @Test(expected = ValidationException.class)
    public void changeStartDate_errorAfterForeseenEndDate() throws Exception {
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-01 12:00:00.000"));
        
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date startDate = timestampFormat.parse("2014-01-11 14:47:48.555");

        t.reschedule(startDate, null);  // ValidationException expected
    }

    @Test
    public void changeForeseenEndDate() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date foreseen = timestampFormat.parse("2014-01-17 16:28:49.179");
        t.reschedule(null, foreseen);

        assertEquals(timestampFormat.parse("2014-01-17 23:59:59.999"), t.getForeseenEndDate());
    }

    @Test
    public void calculateForeseenWorkHours_simplestCase() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-02 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"));

        t.calculateForeseenWorkHours();

        assertEquals(8, t.getForeseenWorkHours().intValue());
    }

    @Test
    public void calculateForeseenWorkHours_multipleDays() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-03 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"));

        t.calculateForeseenWorkHours();

        assertEquals(16, t.getForeseenWorkHours().intValue());
    }

    @Test
    public void calculateForeseenWorkHours_nonWorkdays() throws Exception {
        //  2014-01-02 - 2014-01-07  = 6 days, but two of them are weekends = 4 work days
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-07 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"));

        t.calculateForeseenWorkHours();

        assertEquals(32, t.getForeseenWorkHours().intValue());
    }

    @Test
    public void calculateForeseenWorkHours_multipleWorkers() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-03 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"),
                new TaskOwner("buzz", "Buzz Buzzy", "N2"));

        t.calculateForeseenWorkHours();

        assertEquals(48, t.getForeseenWorkHours().intValue());
    }

    @Test
    public void pastTask_generateEndDateAutomatically() throws ValidationException {
        Task t = createTestTask(null, "Test the Task Implementation", "2013-12-23 00:00:00.000",
                "2013-12-25 23:59:59.999", null, "OLM", false, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.prepareForInsert();
        
        assertNotNull(t.getEndDate());
        assertEquals("2013-12-25 23:59:59.999", timestampFormat.format(t.getEndDate()));
    }
    
    @Test
    public void nonPastTask_ignoreEndDate() throws ValidationException {
        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-03 00:00:00.000",
                "2014-01-03 23:59:59.999", "2014-01-03 23:59:59.999", 
                "OLM", false, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.prepareForInsert();
        
        assertNull(t.getEndDate());
    }
    
    @Test
    public void post_notifyWorkedHours() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-03 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"), new TaskOwner("moe", "Mow Bar", "N1"));

        assertEquals(0, t.getOwners().get(0).getWorkDays().size());
        assertEquals(0, t.getOwners().get(1).getWorkDays().size());

        t.addPost(new Post("john", "John The Programmer", "5 #horasutilizadas", timestampFormat.parse("2014-01-02 17:48:00.734")));

        // User "john" now has a 5-hours work day
        assertEquals(1, t.getOwners().get(0).getWorkDays().size());
        assertEquals("2014-01-02", t.getOwners().get(0).getWorkDays().get(0).getDay());
        assertEquals(5, t.getOwners().get(0).getWorkDays().get(0).getHours());

        // No change to the other user
        assertEquals(0, t.getOwners().get(1).getWorkDays().size());
    }

    @Test
    public void post_notifyWorkedHours_withDate() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-03 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"), new TaskOwner("moe", "Mow Bar", "N1"));

        assertEquals(0, t.getOwners().get(0).getWorkDays().size());
        assertEquals(0, t.getOwners().get(1).getWorkDays().size());

        // Passing the worked date in the message body. Post timestamp will be ignored.
        t.addPost(new Post("john", "John The Programmer", "5 #horasutilizadas em 02/01/2014", timestampFormat.parse("2014-01-03 17:48:00.734")));

        // User "john" now has a 5-hours work day
        assertEquals(1, t.getOwners().get(0).getWorkDays().size());
        assertEquals("2014-01-02", t.getOwners().get(0).getWorkDays().get(0).getDay());
        assertEquals(5, t.getOwners().get(0).getWorkDays().get(0).getHours());

        // No change to the other user
        assertEquals(0, t.getOwners().get(1).getWorkDays().size());
    }

    @Test(expected = ValidationException.class)
    public void post_notifyWorkedHours_withDate_invalid() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-03 23:59:59.999", null, "OLM", true,
                new TaskOwner("john", "John Foo", "N1"), new TaskOwner("moe", "Mow Bar", "N1"));

        // Invalid date in post body
        t.addPost(new Post("john", "John The Programmer", "5 #horasutilizadas em 31/15/2014", timestampFormat.parse("2014-01-03 17:48:00.734")));
    }

    private Task createTestTask(
            String id,
            String description,
            String start,
            String foreseen,
            String end,
            String applicationName,
            boolean createDefaultPosts,
            TaskOwner... owners) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Task t;
        try {
            t = new Task(id, description,
                    df.parse(start), df.parse(foreseen),
                    end != null ? df.parse(end) : null,
                    1, "Project", new Application(applicationName), Arrays.asList(owners));

            if (createDefaultPosts) {
                try {
                    Post p1 = new Post("john", "John The Programmer", "Scope changed. No re-scheduling will be necessary",
                            df.parse("2014-01-03 09:15:30.700"));
                    t.addPost(p1);

                    Post p2 = new Post("john", "John The Programmer", "Doing #overtime to finish it sooner",
                            df.parse("2014-01-08 18:20:49.150"));
                    t.addPost(p2);
                } catch (ValidationException ve) {
                    // Not supposed to happen, as the above posts do not have special meaning that could trigger
                    // business rules.
                    throw new RuntimeException(ve.getMessage());
                }
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return t;
    }

}
