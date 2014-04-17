package br.com.egs.task.control.core.reports;

import br.com.egs.task.control.core.entities.Application;
import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TaskReportGeneratorTest {
    private Tasks taskRepository;
    private TaskReportGenerator reportGenerator;

    @Before
    public void setUp() {
        taskRepository = Mockito.mock(Tasks.class);
        reportGenerator = new TaskReportGeneratorImpl(taskRepository);
    }

    @Test
    public void simpleMontlyReport_singledaytasks() {

        List<Task> returnedByRepository = Arrays.asList(

                // 1 whole day = 8 hours
                createTestTask("First Task",
                        "2014-04-01 00:00:00.000", "2014-04-01 23:59:59.000", "2014-04-01 23:59:59.000",
                        "TaskControl", new TaskOwner("s", "Smith", "N1")) ,

                createTestTask("Second Task",
                        "2014-04-01 00:00:00.000", "2014-04-01 23:59:59.000", "2014-04-01 23:59:59.000",
                        "TaskControl", new TaskOwner("s", "Jones", "N1"))
        );

        Mockito.when(taskRepository.searchTasks(Mockito.any(TaskSearchCriteria.class)))
                .thenReturn(returnedByRepository);

        SimpleMonthlyReportResult result = reportGenerator.generateSimpleMonthlyReport(2014, 4);

        assertNotNull(result);
        assertEquals(2, result.getRecords().size());

        assertEquals("Smith", result.getRecords().get(0).getUser());
        assertEquals("TaskControl", result.getRecords().get(0).getApplication());
        assertEquals(8, result.getRecords().get(0).getHoursInMonth());
        assertEquals("First Task", result.getRecords().get(0).getTaskDescription());

        assertEquals("Jones", result.getRecords().get(1).getUser());
        assertEquals("TaskControl", result.getRecords().get(1).getApplication());
        assertEquals(8, result.getRecords().get(1).getHoursInMonth());
        assertEquals("Second Task", result.getRecords().get(1).getTaskDescription());
    }

    @Test
    public void simpleMontlyReport_multidaytask() {
        List<Task> returnedByRepository = Arrays.asList(

                // 3 whole days = 8 hours * 3 = 24
                createTestTask("Single Task",
                        "2014-04-01 00:00:00.000", "2014-04-03 23:59:59.000", "2014-04-03 23:59:59.000",
                        "TaskControl", new TaskOwner("s", "Smith", "N1"))
        );

        Mockito.when(taskRepository.searchTasks(Mockito.any(TaskSearchCriteria.class)))
                .thenReturn(returnedByRepository);

        SimpleMonthlyReportResult result = reportGenerator.generateSimpleMonthlyReport(2014, 4);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
    }

    @Test
    public void simpleMontlyReport_multipleWorkers() {
        List<Task> returnedByRepository = Arrays.asList(

                // 3 whole days = 8 hours * 3 = 24
                // The task is split in the report, 1 record per worker
                createTestTask("Single Task",
                        "2014-04-01 00:00:00.000", "2014-04-03 23:59:59.000", "2014-04-03 23:59:59.000",
                        "TaskControl", 
                        new TaskOwner("s", "Smith", "N1"),
                        new TaskOwner("h", "Harald", "N1"),
                        new TaskOwner("j", "Jimmy", "N1"))
        );

        Mockito.when(taskRepository.searchTasks(Mockito.any(TaskSearchCriteria.class)))
                .thenReturn(returnedByRepository);

        SimpleMonthlyReportResult result = reportGenerator.generateSimpleMonthlyReport(2014, 4);

        assertNotNull(result);
        assertEquals(3, result.getRecords().size());
        assertEquals(8, result.getRecords().get(0).getHoursInMonth());
        assertEquals(8, result.getRecords().get(1).getHoursInMonth());
        assertEquals(8, result.getRecords().get(2).getHoursInMonth());
    }

    @Test
    public void simpleMontlyReport_nonWorkDays() {
        List<Task> returnedByRepository = Arrays.asList(

                // 7 whole days = 8 hours * 7 = 56
                // Apr 5 and 6 are weekends and must not be added, so there are actually
                // 5 days = 40 hours
                createTestTask("Single Task",
                        "2014-04-01 00:00:00.000", "2014-04-07 23:59:59.000", "2014-04-07 23:59:59.000",
                        "TaskControl", new TaskOwner("s", "Smith", "N1"))
        );

        Mockito.when(taskRepository.searchTasks(Mockito.any(TaskSearchCriteria.class)))
                .thenReturn(returnedByRepository);

        SimpleMonthlyReportResult result = reportGenerator.generateSimpleMonthlyReport(2014, 4);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(40, result.getRecords().get(0).getHoursInMonth());
    }

    @Test
    public void simpleMontlyReport_daysOutOfMonth() {
        List<Task> returnedByRepository = Arrays.asList(

                // This task has a number of days before the month specified in the report.
                // They should not be added to the working hours, resulting in only 3 days = 24 hours
                createTestTask("Single Task",
                        "2014-03-17 00:00:00.000", "2014-04-03 23:59:59.000", "2014-04-03 23:59:59.000",
                        "TaskControl", new TaskOwner("s", "Smith", "N1"))
        );

        Mockito.when(taskRepository.searchTasks(Mockito.any(TaskSearchCriteria.class)))
                .thenReturn(returnedByRepository);

        SimpleMonthlyReportResult result = reportGenerator.generateSimpleMonthlyReport(2014, 4);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(24, result.getRecords().get(0).getHoursInMonth());
    }

    private Task createTestTask(
                String description,
                String start,
                String foreseen,
                String end,
                String applicationName,
                TaskOwner... owners) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Task t = null;
        try {
            t = new Task("id", description,
                    df.parse(start), df.parse(foreseen), df.parse(end),
                    "Project", new Application(applicationName), Arrays.asList(owners));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return t;
    }
}
