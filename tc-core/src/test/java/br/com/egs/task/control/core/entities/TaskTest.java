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
        t.validateForInsert();
    }

    @Test
    public void validateForInsert_ok_startBeforeCurrentDate() throws Exception {
        // The start date can be less than the current date, as long as the
        // foreseen end date is in the future.
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-08 10:00:00.000"));

        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", null, "OLM", false, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.validateForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_nonNullId() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", false, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.validateForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_nonNullEndDate() throws Exception {
        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", "2014-01-09 23:59:59.999", "OLM", false, new TaskOwner("john", "John Foo",
                "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.validateForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_nonNullPosts() throws Exception {
        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.validateForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_foreseenBeforeStartDate() throws Exception {
        Task t = new Task(
                null,
                "Test the Task Implementation",

                timestampFormat.parse("2014-01-11 00:00:00.000"),
                timestampFormat.parse("2014-01-10 23:59:59.999"),
                null,

                "Sup.Producao",
                new Application("OLM"),
                Arrays.asList(new TaskOwner("bob", "Bob Programmer", "N1")));

        t.validateForInsert();
    }

    @Test(expected = ValidationException.class)
    public void validateForInsert_foreseenBeforeCurrentDate() throws Exception {
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-15 23:59:59.999"));

        Task t = createTestTask(null, "Test the Task Implementation", "2014-01-02 00:00:00.000",
                "2014-01-10 23:59:59.999", null, "OLM", false, new TaskOwner("john", "John Foo", "N1"),
                new TaskOwner("mary", "Mary Baz", "N2"));
        t.validateForInsert();
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
        t.addPost(new Post("testusr", "Some #atraso has occured", new Date()));

        Date endDate = timestampFormat.parse("2014-01-20 14:47:48.555");
        t.finish(endDate);

        assertEquals(timestampFormat.parse("2014-01-20 23:59:59.999"), t.getEndDate());
    }

    @Test
    public void finishTask_lateWithAtrasoPost_variation2() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.addPost(new Post("testusr", "The task is #atrasado", new Date()));

        Date endDate = timestampFormat.parse("2014-01-20 14:47:48.555");
        t.finish(endDate);

        assertEquals(timestampFormat.parse("2014-01-20 23:59:59.999"), t.getEndDate());
    }

    @Test
    public void finishTask_lateWithAtrasoPost_variation3() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));
        t.addPost(new Post("testusr", "The task is #atrasada", new Date()));

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
        t.changeStartDate(startDate);

        assertEquals(timestampFormat.parse("2014-01-05 00:00:00.000"), t.getStartDate());
    }

    @Test(expected = ValidationException.class)
    public void changeStartDate_errorAlreadyStarted() throws Exception {
        // Current date AFTER the task start.
        Task.setFixedCurrentDate(timestampFormat.parse("2014-01-06 12:00:00.000"));
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date startDate = timestampFormat.parse("2014-01-05 14:47:48.555");

        t.changeStartDate(startDate);  // ValidationException expected
    }

    @Test
    public void changeForeseenEndDate() throws Exception {
        Task t = createTestTask("111122223333aaaabbbbcccc", "Test the Task Implementation",
                "2014-01-02 00:00:00.000", "2014-01-10 23:59:59.999", null, "OLM", true, new TaskOwner("john",
                "John Foo", "N1"), new TaskOwner("mary", "Mary Baz", "N2"));

        Date foreseen = timestampFormat.parse("2014-01-17 16:28:49.179");
        t.changeForeseenEndDate(foreseen);

        assertEquals(timestampFormat.parse("2014-01-17 23:59:59.999"), t.getForeseenEndDate());
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

        Task t = null;
        try {
            t = new Task(id, description,
                    df.parse(start), df.parse(foreseen),
                    end != null ? df.parse(end) : null,
                    "Project", new Application(applicationName), Arrays.asList(owners));

            if (createDefaultPosts) {
                Post p1 = new Post("john", "Scope changed. No re-scheduling will be necessary",
                        df.parse("2014-01-03 09:15:30.700"));
                t.addPost(p1);

                Post p2 = new Post("john", "Doing #overtime to finish it sooner",
                        df.parse("2014-01-08 18:20:49.150"));
                t.addPost(p2);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return t;
    }

}
