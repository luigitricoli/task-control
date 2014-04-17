package br.com.egs.task.control.core.reports;

import br.com.egs.task.control.core.entities.Task;
import br.com.egs.task.control.core.entities.TaskOwner;
import br.com.egs.task.control.core.repository.TaskSearchCriteria;
import br.com.egs.task.control.core.repository.Tasks;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * TODO: Class description
 */
public class TaskReportGeneratorImpl implements TaskReportGenerator {

    private Tasks taskRepository;

    @Inject
    public TaskReportGeneratorImpl(Tasks taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public SimpleMonthlyReportResult generateSimpleMonthlyReport(int year, int month) {
        TaskSearchCriteria criteria = new TaskSearchCriteria().month(year, month);
        List<Task> tasks = taskRepository.searchTasks(criteria);

        SimpleMonthlyReportResult result = new SimpleMonthlyReportResult();

        for (Task task : tasks) {
            // The report presents one record per owner, not one record per task
            // Project hours are divided among the workers
            int totalHours = calculateWorkHours(task, year, month);
            int numberOfWorkers = task.getOwners().size();

            for (TaskOwner owner : task.getOwners()) {
                result.addItem(new SimpleMonthlyReportResult.Item(
                        owner.getName(),
                        task.getApplication().getName(),
                        totalHours / numberOfWorkers,
                        task.getDescription()));
            }
        }

        return result;
    }

    private int calculateWorkHours(Task task, int reportYear, int reportMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getStartDate());

        // If the task does not contain an end date, it stretches to the current day
        Date end = task.getEndDate() != null ? task.getEndDate() : new Date();

        int totalHours = 0;
        while (cal.getTime().before(end)) {

            boolean isWorkday = cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;

            boolean isInDesiredMonth = cal.get(Calendar.YEAR) == reportYear
                    && (cal.get(Calendar.MONTH) + 1) == reportMonth;

            if (isWorkday && isInDesiredMonth) {
                totalHours += 8;
            }

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return totalHours;
    }
}
